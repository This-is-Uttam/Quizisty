package com.reward.quizisty.Activities;

import static com.reward.quizisty.Utils.Constants.*;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.reward.quizisty.R;
import com.reward.quizisty.Utils.Constants;
import com.reward.quizisty.Utils.ControlRoom;
import com.reward.quizisty.databinding.ActivityVoucherMainDetailBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VoucherMainDetailActivity extends AppCompatActivity {
    ActivityVoucherMainDetailBinding binding;
    int pricePerSpot;
        String vouMainItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVoucherMainDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //        loader
        binding.frameLayout.setVisibility(View.VISIBLE);
        binding.progressBar3.setVisibility(View.VISIBLE);
        binding.message.setVisibility(View.GONE);
        binding.errorImg.setVisibility(View.GONE);

//      vouBidNow disable..
        binding.vouBidNow.setBackgroundTintList(getApplicationContext().getColorStateList(R.color.buttonDisableColor));
        binding.vouBidNow.setClickable(false);
        binding.vouBidNow.setEnabled(false);
        binding.vouBidNow.setText("Please wait");

        /*Intent intent = getIntent();
        trendItemId = intent.getStringExtra("TrendItemId");
        Log.d("qwert", "onCreate: TrendItemId" + trendItemId);
        productItemImgList = new ArrayList<>();*/

        Intent intent = getIntent();
        vouMainItemId = intent.getStringExtra("VOUCHER_MAIN_ID");


        getMainVoucherItem(vouMainItemId);

        binding.vouBidNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyCoinDialog();
            }
        });
    }

    private void getMainVoucherItem(String vouMainItemId) {
        Log.d("getMainVoucherItem", "onCreate: vouMainItemId"+vouMainItemId);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.VOUCHER_MAIN_URL + "/" + vouMainItemId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        Log.d("getMainVoucherItem", "onResponse: response Sucess: " + response.getString("data"));

                        binding.frameLayout.setVisibility(View.GONE);

                        JSONArray jsonArray = response.getJSONArray("data");
                        JSONObject jsonObject = jsonArray.getJSONObject(0);


                        String image = Constants.VOUCHER_IMG_URL+ jsonObject.getString("images");

                        Log.d("getMainVoucherItem", "onResponse: imageVou: " + image);

                        pricePerSpot = (int) Float.parseFloat(jsonObject.getString("price_per_spot"));

                        binding.itemName.setText(jsonObject.getString("name"));
                        binding.vouPricePerSpot.setText(pricePerSpot + "/spot");
                        binding.vouMrp.setText(jsonObject.getString("mrp"));
                        binding.spotLeftText.setText(jsonObject.getString("empty_spot"));
                        binding.progressBar.setProgress(Integer.parseInt(jsonObject.getString("empty_spot")));
                        binding.progressBar.setMax(Integer.parseInt(jsonObject.getString("total_spot")));
                        binding.totalSpotText.setText("out of " + jsonObject.getString("total_spot"));
                        binding.shortDescription.setText(jsonObject.getString("short_desc"));
                        binding.longDetails.setText(jsonObject.getString("details"));

                        Picasso.get()
                                .load(image)
                                .placeholder(R.drawable.placeholder)
                                .into(binding.vouMainImg);


                        int spotFull = jsonObject.getInt("full_status");

                        if (spotFull == 1){
                            //      vouBidNow disable..
                            binding.vouBidNow.setBackgroundTintList(getApplicationContext().getColorStateList(R.color.buttonDisableColor));
                            binding.vouBidNow.setClickable(false);
                            binding.vouBidNow.setEnabled(false);
                            binding.vouBidNow.setText("No Available Spot");

                        }else {
                            checkVoucherBidEligibility();

                            /*binding.vouBidNow.setVisibility(View.VISIBLE);
                            binding.vouBidNow.setBackgroundTintList(getApplicationContext().getColorStateList(R.color.TertiaryColor));
                            binding.vouBidNow.setClickable(true);
                            binding.vouBidNow.setEnabled(true);
                            binding.vouBidNow.setText("Buy Now");*/
                        }





                    } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                        Log.d("getMainVoucherItem", "onResponse: response Failed: " + response.getString("data"));
                        binding.message.setText("Item not found or might be blocked.");
                        binding.message.setVisibility(View.VISIBLE);
//                        binding.errorImg.setVisibility(View.VISIBLE);
                        binding.progressBar3.setVisibility(View.GONE);
                    } else {
                        Log.d("getMainVoucherItem", "onResponse: something went wrong");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getMainVoucherItem", "onResponse: error Response " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(VoucherMainDetailActivity.this));
                return header;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void checkVoucherBidEligibility() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.CHECK_VOUCHER_BID_ELIGIBLE_API + "/" + vouMainItemId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        Log.d("checkVoucherBidEligibility", "onResponse: response Sucess: " + response.getString("data"));

//                      First bidding
                        binding.vouBidOver.setVisibility(View.GONE);

                        binding.vouBidNow.setVisibility(View.VISIBLE);
                        binding.vouBidNow.setBackgroundTintList(getApplicationContext().getColorStateList(R.color.TertiaryColor));
                        binding.vouBidNow.setClickable(true);
                        binding.vouBidNow.setEnabled(true);
                        binding.vouBidNow.setText("Buy Now");

                    } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                        Log.d("checkEligibleBidding", "onResponse: response new: " + response.getString("data"));

                        //                        Winner Announced or Product not found..........

                        binding.vouBidOver.setVisibility(View.VISIBLE);
                        binding.vouBidOver.setText(response.getString("data"));

                        binding.vouBidNow.setVisibility(View.GONE);
                        binding.vouBidNow.setBackgroundTintList(getApplicationContext().getColorStateList(R.color.buttonDisableColor));
                        binding.vouBidNow.setClickable(false);
                        binding.vouBidNow.setEnabled(false);
                        binding.vouBidNow.setText("Can't Buy");

                    }else if (response.getBoolean("status") && response.getInt("code") == 202) {
                        Log.d("checkVoucherBidEligibility", "onResponse: response Failed: " + response.getString("data"));

                        //                        Bidding Again..........

                        binding.vouBidOver.setVisibility(View.GONE);

                        binding.vouBidNow.setVisibility(View.VISIBLE);
                        binding.vouBidNow.setBackgroundTintList(getApplicationContext().getColorStateList(R.color.TertiaryColor));
                        binding.vouBidNow.setClickable(true);
                        binding.vouBidNow.setEnabled(true);
                        binding.vouBidNow.setText("Buy Again");

                    } else {
                        Log.d("checkVoucherBidEligibility", "onResponse: something went wrong");
                        binding.vouBidOver.setVisibility(View.VISIBLE);
                        binding.vouBidOver.setText("Something went wrong");

                        binding.vouBidNow.setBackgroundTintList(getApplicationContext().getColorStateList(R.color.buttonDisableColor));
                        binding.vouBidNow.setClickable(false);
                        binding.vouBidNow.setEnabled(false);
                        binding.vouBidNow.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("checkVoucherBidEligibility", "onResponse: error Response " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(VoucherMainDetailActivity.this));
                return header;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void buyCoinDialog() {
        AlertDialog buyCoinDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.CustomDialog);

        //   diamondsRequired = 10% of pricePerSpot
        int diamondsRequired = (int) Math.floor ((pricePerSpot *10)/100);
//        coinsAfterDiamondsDeduct is coins - diamondsRequired;
        int coinsAfterDiamondsDeduct = pricePerSpot - diamondsRequired;

        View dialogView = getLayoutInflater().inflate(R.layout.buy_coin_dialog_layout, null);
        RadioButton coinsOnlyRadio = dialogView.findViewById(R.id.onlyCoinsRadio);
        RadioButton coinsNdiamondRadio = dialogView.findViewById(R.id.coinsNdiamondRadio);
        TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);
        TextView continueBtn = dialogView.findViewById(R.id.positiveBtn);
        TextView cancelBtn = dialogView.findViewById(R.id.negativeBtn);

        dialogBuilder.setView(dialogView);
        coinsOnlyRadio.setText("Use only coins ("+ pricePerSpot+ " Coins)");
        coinsNdiamondRadio.setText("Use "+ coinsAfterDiamondsDeduct + " Coins + "+diamondsRequired+" Diamonds");
        buyCoinDialog = dialogBuilder.create();

        int userCoins = Integer.parseInt(ControlRoom.getInstance().getCoins(this));
        int userDiamonds = Integer.parseInt(ControlRoom.getInstance().getDiamonds(this));


        boolean isCoinsOnlyBidding;

        //code subham
        Log.d("subham", "buyCoinDialog: price per spot ="+ pricePerSpot+" percent diamond="+diamondsRequired+" coin after deduction=" + coinsAfterDiamondsDeduct);
        if(userCoins >= pricePerSpot && userDiamonds >= diamondsRequired){
            //means user has enough coins and diamond to bid
            coinsNdiamondRadio.setEnabled(true);
            coinsOnlyRadio.setEnabled(true);
            coinsOnlyRadio.setChecked(true);

        }else if(userCoins >= coinsAfterDiamondsDeduct &&  userDiamonds >= diamondsRequired){
            //means user has to use coins with diamond only
            coinsOnlyRadio.setEnabled(false);
            coinsNdiamondRadio.setChecked(true);

        } else if (userCoins >= pricePerSpot && userDiamonds < diamondsRequired) {
            //means user has only coins to bid
            coinsNdiamondRadio.setEnabled(false);
            coinsOnlyRadio.setChecked(true);
        }else{
            //not enough coin and diamond to bid
            coinsNdiamondRadio.setEnabled(false);
            coinsOnlyRadio.setEnabled(false);

        }



        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (coinsOnlyRadio.isChecked()){
                    bidding(Constants.COINS_ONLY_BID_VALUE);
                    buyCoinDialog.dismiss();
                    finish();
                } else if (coinsNdiamondRadio.isChecked()) {
                    bidding(Constants.COINS_DIAMOND_BID_VALUE);
                    buyCoinDialog.dismiss();
                    finish();

                }else {
                    Toast.makeText(VoucherMainDetailActivity.this, "No option is selected", Toast.LENGTH_SHORT).show();
                }

            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyCoinDialog.dismiss();
            }
        });


        buyCoinDialog.show();
    }

    private void bidding(int radioValue) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.VOUCHER_BID_API + "/" + vouMainItemId +"/"+radioValue, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {
                                Log.d("bidding", "onResponse: response Sucessfull: " + response.getString("data"));

                                binding.vouBidNow.setBackgroundTintList(getApplicationContext().getColorStateList(R.color.buttonDisableColor));
                                binding.vouBidNow.setClickable(false);
                                Toast.makeText(VoucherMainDetailActivity.this, "" + response.getString("data"), Toast.LENGTH_SHORT).show();

                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("bidding", "onResponse: response Failed: " + response.getString("data"));
                                Toast.makeText(VoucherMainDetailActivity.this, "" + response.getString("data"), Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("bidding", "onResponse: Something went wrong here");
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("bidding", "onResponse: error ResPonse:  " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, "Bearer " + ControlRoom.getInstance().getAccessToken(VoucherMainDetailActivity.this));
                return header;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }


}
package com.reward.quizisty.Activities;

import static com.reward.quizisty.Utils.Constants.*;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

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
import com.reward.quizisty.databinding.ActivityRedeemViewBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RedeemViewActivity extends AppCompatActivity {

    ActivityRedeemViewBinding binding;
    int voucherId;
    String inputText;
    String voucherInputType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRedeemViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        voucherId = intent.getIntExtra("REDEEM_ITEM_ID", 1);

        binding.frameLayout.setVisibility(View.VISIBLE);
        binding.progressBar3.setVisibility(View.VISIBLE);
        binding.errorImg.setVisibility(View.GONE);
        binding.message.setVisibility(View.GONE);

        getVoucherViewDetails();
//        binding.nameInputLayout.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        binding.redeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                hide Keyboard
                InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                inputText = binding.nameInputLayout.getEditText().getText().toString();
                if (inputText.equals("")){
                    binding.nameInputLayout.setError("This field is required");
                }else {
                    checkInputType(inputText);
                }
            }
        });


    }

    private void checkInputType(String inputText) {
        if (voucherInputType.equals(INPUT_TYPE_EMAIL)){
            if (Patterns.EMAIL_ADDRESS.matcher(inputText).matches()){
                Log.d("redeem", "onClick: input text mathches: "+ inputText);
//                call api now....
                    postRedeemRequest(inputText);

            }else{
                Log.d("redeem", "onClick: input text doesn't mathch: "+ inputText);
                binding.nameInputLayout.setError("Please enter valid email address");
            }
        } else if (voucherInputType.equals(INPUT_TYPE_PHONE)) {
            if (inputText.matches("^[6-9]\\d{9}$")){
                Log.d("redeem", "onClick: input text phone mathches: "+ inputText);
//                call api now....
                postRedeemRequest(inputText);

            }else{
                Log.d("redeem", "onClick: input text phone doesn't mathch: "+ inputText);
                binding.nameInputLayout.setError("Please enter valid phone number");
            }

        } else if (voucherInputType.equals(INPUT_TYPE_NUMBER)) {
            if(inputText.matches("\\d+(?:\\.\\d+)?")){
                Log.d("redeem", "onClick: input text mathches: "+ inputText);
//                call api now....
                postRedeemRequest(inputText);

            }else{
                Log.d("redeem", "onClick: input text doesn't mathch: "+ inputText);
                binding.nameInputLayout.setError("Please enter valid number");
            }

        }else {
            Log.d("redeem", "onClick: input text nor mathches: "+ inputText);
//                call api now....
            postRedeemRequest(inputText);

        }
    }

    private void postRedeemRequest(String inputText) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("redeem_id", voucherId);
            jsonObject.put("redeem_details", inputText);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, REDEEM_REQUEST_URL,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200){
                        Log.d("postRedeemRequest", "onResponse: response Sucessfull: "+ response.getString("data"));

                        Toast.makeText(RedeemViewActivity.this, ""+response.getString("data"), Toast.LENGTH_SHORT).show();
                        finish();



                    }else if (!response.getBoolean("status") && response.getInt("code") == 201){
                        Log.d("postRedeemRequest", "onResponse: response Failed: "+ response.getString("data"));
                        Toast.makeText(RedeemViewActivity.this, ""+response.getString("data"), Toast.LENGTH_SHORT).show();
                    }else {
                        Log.d("postRedeemRequest", "onResponse: Something went wrong");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("postRedeemRequest", "onResponse: error ResPonse:  " + error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(RedeemViewActivity.this));
                return header;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void getVoucherViewDetails() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, REDEEM_URL + "/" + voucherId,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        Log.d("getVoucherViewDetails", "onResponse: response Sucessfull: " + response.getString("data"));
                        binding.frameLayout.setVisibility(View.GONE);
                        //                      try catch
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

//                            for (int i=0; i<jsonArray.length();i++){
                            JSONObject jsonObject = (JSONObject) jsonArray.get(0);

                            String voucherName = jsonObject.getString("name");
                            String voucherImg = jsonObject.getString("image");
                            String voucherCoins = jsonObject.getString("coins");
                            String voucherAmt = jsonObject.getString("amount");
                            String voucherHint = jsonObject.getString("hint");
                             voucherInputType = jsonObject.getString("input_type");
                            String voucherStatus = jsonObject.getString("status");
                            String voucherSymbol = jsonObject.getString("symbol");



                            binding.voucherName2.setText(voucherName);
                            binding.voucherCoins2.setText(voucherCoins);
                            binding.voucherAmt2.setText(voucherSymbol + voucherAmt);
                            binding.nameInputLayout.setHint(voucherHint);

                            String vouImg = Constants.REDEEM_IMG_URL + voucherImg;
                            Log.d("voucherimg", "onResponse: voucher: "+ vouImg);

                            Picasso.get()
                                    .load(vouImg)
                                    .placeholder(R.drawable.placeholder)
                                    .into(binding.voucherImg2);

//                          Input type
                            if (voucherInputType.equals(Constants.INPUT_TYPE_TEXT)) {
                                binding.nameInputLayout.getEditText().setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                            } else if (voucherInputType.equals(INPUT_TYPE_EMAIL)) {
                                binding.nameInputLayout.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                            } else if (voucherInputType.equals(INPUT_TYPE_PHONE)) {
                                binding.nameInputLayout.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
                            } else {
                                binding.nameInputLayout.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
                            }


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                        Log.d("getVoucherViewDetails", "onResponse: response Failed: " + response.getString("data"));
                        binding.frameLayout.setVisibility(View.VISIBLE);
                        binding.progressBar3.setVisibility(View.GONE);
                        binding.errorImg.setVisibility(View.VISIBLE);
                        binding.message.setVisibility(View.VISIBLE);

                        binding.message.setText(response.getString("data"));
                    } else {
                        Log.d("getVoucherViewDetails", "onResponse: Something went wrong");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getVoucherViewDetails", "onResponse: error ResPonse:  " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(RedeemViewActivity.this));
                return header;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
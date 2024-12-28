package com.reward.quizisty.Activities;


import static com.reward.quizisty.Utils.Constants.BEARER;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.reward.quizisty.Adapters.RedeemHisAdapter;
import com.reward.quizisty.Modals.RedeemHistoryModal;
import com.reward.quizisty.Utils.Constants;
import com.reward.quizisty.Utils.ControlRoom;
import com.reward.quizisty.databinding.ActivityRedeemHistoryBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RedeemHistoryActivity extends AppCompatActivity {
    ActivityRedeemHistoryBinding binding;
    String redeemImg;
    ArrayList<RedeemHistoryModal> redeemHisList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRedeemHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.redeemHisToolbar.setNavigationOnClickListener(v -> finish());

        binding.frameLayout.setVisibility(View.VISIBLE);
        binding.progressBar3.setVisibility(View.VISIBLE);
        binding.errorImg.setVisibility(View.GONE);
        binding.message.setVisibility(View.GONE);

        getRedeemHistoryList();

        binding.redeemHisSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                redeemHisList.clear();
                getRedeemHistoryList();

            }
        });
    }

    private void getRedeemHistoryList() {
        redeemHisList = new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.REDEEM_HISTORY_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {
                                binding.frameLayout.setVisibility(View.GONE);


                                Log.d("getRedeemHistoryList", "onResponse: response Sucess: " + response.getString("data"));
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {


                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                imgList from imageString
                                    Log.d("getRedeemHistoryList", "onResponse: id " + jsonObject.getString("redeem_id"));
//                                    getRedeemHisImage(jsonObject.getString("redeem_id"));
//                                    String imageStr = redeemImg;

                                    String name = jsonObject.getString("redeem_name");
                                    String img = jsonObject.getString("image");
                                    String price = jsonObject.getString("amount");
                                    String symbol = jsonObject.getString("symbol");
                                    String coin = jsonObject.getString("used_coins");
                                    String givenDetail = jsonObject.getString("user_details");
                                    int status = jsonObject.getInt("status");
                                    String redeemCode = jsonObject.getString("redeem_code");

                                    String requestDateStr, reqMainDate, requestTimeStr, reqMainTime, respDateStr, respMainDate,respTimeStr,respMainTime;

                                    requestDateStr = jsonObject.getString("request_date");
                                    requestTimeStr = jsonObject.getString("request_time");
                                    if (!requestDateStr.equals("") && !requestTimeStr.equals("")){
//                                request date

                                        Date reqDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                                .parse(requestDateStr);
                                        reqMainDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(reqDate);

//                                  request time
                                        Date reqTime = new SimpleDateFormat("hh:mm:ssa", Locale.getDefault())
                                                .parse(requestTimeStr);
                                        reqMainTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(reqTime);

                                    }else {
                                        reqMainDate= "";
                                        reqMainTime= "";
                                    }


                                    respDateStr = jsonObject.getString("response_date");
                                    respTimeStr = jsonObject.getString("response_time");
                                    if (!respDateStr.equals("null") && !respTimeStr.equals("null")) {
                                        Log.d("getRedeemHistoryList", "onResponse: respdate time: "+ respDateStr+respTimeStr);
//                                  response date
                                        Date respDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                                .parse(respDateStr);
                                        respMainDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(respDate);

//                                    response time
                                        Date respTime = new SimpleDateFormat("hh:mm:ssa", Locale.getDefault())
                                                .parse(respTimeStr);
                                        respMainTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(respTime);

                                    }else {
                                        respMainDate = "Not responded yet";
                                        respMainTime = "";
                                    }





                                    Log.d("getRedeemHistoryList", "onResponse: date format " + reqMainTime + " time ");


                                    RedeemHistoryModal redeemHistoryModal = new RedeemHistoryModal(
                                            name, symbol+price, coin, reqMainDate + " " + reqMainTime,
                                            respMainDate + " " + respMainTime, img, status
                                    );
                                    redeemHistoryModal.setRedHisGivenDetail(givenDetail);
                                    redeemHistoryModal.setRedeemCode(redeemCode);
                                    redeemHisList.add(redeemHistoryModal);
                                }

//                        checking Gifts list empty.
                                if (redeemHisList.size() == 0) {
                                    binding.redeemHisRv.setVisibility(View.GONE);
                                    binding.emptyTxtGiftRedeems.setVisibility(View.VISIBLE);
                                } else {
                                    binding.redeemHisRv.setVisibility(View.VISIBLE);
                                    binding.emptyTxtGiftRedeems.setVisibility(View.GONE);


                                    binding.redeemHisRv.setAdapter(new RedeemHisAdapter(redeemHisList, RedeemHistoryActivity.this));
                                    binding.redeemHisRv.setLayoutManager(new LinearLayoutManager(RedeemHistoryActivity.this));
                                    binding.redeemHisRv.setNestedScrollingEnabled(false);

                                    binding.redeemHisSwipeRefreshLayout.setRefreshing(false);
                                }





                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("getRedeemHistoryList", "onResponse: response Failed: " + response.getString("data"));
                                binding.redeemHisSwipeRefreshLayout.setRefreshing(false);

                                String responseMsg = response.getString("data");

                                if (responseMsg.equals("No data found!..")){
                                    binding.errorImg.setVisibility(View.GONE);
                                    binding.message.setText("No Gift Redeems Available.");
                                    binding.message.setAlpha(0.6f);
                                }else {
                                    binding.errorImg.setVisibility(View.VISIBLE);
                                    binding.message.setText(responseMsg);
                                }
                                binding.frameLayout.setVisibility(View.VISIBLE);
                                binding.progressBar3.setVisibility(View.GONE);

                                binding.message.setVisibility(View.VISIBLE);

//                                binding.frameLayout.setVisibility(View.VISIBLE);
//                                binding.progressBar3.setVisibility(View.GONE);
//                                binding.errorImg.setVisibility(View.VISIBLE);
//                                binding.message.setVisibility(View.VISIBLE);
//                                binding.message.setText(response.getString("data"));
                            } else {
                                Log.d("getRedeemHistoryList", "onResponse: something went wrong");
                                binding.redeemHisSwipeRefreshLayout.setRefreshing(false);

                            }
                        } catch (JSONException | ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getRedeemHistoryList", "onResponse: error response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(RedeemHistoryActivity.this));
                return header;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    public static boolean canScrollVerticallyAnyFurther(View view, boolean downwardScroll){
        return view.canScrollVertically(downwardScroll ? +1 : -1);
    }
}
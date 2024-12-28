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
import com.reward.quizisty.Adapters.VoucherHistoryAdapter;
import com.reward.quizisty.Modals.BiddingHistoryModal;
import com.reward.quizisty.Utils.Constants;
import com.reward.quizisty.Utils.ControlRoom;
import com.reward.quizisty.databinding.ActivityVoucherHistoryBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class VoucherHistoryActivity extends AppCompatActivity {
    ActivityVoucherHistoryBinding binding;
    ArrayList<BiddingHistoryModal> vouMainBidHisList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVoucherHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.frameLayout.setVisibility(View.VISIBLE);
        binding.progressBar3.setVisibility(View.VISIBLE);
        binding.errorImg.setVisibility(View.GONE);
        binding.message.setVisibility(View.GONE);

        getVoucherBiddingHistoryList();

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getVoucherBiddingHistoryList();
            }
        });
    }

    private void getVoucherBiddingHistoryList() {
        vouMainBidHisList = new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.VOUCHER_BIDDING_HISTORY_URL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                binding.swipeRefresh.setRefreshing(false);
                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {

                        binding.frameLayout.setVisibility(View.GONE);

                        Log.d("getVoucherBiddingHistoryList", "onResponse: response Sucess: " + response.getString("data"));
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            BiddingHistoryModal bidHisModal = new BiddingHistoryModal();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                imgList from imageString
                            String imageStr = jsonObject.getString("images");
                            ArrayList<String> imgList = new ArrayList<>(Arrays.asList(imageStr.split(",")));

                            String name = jsonObject.getString("name");
//                                date and time 2023-07-11 13:29:41

                            String dateTime = jsonObject.getString("bidding_date_time");
                            Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
                                    .parse(dateTime);
                            String mainDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date);
                            String mainTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date);
                            Log.d("getBiddinghistoryList", "onResponse: date format " + mainDate + " time " + mainTime);

                            String bidCoin = jsonObject.getString("bidi_coin");
                            String bidDiamond = jsonObject.getString("bid_amt"); // bid_amt is Diamond //

//                                    winner decision....
                            int iswinner = jsonObject.getInt("is_winner");
                            int winnDiamond = jsonObject.getInt("winning_daimond");
                            String winningStatus;
                            String voucherCode = jsonObject.getString("winning_code");
                            bidHisModal.setVoucherCode(voucherCode);
                            if (iswinner == 0 && winnDiamond > 0) {
                                winningStatus = "You have won " + winnDiamond + " Diamonds.";
                                bidHisModal.setBidWinningStatus(winningStatus);
                                bidHisModal.setWinner(0);
                            } else if (iswinner != 0 && winnDiamond == 0) {
                                winningStatus = "You are Winner of this Shopping.";
                                bidHisModal.setBidWinningStatus(winningStatus);
                                bidHisModal.setWinner(1);

                            } else {
                                winningStatus = "Winner is not announced yet.";
                                bidHisModal.setWinner(2);
                                bidHisModal.setBidWinningStatus(winningStatus);
                            }

                            bidHisModal.setBidProductImage(imgList.get(0));
                            bidHisModal.setBidProductName(name);
                            bidHisModal.setBidCoin(bidCoin);
                            bidHisModal.setBidDiamond(bidDiamond);
                            bidHisModal.setBidDate(mainDate);
                            bidHisModal.setBidTime(mainTime);
                            bidHisModal.setWinningDiamond(winnDiamond + "");

                            vouMainBidHisList.add(bidHisModal);
                            Log.d("pppp", "onResponse: redeem history list size: " + vouMainBidHisList.size());

                        }
                        binding.vouMainBidHisRv.setAdapter(new VoucherHistoryAdapter(
                                vouMainBidHisList, VoucherHistoryActivity.this));
                        binding.vouMainBidHisRv.setLayoutManager(new LinearLayoutManager(VoucherHistoryActivity.this));
                        binding.vouMainBidHisRv.setNestedScrollingEnabled(false);

                    } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                        binding.swipeRefresh.setRefreshing(false);
                        Log.d("getVoucherBiddingHistoryList", "onResponse: response Failed: " + response.getString("data"));
                        binding.frameLayout.setVisibility(View.VISIBLE);
                        binding.progressBar3.setVisibility(View.GONE);
                        binding.errorImg.setVisibility(View.INVISIBLE);
                        binding.message.setVisibility(View.VISIBLE);
                        binding.message.setText(response.getString("data"));
                    } else {
                        Log.d("getVoucherBiddingHistoryList", "onResponse: something went wrong");

                    }
                } catch (JSONException | ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getVoucherBiddingHistoryList", "onResponse: error response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(VoucherHistoryActivity.this));
                return header;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
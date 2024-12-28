package com.reward.quizisty.Fragments;

import static com.reward.quizisty.Utils.Constants.AUTHORISATION;
import static com.reward.quizisty.Utils.Constants.BEARER;
import static com.reward.quizisty.Utils.Constants.CONTENT_TYPE;
import static com.reward.quizisty.Utils.Constants.CONTENT_TYPE_VALUE;
import static com.reward.quizisty.Utils.Constants.MILESTONES_API;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.reward.quizisty.Modals.MileStonesModal;
import com.reward.quizisty.Utils.ControlRoom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MileStoneRepository {
    MutableLiveData<ArrayList<MileStonesModal>> mileStonesModalLiveData = new MutableLiveData<>();

    public LiveData<ArrayList<MileStonesModal>> getMileStoneModals() {
        return mileStonesModalLiveData;
    }

    public void fetchMileStoneData(Context context) {

        ArrayList<MileStonesModal> mileStonesModalList = new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, MILESTONES_API,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getBoolean("status") && response.getInt("response_code") == 200) {
                        Log.d("fetchMileStoneData", "onResponse: response Sucessfull: " + response.getString("data"));
                        //                      try catch

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                                int id = jsonObject.getInt("id");
                                String refers_no = jsonObject.getString("refers_no");
                                int rewardType = jsonObject.getInt("reward_type");
                                String prize = jsonObject.getString("prize");  // redeem
                                String image = jsonObject.getString("image");
                                int status = jsonObject.getInt("status");
                                String reject_reason="";
                                if (status == 4) {
//                                  claim rejected..get reject reason.
                                    reject_reason = jsonObject.getString("reject_reason");
                                }

                                MileStonesModal mileStonesModal =
                                        new MileStonesModal(
                                                id,
                                                rewardType,
                                                status,
                                                prize,
                                                refers_no,
                                                image
                                        );
                                if (!reject_reason.isEmpty()) {
//                                  claim rejected..get reject reason.
                                    mileStonesModal.setRejectReason(reject_reason);
                                }
                                mileStonesModalList.add(mileStonesModal);
                            }
                            mileStonesModalLiveData.setValue(mileStonesModalList);


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    } /*else if (!response.getBoolean("status") && response.getInt("response_code") == 201) {
                        Log.d("fetchMileStoneData", "onResponse: response Failed: " + response.getString("data"));
                        String responseMsg = response.getString("data");

                        if (responseMsg.equals("No Data Found")) {
                            binding.errorImg.setVisibility(View.GONE);
                            binding.message.setText("No Gifts Available");
                            binding.message.setAlpha(0.6f);
                        } else {
                            binding.errorImg.setVisibility(View.VISIBLE);
                            binding.message.setText(responseMsg);
                        }
                        binding.frameLayout.setVisibility(View.VISIBLE);
                        binding.progressBar3.setVisibility(View.GONE);

                        binding.message.setVisibility(View.VISIBLE);
                        binding.redeemSwipeRefresh.setRefreshing(false);

                    } */ else {
                        Log.d("fetchMileStoneData", "onResponse: Something went wrong ; " + response.getString("data"));
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("fetchMileStoneData", "onResponse: error ResPonse:  " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(context));
                return header;
            }
        };
        Volley.newRequestQueue(context).add(jsonObjectRequest);

    }
}

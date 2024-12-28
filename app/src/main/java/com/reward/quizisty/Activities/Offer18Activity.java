package com.reward.quizisty.Activities;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.reward.quizisty.Modals.Offer18MainListModal;
import com.reward.quizisty.databinding.ActivityOffer18Binding;

import java.util.ArrayList;

public class Offer18Activity extends AppCompatActivity {
    ActivityOffer18Binding binding;
    ArrayList<Offer18MainListModal> trakierMainList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOffer18Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fetchAllCampaigns();


    }

    private void fetchAllCampaigns() {/*
        binding.trakierProgress.setVisibility(View.VISIBLE);
        trakierMainList = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, TRAKIER_CAMPAIGN_API,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getBoolean("success")){

                        binding.trakierProgress.setVisibility(View.GONE);

                        JSONObject data = response.getJSONObject("data");


                        JSONArray campaigns = data.getJSONArray("campaigns");

                        for (int i = 0; i< campaigns.length(); i++){
                            int totalCoins = 0;
                            JSONObject campaignData = campaigns.getJSONObject(i);
                            String campaignId = campaignData.getString("offerid");
                            try {


                                String campaignTitle = campaignData.getString("title");
                                String campaignDesc = campaignData.getString("description");
                                String campaignKpi = campaignData.getString("kpi");
                                String campaignIcon = campaignData.getString("thumbnail");



                                String campaignPosterImg = campaignData.getJSONArray("creatives")
                                        .getJSONObject(0)
                                        .getString("full_url");



                                JSONArray goals = campaignData.getJSONArray("goals");

                                for (int j= 0; j< goals.length(); j++){
                                    JSONObject singleGoal = goals.getJSONObject(j);

                                    int payout = singleGoal.getJSONArray("payouts").getJSONObject(0).getInt("payout");

                                    totalCoins = totalCoins + payout;

                                }



                                Offer18MainListModal offer18MainModal = new Offer18MainListModal(
                                        campaignPosterImg,campaignIcon,campaignTitle,campaignDesc,
                                        String.valueOf(totalCoins),""
                                );
                                offer18MainModal.setAdId(campaignId);

                                trakierMainList.add(offer18MainModal);
                            } catch (JSONException e) {

                                Log.d("fetchAllCampaigns", "onResponse: Something unexpected at Campaign Id: "+campaignId+" Message: "+ e.getMessage());

                            }

                        }

                        if (trakierMainList.size() == 0){
                            binding.trakierMainListRv.setVisibility(View.GONE);
                            binding.emptyCampaignTxt.setVisibility(View.VISIBLE);
                        }else {
                            binding.trakierMainListRv.setVisibility(View.VISIBLE);
                            binding.emptyCampaignTxt.setVisibility(View.GONE);

                            binding.trakierMainListRv.setLayoutManager(new LinearLayoutManager(Offer18Activity.this));
                            binding.trakierMainListRv.setAdapter(new Offer18MainListAdapter(trakierMainList,Offer18Activity.this));
                        }


                    }else {
                        Log.d("fetchAllCampaigns", "onResponse : Something went wrong: "
                                + response.getString("data"));
                    }
                } catch (JSONException e) {
                    Log.d("fetchAllCampaigns", "onResponse Failed : Json Exception: "+ e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("fetchAllCampaigns", "onResponse Failed : VolleyError: "+ error.getMessage());
            }
        });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    */}
}
package com.reward.quizisty.Activities;


import static com.reward.quizisty.Utils.Constants.AUTHORISATION;
import static com.reward.quizisty.Utils.Constants.CONTENT_TYPE;
import static com.reward.quizisty.Utils.Constants.CONTENT_TYPE_VALUE;
import static com.reward.quizisty.Utils.Constants.OFFER18_SINGLE_OFFER_API;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.res.ResourcesCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.reward.quizisty.Modals.Offer18MainListModal;
import com.reward.quizisty.Modals.Offer18TasksModal;
import com.reward.quizisty.R;
import com.reward.quizisty.Utils.ControlRoom;
import com.reward.quizisty.databinding.ActivityOffer18CampaignDetailBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Offer18CampaignDetailActivity extends AppCompatActivity {
    ActivityOffer18CampaignDetailBinding binding;
    ArrayList<Offer18TasksModal> offer18TasksModalList;
    Offer18MainListModal offer18MainModal;
    public static final String CAMPAIGN_ID = "campaignId";
    private static final String TAG = "Offer18CampaignDetailActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOffer18CampaignDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String campaignUid = intent.getStringExtra(CAMPAIGN_ID);
        getCampaignDetails(campaignUid);


    }


    private void getCampaignDetails(String campaignUid) {
        offer18TasksModalList = new ArrayList<>();

        binding.campaignDetailProgressBar.setVisibility(View.VISIBLE);
        binding.campaignDetailLayout.setVisibility(View.GONE);
        binding.emptyCampaignDetailTxt.setVisibility(View.GONE);

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("offer_id", Integer.parseInt(campaignUid));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, OFFER18_SINGLE_OFFER_API,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: response success: " + response);

                try {
                    if (response.getInt("response_code") == 200) {
                        Log.d(TAG, "onResponse: response success");

                        binding.campaignDetailProgressBar.setVisibility(View.GONE);
                        binding.campaignDetailLayout.setVisibility(View.VISIBLE);

//                        JSONObject data = response.getJSONObject("data");


//                        JSONArray campaigns = response.getJSONObject("data");

//                        for (int i = 0; i< campaigns.length(); i++){
                        int totalCoins = 0;
                        JSONObject campaignData = response.getJSONObject("data");
                        String campaignId = campaignData.getString("offerid");


                        String campaignName = campaignData.getString("name");
                        String longDescription = campaignData.getString("long_description");
                        String campaigntermsNcondition = campaignData.getString("terms_condition");

                        String campaignPrice = campaignData.getString("price");
                        String campaignIcon = campaignData.getString("logo");
                        String campaignClickUrl = campaignData.getString("click_url");
                        String helpUrl = campaignData.getString("link");


                        String campaignPosterImg = campaignData.getJSONArray("creatives")
                                .getJSONObject(0)
                                .getString("url");



                                /*JSONArray goals = campaignData.getJSONArray("goals");

                                for (int j= 0; j< goals.length(); j++){
                                    JSONObject singleGoal = goals.getJSONObject(j);

                                    int payout = singleGoal.getJSONArray("payouts").getJSONObject(0).getInt("payout");

                                    totalCoins = totalCoins + payout;

                                }*/


                        Offer18MainListModal offer18MainModal = new Offer18MainListModal(
                                campaignPosterImg, campaignIcon, campaignName, longDescription,
                                campaignPrice, ""
                        );
                        offer18MainModal.setAdId(campaignId);
                        offer18MainModal.setClickUrl(campaignClickUrl);
                        offer18MainModal.setHelpUrl(helpUrl);
                        offer18MainModal.setAdTermsNcon(campaigntermsNcondition);


                        setCampaignData(offer18MainModal, null);

//                            offer18TasksModalList.add(offer18MainModal);


//                        }

                        /*if (offer18TasksModalList.isEmpty()){
                            binding.offer18TaskRv.setVisibility(View.GONE);
                            binding.emptyCampaignTxt.setVisibility(View.VISIBLE);
                        }else {
                            binding.offer18MainListRv.setVisibility(View.VISIBLE);
                            binding.emptyCampaignTxt.setVisibility(View.GONE);

                            binding.offer18MainListRv.setLayoutManager(new LinearLayoutManager(requireContext()));
                            binding.offer18MainListRv.setAdapter(new Offer18MainListAdapter(offer18TasksModalList,requireContext()));
                        }
*/

                    } else {
                        Log.d(TAG, "onResponse : Something went wrong: "
                                + response.getString("data"));
                        binding.campaignDetailProgressBar.setVisibility(View.GONE);
                        binding.campaignDetailLayout.setVisibility(View.GONE);
                        binding.emptyCampaignDetailTxt.setVisibility(View.VISIBLE);
                        binding.emptyCampaignDetailTxt.setText("Something went wrong!! ");
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "onResponse Failed : Json Exception: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onResponse Failed : VolleyError: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, "Bearer " + ControlRoom.getInstance().getAccessToken(Offer18CampaignDetailActivity.this));
                return header;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);


        /*JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, TRAKIER_CAMPAIGN_API,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getBoolean("success")){
                        binding.campaignDetailProgressBar.setVisibility(View.GONE);
                        binding.campaignDetailLayout.setVisibility(View.VISIBLE);
                        binding.emptyCampaignDetailTxt.setVisibility(View.GONE);

                        JSONObject data = response.getJSONObject("data");

                        JSONArray campaigns = data.getJSONArray("campaigns");



                        for (int i = 0; i< campaigns.length(); i++){
                            int totalCoins = 0;
                            JSONObject campaignData = campaigns.getJSONObject(i);
                            int campaignId = campaignData.getInt("id");
                            Log.d("testTcda", "onResponse: TCDA: campaign id: "+campaignId+"campaign Uid: "+campaignUid);
                            if (campaignId == campaignUid){

                                try {


                                    String campaignTitle = campaignData.getString("title");
                                    String campaignDesc = campaignData.getString("description");
                                    String campaignKpi = campaignData.getString("kpi");
                                    String campaignIcon = campaignData.getString("thumbnail");



                                    String campaignPosterImg = campaignData.getJSONArray("creatives")
                                            .getJSONObject(0)
                                            .getString("full_url");



                                    JSONArray goals = campaignData.getJSONArray("goals");
                                    int totalTasks = goals.length();

                                    for (int j= 0; j< goals.length(); j++){
                                        JSONObject singleGoal = goals.getJSONObject(j);

                                        int payout = singleGoal.getJSONArray("payouts").getJSONObject(0).getInt("payout");
                                        totalCoins = totalCoins + payout;

                                        String taskTitle = singleGoal.getString("title");
                                        Offer18TasksModal tasksModal = new Offer18TasksModal(String.valueOf(j+1),taskTitle,false);

                                        offer18TasksModalList.add(tasksModal);
                                    }

                                     offer18MainModal = new Offer18MainListModal(
                                            campaignPosterImg,campaignIcon,campaignTitle,campaignDesc,
                                            String.valueOf(totalCoins),"");
                                    offer18MainModal.setTotalTasks(totalTasks);

                                    setCampaignData(offer18MainModal, offer18TasksModalList);



                                } catch (JSONException e) {

                                    Log.d("getCampaignDetailss", "onResponse: Something unexpected at Campaign Id: "+campaignId+" Message: "+ e.getMessage());

                                }

                            }else if (i == (campaigns.length()-1) && campaignId != campaignUid){
                                binding.campaignDetailLayout.setVisibility(View.GONE);
                                binding.campaignDetailProgressBar.setVisibility(View.GONE);
                                binding.emptyCampaignDetailTxt.setVisibility(View.VISIBLE);
                            }


                        }




                    }else {
                        Log.d("getCampaignDetailss", "onResponse : Something went wrong: "
                                + response.getString("data"));
                    }
                } catch (JSONException e) {
                    Log.d("getCampaignDetailss", "onResponse Failed : Json Exception: "+ e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getCampaignDetailss", "onResponse Failed : VolleyError: "+ error.getMessage());
            }
        });

        Volley.newRequestQueue(this).add(jsonObjectRequest);



    */
    }

    private void setCampaignData(Offer18MainListModal offer18MainModal, ArrayList<Offer18TasksModal> offer18TasksModalList) {

        binding.adTitle.setText(offer18MainModal.getAdTitle());
        binding.adDesc.setText(offer18MainModal.getAdDesc());
        binding.termsNcondition.setText(offer18MainModal.getAdTermsNcon());
        binding.adRewardCoin.setText(offer18MainModal.getAdRewardCoin());
        Picasso.get()
                .load(offer18MainModal.getAdIcon())
                .placeholder(R.drawable.placeholder)
                .into(binding.adIcon);
        Picasso.get()
                .load(offer18MainModal.getAdPosterImg())
                .placeholder(R.drawable.placeholder)
                .into(binding.adPosterImg);
        binding.tasksCount.setText("0/" + offer18MainModal.getTotalTasks());

        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(new CustomTabColorSchemeParams.Builder()
                        .setToolbarColor(
                                ResourcesCompat.getColor(getResources(),
                                        R.color.md_theme_primary, Offer18CampaignDetailActivity.this.getTheme()))
                        .build())
                .build();

        binding.adClaimBtn.setOnClickListener(v -> {
            EditText refEditTxt = binding.refCodeInputLayout.getEditText();
            assert refEditTxt != null;
            String refCode = refEditTxt.getText().toString();
            if (!refCode.isEmpty() && refCode.length() <= 10) {
                customTabsIntent.
                        launchUrl(Offer18CampaignDetailActivity.this,
                                Uri.parse(offer18MainModal.getClickUrl() + refCode));
                binding.refCodeInputLayout.clearFocus();

            }else if (refCode.length() > 10){
                binding.refCodeInputLayout.setError("Refer Code is not valid.");

            }else {
                customTabsIntent.
                        launchUrl(Offer18CampaignDetailActivity.this,
                                Uri.parse(offer18MainModal.getClickUrl()));
            }

        });

        if (Objects.equals(offer18MainModal.getHelpUrl(), "Not Available")) {
            binding.helpButton.setVisibility(View.GONE);
        } else {
            binding.helpButton.setVisibility(View.VISIBLE);
            binding.helpButton.setOnClickListener(v -> {
                Intent helpIntent = new Intent(Intent.ACTION_VIEW);
                helpIntent.setData(Uri.parse(offer18MainModal.getHelpUrl()));
                startActivity(helpIntent);
            });
        }


//        binding.offer18TaskRv.setAdapter(new Offer18TasksAdapter(offer18TasksModalList,this));
//        binding.offer18TaskRv.setLayoutManager(new LinearLayoutManager(this));

    }
}
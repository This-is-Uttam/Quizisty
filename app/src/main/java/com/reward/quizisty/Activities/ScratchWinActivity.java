package com.reward.quizisty.Activities;


import static com.reward.quizisty.Adapters.AdNetAdapter.appLovinAdUnitIdReward;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anupkumarpanwar.scratchview.ScratchView;
import com.reward.quizisty.R;
import com.reward.quizisty.Utils.Constants;
import com.reward.quizisty.Utils.ControlRoom;
import com.reward.quizisty.databinding.ActivityScratchWinBinding;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.UnityAds;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ScratchWinActivity extends AppCompatActivity {
    ActivityScratchWinBinding binding;
    MaxRewardedAd myRewardedAd;
    Dialog dialog;
    ArrayList<Integer> scratchCoinList;
    int cardCoin;
    int randomInt;
    static float scratchPercent;
    ScratchView updatedScratch;
    private String unityAdUnitIdReward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScratchWinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.scratchToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        getScratchDetailsFromServer();

        dialog = new Dialog(this, R.style.CustomDialog);

        View view = LayoutInflater.from(this).inflate(R.layout.scratch_dialog, null);
        TextView dialogScratchCoin = view.findViewById(R.id.dialogScratchCoins);
        dialogScratchCoin.setText("You have won " + cardCoin + " Coins");

        dialog.setContentView(view);

        binding.scratchView.setRevealListener(new ScratchView.IRevealListener() {
            @Override
            public void onRevealed(ScratchView scratchView) {
                Log.d("ScratchWinActivity", "onRevealPercentChangedListener: revealed");
                binding.scratchView.reveal();
                updatedScratch =scratchView;
                Log.d("ScratchWinActivity", "onRevealed: updatedScratch: "+updatedScratch.isRevealed());

//                show Applovin Rewarded Ad

                /* myRewardedAd.show(ScratchWinActivity.this, new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        Log.d("ScratchActivityAd", "onUserEarnedReward: rewardItem"+ rewardItem);
                    }
                });*/
                if (myRewardedAd.isReady()){

                    myRewardedAd.showAd();
                }

            }

            @Override
            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {
                Log.d("ScratchWinActivity", "onRevealPercentChangedListener: percent: " + percent);

                scratchPercent = percent;
//                Tap anywhere to reveal
                binding.scratchCnstrtLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.scratchView.reveal();
                    }
                });



            }
        });



//        AppLovin Rewarded Ad
        loadRewardedAd();




    }

    private void loadUnityRewardedAd() {

        UnityAds.load(unityAdUnitIdReward, new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {
                Log.d("UnityAd", "onUnityAdsAdLoaded: Rewarded Ad loaded successfully");
            }

            @Override
            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                Log.d("UnityAd", "onUnityAdsAdLoaded:Rewarded Ad loaded Failed "+ message);
            }
        });
    }


    private void loadRewardedAd() {
        myRewardedAd = MaxRewardedAd.getInstance(appLovinAdUnitIdReward, this);
        myRewardedAd.setListener(new MaxRewardedAdListener() {
            @Override
            public void onUserRewarded(MaxAd maxAd, MaxReward maxReward) {

            }

            @Override
            public void onRewardedVideoStarted(MaxAd maxAd) {
                loadRewardedAd();

            }

            @Override
            public void onRewardedVideoCompleted(MaxAd maxAd) {
                sendScratchCoinsToServer();

            }

            @Override
            public void onAdLoaded(MaxAd maxAd) {

            }

            @Override
            public void onAdDisplayed(MaxAd maxAd) {

            }

            @Override
            public void onAdHidden(MaxAd maxAd) {

            }

            @Override
            public void onAdClicked(MaxAd maxAd) {

            }

            @Override
            public void onAdLoadFailed(String s, MaxError maxError) {

            }

            @Override
            public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {

            }
        });
        myRewardedAd.loadAd();
    }

    private void sendScratchCoinsToServer() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("card", randomInt + 1);    //"randomInt+1" is position  of coin in scratchCardList, bcoz it starts from zero.
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SCRATCH_CARD_POST_API,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if (response.getBoolean("status") && response.getInt("code") == 200) {

                        Log.d("sendScratchCoinsToServer", "onResponse: respose success " +
                                response.getString("data"));
                        Toast.makeText(ScratchWinActivity.this, "Coins added successfully!", Toast.LENGTH_SHORT).show();
//                                JSONObject data = response.getJSONObject("data");


                    } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                        Log.d("sendScratchCoinsToServer", "onResponse: respose Failed " + response.getString("data"));

                    } else {
                        Log.d("sendScratchCoinsToServer", "onResponse: something went wrong ");

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("sendScratchCoinsToServer", "onResponse: error response " + error.getMessage());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, Constants.BEARER + ControlRoom.getInstance().getAccessToken(ScratchWinActivity.this));
                return header;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

    private void getScratchDetailsFromServer() {
        scratchCoinList = new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.SCRATCH_CARD_GET_API, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getBoolean("status") && response.getInt("code") == 200) {

                                Log.d("getScratchDetailsFromServer", "onResponse: respose success " +
                                        response.getString("data"));
                                JSONObject data = response.getJSONObject("data");
                                JSONObject scratchCard = data.getJSONObject("scratchCard");
//                                coins
                                int c1 = scratchCard.getInt("c1");
                                int c2 = scratchCard.getInt("c2");
                                int c3 = scratchCard.getInt("c3");
                                int c4 = scratchCard.getInt("c4");
                                int c5 = scratchCard.getInt("c5");
                                int c6 = scratchCard.getInt("c6");
                                int c7 = scratchCard.getInt("c7");
                                int c8 = scratchCard.getInt("c8");
                                int c9 = scratchCard.getInt("c9");
                                int c10 = scratchCard.getInt("c10");

                                scratchCoinList.add(c1);
                                scratchCoinList.add(c2);
                                scratchCoinList.add(c3);
                                scratchCoinList.add(c4);
                                scratchCoinList.add(c5);
                                scratchCoinList.add(c6);
                                scratchCoinList.add(c7);
                                scratchCoinList.add(c8);
                                scratchCoinList.add(c9);
                                scratchCoinList.add(c10);

                                Log.d("list", "onResponse: position 10: " + scratchCoinList.get(scratchCoinList.size() - 1));
                                Log.d("list", "onResponse: position 10: " + (scratchCoinList.size() - 1));

                                Random random = new Random();
                                randomInt = random.nextInt(scratchCoinList.size() - 1);
                                cardCoin = scratchCoinList.get(randomInt);
                                Log.d("luck", "onResponse: cardCoin: " + cardCoin);
                                Log.d("luck", "onResponse: randomInt: " + randomInt);
                                for (int coin = 0; coin < scratchCoinList.size(); coin++) {
                                    if (cardCoin == 0) {
                                        binding.scratchCoins.setText("Better Luck Next Time");
                                        binding.scratchCoins.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                                    } else {
                                        binding.scratchCoins.setText(cardCoin + "");
                                    }
                                }


                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("getScratchDetailsFromServer", "onResponse: respose Failed " + response.getString("data"));

                            } else {
                                Log.d("getScratchDetailsFromServer", "onResponse: something went wrong ");

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getScratchDetailsFromServer", "onResponse: error response " + error.getMessage());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, Constants.BEARER + ControlRoom.getInstance().getAccessToken(ScratchWinActivity.this));
                return header;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        Log.d("ScratchWinActivity", "onBackPressed: scratchPercent: "+ scratchPercent);
// if scratchPercent > 0.5 then the scratch view revealed but it"s value doesn't set to 1.0 afterwards.
        if (scratchPercent >(float) 0f && scratchPercent < (float) 0.50f) {
            binding.scratchView.reveal();
        } else if (binding.scratchView.isRevealed()){
            Log.d("ScratchWinActivity", "onBackPressed: isRevealed: "+ binding.scratchView.isRevealed());
            goBack();
        } else {
            super.onBackPressed();
        }
    }

    void goBack(){
        Intent intent = new Intent(ScratchWinActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);

    }
}
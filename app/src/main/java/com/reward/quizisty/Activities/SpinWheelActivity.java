package com.reward.quizisty.Activities;

import static com.reward.quizisty.Utils.Constants.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
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
import com.reward.quizisty.databinding.ActivitySpinWheelBinding;
import com.applovin.mediation.ads.MaxRewardedAd;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class SpinWheelActivity extends AppCompatActivity {
    ActivitySpinWheelBinding binding;
    public static final int TO_DEGREES = 4320;
    public static final int BASE_DEGREES = 36;
    int spinPosition;
    static String p1, p2, p3, p4, p5, p6, p7, p8, p9, p10 = "";
    boolean isSpinAvailable;

    MaxRewardedAd myRewardedAd;
    boolean isWinner = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpinWheelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.spinToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        binding.youWon.setVisibility(View.GONE);

        binding.spinBtn.setClickable(false);

        getSpinnerData();


        binding.spinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                disable spin btn
                binding.spinBtn.setClickable(false);
                binding.spinBtn.setBackgroundTintList(getApplicationContext()
                        .getColorStateList(R.color.buttonDisableColor));

//                availableSpin-1;
                Animation spinAnim = binding.wheelCard.getAnimation();
                if (spinAnim != null && spinAnim.hasStarted() && !spinAnim.hasEnded()) {

                    Toast.makeText(SpinWheelActivity.this, "Can't spin during Spinning", Toast.LENGTH_SHORT).show();
                } else {

                    int degree = generateRandomDegrees();
                    Animation rotate = new RotateAnimation(0, TO_DEGREES + degree,
                            (float) binding.wheelCard.getWidth() / 2, (float) binding.wheelCard.getHeight() / 2);
                    rotate.setDuration(9000);
                    rotate.setFillAfter(true);
                    rotate.setInterpolator(new DecelerateInterpolator());
                    binding.wheelConstrnt.startAnimation(rotate);

//                    Animation Callback......
                    Animation animation = binding.wheelConstrnt.getAnimation();

                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            binding.youWon.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                             SharedPreferences sp = getSharedPreferences("SPINNER_DATA", MODE_PRIVATE);
                            String p1  = sp.getString("p1","0");
                            String p2  = sp.getString("p2","0");
                            String p3  = sp.getString("p3","0");
                            String p4  = sp.getString("p4","0");
                            String p5  = sp.getString("p5","0");
                            String p6  = sp.getString("p6","0");
                            String p7  = sp.getString("p7","0");
                            String p8  = sp.getString("p8","0");
                            String p9  = sp.getString("p9","0");
                            String p10  = sp.getString("p10","0");
//                            Log.d("booom", "onAnimationEnd: Spin/wheel points : "+test +" points2: "+ test2);

                            binding.youWon.setScaleX(0f);
                            binding.youWon.setScaleY(0f);

//                            binding.spinWinAnim.l

//                          Getting Positions of spin wheel.....
                            switch (spinPosition) {
                                case 1:
                                    if (p1.equals("0")) {
                                        binding.youWonCoins.setVisibility(View.GONE);
                                        binding.youWonTxt.setText("Better Luck Next Time");

                                    } else {

                                        binding.youWonCoins.setText(p1);
                                        isWinner = true;

                                    }

                                    break;
                                case 2:
                                    if (p2.equals("0")) {
                                        binding.youWonCoins.setVisibility(View.GONE);
                                        binding.youWonTxt.setText("Better Luck Next Time");
                                    } else {

                                        binding.youWonCoins.setText(p2);
                                        isWinner = true;
                                    }

                                    break;
                                case 3:
                                    if (p3.equals("0")) {
                                        binding.youWonCoins.setVisibility(View.GONE);
                                        binding.youWonTxt.setText("Better Luck Next Time");
                                    } else {

                                        binding.youWonCoins.setText(p3);
                                        isWinner = true;
                                    }
                                    break;
                                case 4:
                                    if (p4.equals("0")) {
                                        binding.youWonCoins.setVisibility(View.GONE);
                                        binding.youWonTxt.setText("Better Luck Next Time");
                                    } else {

                                        binding.youWonCoins.setText(p4);
                                        isWinner = true;
                                    }
                                    break;
                                case 5:
                                    if (p5.equals("0")) {
                                        binding.youWonCoins.setVisibility(View.GONE);
                                        binding.youWonTxt.setText("Better Luck Next Time");
                                    } else {

                                        binding.youWonCoins.setText(p5);
                                        isWinner = true;
                                    }
                                    break;
                                case 6:
                                    if (p6.equals("0")) {
                                        binding.youWonCoins.setVisibility(View.GONE);
                                        binding.youWonTxt.setText("Better Luck Next Time");
                                    } else {

                                        binding.youWonCoins.setText(p6);
                                        isWinner = true;
                                    }
                                    break;
                                case 7:
                                    if (p7.equals("0")) {
                                        binding.youWonCoins.setVisibility(View.GONE);
                                        binding.youWonTxt.setText("Better Luck Next Time");
                                    } else {

                                        binding.youWonCoins.setText(p7);
                                        isWinner = true;
                                    }
                                    break;
                                case 8:
                                    if (p8.equals("0")) {
                                        binding.youWonCoins.setVisibility(View.GONE);
                                        binding.youWonTxt.setText("Better Luck Next Time");
                                    } else {

                                        binding.youWonCoins.setText(p8);
                                        isWinner = true;
                                    }
                                    break;
                                case 9:
                                    if (p9.equals("0")) {
                                        binding.youWonCoins.setVisibility(View.GONE);
                                        binding.youWonTxt.setText("Better Luck Next Time");
                                    } else {

                                        binding.youWonCoins.setText(p9);
                                        isWinner = true;
                                    }
                                    break;
                                case 10:
                                    if (p10.equals("0")) {
                                        binding.youWonCoins.setVisibility(View.GONE);
                                        binding.youWonTxt.setText("Better Luck Next Time");
                                    } else {

                                        binding.youWonCoins.setText(p10);
                                        isWinner = true;
                                    }
                                    break;


                            }



                            binding.spinBtn.setClickable(false);
                            binding.spinBtn.setBackgroundTintList(getApplicationContext()
                                    .getColorStateList(R.color.buttonDisableColor));


                            claimSpinReward(isWinner);

//                            if (myRewardedAd.isReady()){
//                                myRewardedAd.showAd();
//                            }else{
//                                UnityAds.show(SpinWheelActivity.this, unityAdUnitIdReward, new UnityAdsShowOptions(),
//                                        new IUnityAdsShowListener() {
//                                    @Override
//                                    public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
//                                        Log.d("UnityAd", "onUnityAdsShowFailure: Spin Activity failed: "+ message);
//                                    }
//
//                                    @Override
//                                    public void onUnityAdsShowStart(String placementId) {
//                                        Log.d("UnityAd", "onUnityAdsShowStart: Spin Activity");
//                                    }
//
//                                    @Override
//                                    public void onUnityAdsShowClick(String placementId) {
//                                        Log.d("UnityAd", "onUnityAdsShowClick: Spin Activity");
//                                    }
//
//                                    @Override
//                                    public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
//                                        Log.d("UnityAd", "onUnityAdsShowComplete: Spin Activity");
//
//                                        sendSpinPositionToServer(spinPosition);
//
//                                        if (isWinner){
//                                            binding.spinWinAnim.setAnimation(R.raw.spin_win_anim);
//                                            binding.spinWinAnim.playAnimation();
//                                        }
//
//
//                                        binding.youWon.setVisibility(View.VISIBLE);
////                            binding.youWonCoins.setText(binding.t1.getText().toString());
//                                        binding.youWon.animate().scaleXBy(1f).scaleYBy(1f).setDuration(1000).start();
//
//                                        // Sending position data to server....
//
//
//                                        new Handler().postDelayed(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                Intent intent = new Intent(SpinWheelActivity.this, MainActivity.class);
//                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                startActivity(intent);
//                                                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
//                                                finish();
//                                            }
//                                        }, 3000);
//                                    }
//                                });
//
//                            }
//                                Log.d("ScratchWinActivity", "onRevealed: Ad is not ready");

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            binding.youWon.setVisibility(View.GONE);
                        }
                    });
                }


            }
        });



/*//       App Lovin Rewarded ad start.........
        loadRewardedAd();
//      Unity Rewarded ad start.........
        loadUnityRewardedAd();*/
    }

    private void claimSpinReward(boolean isWinner) {
        sendSpinPositionToServer(spinPosition);

        if (isWinner){
            binding.spinWinAnim.setAnimation(R.raw.spin_win_anim);
            binding.spinWinAnim.playAnimation();
        }

        binding.youWon.setVisibility(View.VISIBLE);
//                            binding.youWonCoins.setText(binding.t1.getText().toString());
        binding.youWon.animate().scaleXBy(1f).scaleYBy(1f).setDuration(1000).start();

        // Sending position data to server....


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SpinWheelActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                finish();
            }
        }, 3000);
    }

   /* private void loadUnityRewardedAd() {

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
                sendSpinPositionToServer(spinPosition);

                if (isWinner){
                    binding.spinWinAnim.setAnimation(R.raw.spin_win_anim);
                    binding.spinWinAnim.playAnimation();
                }

                binding.youWon.setVisibility(View.VISIBLE);
//                            binding.youWonCoins.setText(binding.t1.getText().toString());
                binding.youWon.animate().scaleXBy(1f).scaleYBy(1f).setDuration(1000).start();

                // Sending position data to server....


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SpinWheelActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                        finish();
                    }
                }, 3000);

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
    }*/


    private void sendSpinPositionToServer(int spinPosition) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("position", spinPosition);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, SPINNER_POST_POSITION_API,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        Log.d("sendSpinPositionToServer", "onResponse: response Sucessfull: " + response.getString("data"));

//                        Toast.makeText(RedeemViewActivity.this, ""+response.getString("data"), Toast.LENGTH_SHORT).show();
//                        finish();
//                        CoinFragment coinFragment = new CoinFragment();
//                                coinFragment.updateCoins();
//                                coinFragment.getSpinnerDataFrag();


                    } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                        Log.d("sendSpinPositionToServer", "onResponse: response Failed: " + response.getString("data"));
//                        Toast.makeText(RedeemViewActivity.this, ""+response.getString("data"), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("sendSpinPositionToServer", "onResponse: Something went wrong");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("sendSpinPositionToServer", "onResponse: error ResPonse:  " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(SpinWheelActivity.this));
                return header;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void getSpinnerData() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.SPINNER_DATA_API, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {

//                                binding.frameLayout.setVisibility(View.GONE);
//                                binding.swipeRefreshBidHis.setRefreshing(false);

                                Log.d("getSpinnerData", "onResponse: response Sucess: " + response.getString("data"));

                                JSONObject data = response.getJSONObject("data");
                                JSONObject spinner = data.getJSONObject("spinner");

                                isSpinAvailable = data.getBoolean("attempt_limit");
                                if (!isSpinAvailable) {
                                    binding.spinBtn.setClickable(false);
                                    binding.spinBtn.setBackgroundTintList(getApplicationContext()
                                            .getColorStateList(R.color.buttonDisableColor));
                                }


                                String p1 = spinner.getString("p1");
                                String p2 = spinner.getString("p2");
                                String p3 = spinner.getString("p3");
                                String p4 = spinner.getString("p4");
                                String p5 = spinner.getString("p5");
                                String p6 = spinner.getString("p6");
                                String p7 = spinner.getString("p7");
                                String p8 = spinner.getString("p8");
                                String p9 = spinner.getString("p9");
                                String p10 = spinner.getString("p10");

                                // set values in shared preference

                                getSharedPreferences("SPINNER_DATA", MODE_PRIVATE).edit()
                                        .putString("p1", p1)
                                        .putString("p2", p2)
                                        .putString("p3", p3)
                                        .putString("p4", p4)
                                        .putString("p5", p5)
                                        .putString("p6", p6)
                                        .putString("p7", p7)
                                        .putString("p8", p8)
                                        .putString("p9", p9)
                                        .putString("p10", p10)
                                        .apply();

                                if (p1.equals("0")) {
                                    binding.t1.setText("Better Luck Next Time");
                                    binding.t1.setTextColor(getResources().getColor(android.R.color.white, getTheme()));

                                } else {
                                    binding.t1.setText(p1);
                                    binding.t1.setTextColor(getResources().getColor(R.color.orangeDark, getTheme()));
                                    Drawable drawable = getBaseContext().getResources().getDrawable(R.drawable.coins_18dp, getTheme());
                                    binding.t1.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);

                                }
                                if (p2.equals("0")) {
                                    binding.t2.setText("Better Luck \n Next Time");
                                    binding.t2.setTextColor(getResources().getColor(android.R.color.white, getTheme()));
                                } else {
                                    binding.t2.setText(p2);
                                    binding.t2.setTextColor(getResources().getColor(R.color.orangeDark, getTheme()));
                                    Drawable drawable = getBaseContext().getResources().getDrawable(R.drawable.coins_18dp, getTheme());
                                    binding.t2.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                                }
                                if (p3.equals("0")) {
                                    binding.t3.setText("Better Luck \n Next Time");
                                    binding.t3.setTextColor(getResources().getColor(android.R.color.white, getTheme()));
                                } else {
                                    binding.t3.setText(p3);
                                    binding.t3.setTextColor(getResources().getColor(R.color.orangeDark, getTheme()));
                                    Drawable drawable = getBaseContext().getResources().getDrawable(R.drawable.coins_18dp, getTheme());
                                    binding.t3.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                                }
                                if (p4.equals("0")) {
                                    binding.t4.setText("Better Luck \n Next Time");
                                    binding.t4.setTextColor(getResources().getColor(android.R.color.white, getTheme()));
                                } else {
                                    binding.t4.setText(p4);
                                    binding.t4.setTextColor(getResources().getColor(R.color.orangeDark, getTheme()));
                                    Drawable drawable = getBaseContext().getResources().getDrawable(R.drawable.coins_18dp, getTheme());
                                    binding.t4.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                                }
                                if (p5.equals("0")) {
                                    binding.t5.setText("Better Luck \n Next Time");
                                    binding.t5.setTextColor(getResources().getColor(android.R.color.white, getTheme()));
                                } else {
                                    binding.t5.setText(p5);
                                    binding.t5.setTextColor(getResources().getColor(R.color.orangeDark, getTheme()));
                                    Drawable drawable = getBaseContext().getResources().getDrawable(R.drawable.coins_18dp, getTheme());
                                    binding.t5.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                                }
                                if (p6.equals("0")) {
                                    binding.t6.setText("Better Luck \n Next Time");
                                    binding.t6.setTextColor(getResources().getColor(android.R.color.white, getTheme()));
                                } else {
                                    binding.t6.setText(p6);
                                    binding.t6.setTextColor(getResources().getColor(R.color.orangeDark, getTheme()));
                                    Drawable drawable = getBaseContext().getResources().getDrawable(R.drawable.coins_18dp, getTheme());
                                    binding.t6.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                                }
                                if (p7.equals("0")) {
                                    binding.t7.setText("Better Luck \n Next Time");
                                    binding.t7.setTextColor(getResources().getColor(android.R.color.white, getTheme()));
                                } else {
                                    binding.t7.setText(p7);
                                    binding.t7.setTextColor(getResources().getColor(R.color.orangeDark, getTheme()));
                                    Drawable drawable = getBaseContext().getResources().getDrawable(R.drawable.coins_18dp, getTheme());
                                    binding.t7.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                                }
                                if (p8.equals("0")) {
                                    binding.t8.setText("Better Luck \n Next Time");
                                    binding.t8.setTextColor(getResources().getColor(android.R.color.white, getTheme()));
                                } else {
                                    binding.t8.setText(p8);
                                    binding.t8.setTextColor(getResources().getColor(R.color.orangeDark, getTheme()));
                                    Drawable drawable = getBaseContext().getResources().getDrawable(R.drawable.coins_18dp, getTheme());
                                    binding.t8.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                                }
                                if (p9.equals("0")) {
                                    binding.t9.setText("Better Luck \n Next Time");
                                    binding.t9.setTextColor(getResources().getColor(android.R.color.white, getTheme()));
                                } else {
                                    binding.t9.setText(p9);
                                    binding.t9.setTextColor(getResources().getColor(R.color.orangeDark, getTheme()));
                                    Drawable drawable = getBaseContext().getResources().getDrawable(R.drawable.coins_18dp, getTheme());
                                    binding.t9.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                                }
                                if (p10.equals("0")) {
                                    binding.t10.setText("Better Luck \n Next Time");
                                    binding.t10.setTextColor(getResources().getColor(android.R.color.white, getTheme()));
                                } else {
                                    binding.t10.setText(p10);
                                    binding.t10.setTextColor(getResources().getColor(R.color.orangeDark, getTheme()));
                                    Drawable drawable = getBaseContext().getResources().getDrawable(R.drawable.coins_18dp, getTheme());
                                    binding.t10.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                                }
                                // Enable spin button
                                binding.spinBtn.setClickable(true);



                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("getSpinnerData", "onResponse: response Failed: " + response.getString("data"));
                               /* binding.frameLayout.setVisibility(View.VISIBLE);
                                binding.progressBar3.setVisibility(View.GONE);
                                binding.errorImg.setVisibility(View.INVISIBLE);
                                binding.message.setVisibility(View.VISIBLE);
                                binding.message.setText(response.getString("data"));*/
                            } else {
                                Log.d("getSpinnerData", "onResponse: something went wrong");

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getSpinnerData", "onResponse: error response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(SpinWheelActivity.this));
                return header;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private int generateRandomDegrees() {
        Random random = new Random();
        int r = random.nextInt(9) + 1;
        String coins = "t" + (r);
        Log.d("generateRandomDegrees", ": Coins: " + coins);

        spinPosition = r;
        return -((r - 1) * BASE_DEGREES);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    private void goBack() {
        Intent intent = new Intent(SpinWheelActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        finish();
    }

}
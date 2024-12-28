package com.reward.quizisty.Adapters;

import static com.reward.quizisty.Utils.Constants.BEARER;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.reward.quizisty.Activities.ScratchWinActivity;
import com.reward.quizisty.Activities.SpinWheelActivity;
import com.reward.quizisty.Modals.AdNetModal;
import com.reward.quizisty.R;
import com.reward.quizisty.Utils.Constants;
import com.reward.quizisty.Utils.ControlRoom;
import com.reward.quizisty.databinding.AdNetRvItemBinding;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.ayetstudios.publishersdk.AyetSdk;
import com.ayetstudios.publishersdk.interfaces.UserBalanceCallback;
import com.ayetstudios.publishersdk.messages.SdkUserBalance;
import com.makeopinion.cpxresearchlib.CPXResearch;
import com.makeopinion.cpxresearchlib.models.CPXConfiguration;
import com.makeopinion.cpxresearchlib.models.CPXConfigurationBuilder;
import com.makeopinion.cpxresearchlib.models.CPXStyleConfiguration;
import com.makeopinion.cpxresearchlib.models.SurveyPosition;
import com.pollfish.Pollfish;
import com.pollfish.builder.Params;
import com.pollfish.callback.PollfishSurveyCompletedListener;
import com.pollfish.callback.SurveyInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ai.bitlabs.sdk.BitLabs;

public class AdNetAdapter extends RecyclerView.Adapter<AdNetAdapter.Viewholder> {
    private static final String AYET_AD_SLOT_NAME = "rewardcycle_ayet";
    public static final String EXCENTIV_API_KEY = "SMnpZL6zg0TRtxioKBvq";
    private final String BitlabAppKey = "08439587-7d74-4307-950e-5c7c05687155";
    private final String PollfishApiKey = "81315273-8c17-4603-8ed3-02a71e3b3f45";
    private final String AyetAppKey = "35841309368e00c7d53115399b84d107";
    private final String CpxResearchAppId = "24169";
    private final String CpxResearchSecureHash = "ztEUobYK2jNz1HoDiY8U6nUVMIg2QUBE";
    public final static String appLovinAdUnitIdReward = "4ad1e972a743467c";
    public final static String appLovinAdUnitIdInter = "335f6351811c54d6";
    private final CPXResearch cpxResearch;
    ArrayList<AdNetModal> adNetList;
    Context context;
    BitLabs bitLabs;
    private MaxRewardedAd myRewardedAd;
    private MaxInterstitialAd myInterstitialAd;
    int retryAttempt;
    private static boolean isScratch;
    private String TAG = "AdNetAdapter";

    public AdNetAdapter(ArrayList<AdNetModal> adNetList, Context context) {
        this.adNetList = adNetList;
        this.context = context;

//        Bitlabs initialisation
        bitLabs = BitLabs.INSTANCE;
        bitLabs.init(BitlabAppKey, ControlRoom.getInstance().getId(context));

//      Pollfish initialisation
        Params params = new Params.Builder(PollfishApiKey)
                .rewardMode(true)
                .releaseMode(true)
                .userId(ControlRoom.getInstance().getId(context))
                .requestUUID(ControlRoom.getInstance().getId(context))
                .placementId("4848ffea-ed18-4b65-86e5-a2b8fe7c8aee")
                .clickId(ControlRoom.getInstance().getId(context))
                .offerwallMode(true)
                .pollfishSurveyCompletedListener(new PollfishSurveyCompletedListener() {
                    @Override
                    public void onPollfishSurveyCompleted(@NonNull SurveyInfo surveyInfo) {
                        Toast.makeText(context, "Pollfish Survey completed", Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

        Pollfish.initWith((Activity) context, params);

//        Ayet Studios initialisation
        AyetSdk.init(((Activity) context).getApplication(),
                ControlRoom.getInstance().getId(context),
                new UserBalanceCallback() {
                    @Override
                    public void userBalanceChanged(SdkUserBalance sdkUserBalance) {
                    }

                    @Override
                    public void userBalanceInitialized(SdkUserBalance sdkUserBalance) {
                    }

                    @Override
                    public void initializationFailed() {
                        Log.d("AyetSdk", "initializationFailed - please check APP KEY & internet connectivity");
                    }
                }, AyetAppKey);

//        CPX Research

//        CPX Researchs Offerwall
        CPXStyleConfiguration style = new CPXStyleConfiguration(SurveyPosition.SideLeftNormal,
                "Click to open CPX Research Offerwall",
                14,
                "#ffffff",
                "#3295AC",
                true);

        CPXConfiguration config = new CPXConfigurationBuilder(
                CpxResearchAppId,
                ControlRoom.getInstance().getId(context),
                CpxResearchSecureHash,
                style).build();
//        CPXHash.Companion.md5()


        cpxResearch = CPXResearch.Companion.init(config);

//      AppLovin Rewarded Ad load
        loadRewardedAd();
//        Applovin Interstitial Ad load
        loadInterstitialAd();
    }

    @NonNull
    @Override
    public AdNetAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ad_net_rv_item, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdNetAdapter.Viewholder holder, int position) {
        AdNetModal adNetModal = adNetList.get(position);

        holder.binding.adNetImg.setImageResource(adNetModal.getAdNetImg());
        holder.binding.adNetName.setText(adNetModal.getAdNetName());
        if (adNetModal.getId() == 4){
//            scratch card
            Log.d(TAG, "onBindViewHolder: scratch card");
            holder.binding.claimBtn.setEnabled(false);
            getScratchDetailsFromServerFrag(holder);
        }
        if (adNetModal.getId() == 5) {
//            spinner
            Log.d(TAG, "onBindViewHolder: spinner");
            holder.binding.claimBtn.setEnabled(false);
            getSpinnerDataFrag(holder);
        }


        holder.binding.claimBtn.setOnClickListener(v -> {
            switch (adNetModal.getId()) {
                case 0:
//                    Bitlabs offerwall
                    if (bitLabs != null) {
                        bitLabs.launchOfferWall(context);
                    } else {
                        Toast.makeText(context, "Something went wrong, try again!", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case 1:
//                    Pollfish Offerwall
                    Pollfish.show();
                    break;
                case 2:
//                    CPx Research
                    cpxResearch.openSurveyList((Activity) context);
//                    cpxResearch.setSurveyVisibleIfAvailable(true, (Activity) context);
                    boolean bannerCpx = cpxResearch.getShowBannerIfSurveysAreAvailable();
                    Log.d("Cpx", "onClick: bannershow: " + bannerCpx);
                    if (!bannerCpx) {
//                        Toast.makeText(context, "Survey not available, try again in few seconds later", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Wait few seconds to show CPX Banner", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
//                    Ayet Studio
                    if (AyetSdk.isInitialized()) {
                        Log.d("AyetSdk", "onCreateView: AyetSdk is initialised successfully");
                        AyetSdk.showOfferwall(((Activity) context).getApplication(), AYET_AD_SLOT_NAME);
                    } else {
                        Log.d("AyetSdk", "onCreateView: AyetSdk is Not initialised");
                        Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 4:
//                  Scratch and win
                    isScratch = true;
                    Log.d("AppLovin Interstitial", "onBindViewHolder: isscratch: " + isScratch);
                    if (myInterstitialAd.isReady())
                        myInterstitialAd.showAd();
                    else {
                        context.startActivity(new Intent(context, ScratchWinActivity.class));
                    }
                    break;
                case 5:
//                  Spin and win
                    isScratch = false;
                    if (myRewardedAd.isReady()) {
                        myRewardedAd.showAd();
                    } else if (myInterstitialAd.isReady())
                        myInterstitialAd.showAd();
                    else {
                        context.startActivity(new Intent(context, SpinWheelActivity.class));
                    }
                    break;
                case 6:
//                  Excentiv
                    Uri excentivUri = Uri.parse("https://excentiv.com/offerwall/?userid=" +
                            ControlRoom.getInstance().getId(context) + "&key=" + EXCENTIV_API_KEY);
                    Intent intent = new Intent(Intent.ACTION_VIEW, excentivUri);
                    context.startActivity(intent);
                    break;
                case 7:
//                  Ewall
                    Uri ewallUri = Uri.parse("https://ewall.biz/offerwall/m8xjoa4xzrrt3f5yric0skhylcxu2h/" +
                            ControlRoom.getInstance().getId(context));
                    Intent ewallIntent = new Intent(Intent.ACTION_VIEW, ewallUri);
                    context.startActivity(ewallIntent);
                    break;

            }
        });

    }

    @Override
    public int getItemCount() {
        return adNetList.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        AdNetRvItemBinding binding;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = AdNetRvItemBinding.bind(itemView);
        }
    }


    public void getSpinnerDataFrag(Viewholder holder) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.SPINNER_DATA_API, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {

//                                binding.frameLayout.setVisibility(View.GONE);
//                                binding.swipeRefreshBidHis.setRefreshing(false);

                                Log.d("getSpinnerDataFrag", "onResponse: response Sucess: " + response.getString("data"));
                                holder.binding.claimBtn.setEnabled(true);
                                
                                JSONObject data = response.getJSONObject("data");
                                JSONObject spinner = data.getJSONObject("spinner");
                                boolean attemptLimitSpin = data.getBoolean("attempt_limit");
                                Log.d("zzz", "onResponse: attemptLimit spin: " + attemptLimitSpin);

                                if (attemptLimitSpin){

                                    holder.binding.claimBtn.setBackgroundTintList(ColorStateList.valueOf(
                                            context.getResources().getColor(R.color.button_color, context.getTheme())));
                                    holder.binding.claimBtn.setText("Claim");
                                }else {
                                    holder.binding.claimBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(context, "Today's limit exceeds, Claim Tommorow!", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                    holder.binding.claimBtn.setBackgroundTintList(ColorStateList.valueOf(
                                            context.getResources().getColor(R.color.buttonDisableColor, context.getTheme())));
                                    holder.binding.claimBtn.setText("Claimed!");
                                }
                                /*if (attemptLimitSpin) {
                                    holder.binding.claimBtn.setEnabled(true);
//                                    if (activity != null && isAdded()) {
//                                        binding.spinWin.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.button_bg, activity.getTheme()));
//                                        binding.spinWin.setTextColor(getResources().getColor(R.color.white, requireActivity().getTheme()));
//                                    }
//                                    binding.spinWin.setBackground(getResources().getDrawable(R.drawable.button_bg, getActivity().getTheme()));
//                                    binding.spinWin.setTextColor(getResources().getColor(R.color.white, requireActivity().getTheme()));

                                } else {
                                    holder.binding.claimBtn.setEnabled(false);
//                                    if (activity != null && isAdded()) {
//                                        binding.spinWin.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.disable_button_bg, activity.getTheme()));
//                                        binding.spinWin.setTextColor(getResources().getColor(R.color.whiteOnly, requireActivity().getTheme()));
//                                    }
//                                    binding.spinWin.setBackground(getResources().getDrawable(R.drawable.disable_button_bg, getActivity().getTheme()));
//                                    binding.spinWin.setTextColor(getResources().getColor(R.color.whiteOnly, requireActivity().getTheme()));

                                }*/


                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("getSpinnerDataFrag", "onResponse: response Failed: " + response.getString("data"));

                            } else {
                                Log.d("getSpinnerDataFrag", "onResponse: something went wrong");

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getSpinnerDataFrag", "onResponse: error response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(context));
                return header;
            }
        };
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    private void getScratchDetailsFromServerFrag(Viewholder holder) {
//        scratchCoinList = new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.SCRATCH_CARD_GET_API, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getBoolean("status") && response.getInt("code") == 200) {

                                Log.d("getScratchDetailsFromServerFrag", "onResponse: respose success " +
                                        response.getString("data"));
                                JSONObject data = response.getJSONObject("data");
                                JSONObject scratchCard = data.getJSONObject("scratchCard");
                                holder.binding.claimBtn.setEnabled(true);

                                boolean attemptLimit = data.getBoolean("attempt_limit");
                                Log.d("zzz", "onResponse: attemptLimit scratch: " + attemptLimit);
//                                holder.binding.claimBtn.setEnabled(attemptLimit);
                                if (attemptLimit){
                                   
                                    holder.binding.claimBtn.setBackgroundTintList(ColorStateList.valueOf(
                                            context.getResources().getColor(R.color.button_color, context.getTheme())));
                                    holder.binding.claimBtn.setText("Claim");
                                }else {
                                    holder.binding.claimBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(context, "Today's limit exceeds, Claim Tommorow!", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                    holder.binding.claimBtn.setText("Claimed!");
                                    holder.binding.claimBtn.setBackgroundTintList(ColorStateList.valueOf(
                                            context.getResources().getColor(R.color.buttonDisableColor, context.getTheme())));
                                }
                               /* if (attemptLimit) {
                                    holder.binding.claimBtn.setEnabled(true);
//                                    if (activity != null && isAdded()) {
//                                        binding.scratchWin.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.button_bg, activity.getTheme()));
//                                        binding.scratchWin.setTextColor(getResources().getColor(R.color.white, requireActivity().getTheme()));
//                                    }
//                                    binding.scratchWin.setBackground(getResources().getDrawable(R.drawable.button_bg, getActivity().getTheme()));
//                                    binding.scratchWin.setTextColor(getResources().getColor(R.color.white, requireActivity().getTheme()));
                                } else {
                                    holder.binding.claimBtn.setEnabled(false);
//                                    if (activity != null && isAdded()) {
//                                        binding.scratchWin.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.disable_button_bg, activity.getTheme()));
//                                        binding.scratchWin.setTextColor(getResources().getColor(R.color.whiteOnly, requireActivity().getTheme()));
//                                    }
//                                    binding.scratchWin.setBackground(getResources().getDrawable(R.drawable.disable_button_bg, getActivity().getTheme()));
//                                    binding.scratchWin.setTextColor(getResources().getColor(R.color.whiteOnly, requireActivity().getTheme()));
                                }*/


                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("getScratchDetailsFromServerFrag", "onResponse: respose Failed " + response.getString("data"));

                            } else {
                                Log.d("getScratchDetailsFromServerFrag", "onResponse: something went wrong ");

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getScratchDetailsFromServerFrag", "onResponse: error response " + error.getMessage());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(context));
                return header;
            }
        };
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }


    private void loadRewardedAd() {
        myRewardedAd = MaxRewardedAd.getInstance(appLovinAdUnitIdReward, (Activity) context);
        myRewardedAd.setListener(new MaxRewardedAdListener() {
            @Override
            public void onUserRewarded(MaxAd maxAd, MaxReward maxReward) {

            }

            @Override
            public void onRewardedVideoStarted(MaxAd maxAd) {

            }

            @Override
            public void onRewardedVideoCompleted(MaxAd maxAd) {
                loadRewardedAd();
                context.startActivity(new Intent(context, SpinWheelActivity.class));
            }

            @Override
            public void onAdLoaded(MaxAd maxAd) {
                Log.d("AppLovin", "onAdLoaded: Applovin Rewarded is loaded");

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
                Toast.makeText(context, "Ad loaded Failed", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
                Log.d("ccc", "onAdDisplayFailed: " + maxError.getMessage());

            }
        });
        myRewardedAd.loadAd();
    }


    private void loadInterstitialAd() {

        myInterstitialAd = new MaxInterstitialAd(appLovinAdUnitIdInter, (Activity) context);
        myInterstitialAd.setListener(new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd maxAd) {
                Log.d("AppLovin", "onAdLoaded: Applovin Interstitial ad");
                // Reset retry attempt
                retryAttempt = 0;
            }

            @Override
            public void onAdDisplayed(MaxAd maxAd) {

            }


            @Override
            public void onAdHidden(MaxAd maxAd) {
                myInterstitialAd.loadAd();
                Log.d("AppLovin Interstitial", "onBindViewHolder: on ad hidden isscratch: " + isScratch);
                if (isScratch) {
//                    scratch page
                    context.startActivity(new Intent(context, ScratchWinActivity.class));
                } else {
//                    spin page
                    context.startActivity(new Intent(context, SpinWheelActivity.class));
                }
            }

            @Override
            public void onAdClicked(MaxAd maxAd) {

            }

            @Override
            public void onAdLoadFailed(String s, MaxError maxError) {
                // Interstitial ad failed to load
                // AppLovin recommends that you retry with exponentially higher delays up to a maximum delay (in this case 64 seconds)

                /*retryAttempt++;
                long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myInterstitialAd.loadAd();
                        Log.d("AppLovin", "run: loadAd App Lovin: retry attempt: " + retryAttempt);
                    }
                }, delayMillis);*/
            }

            @Override
            public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
                Log.d("myIntertitialAd", "onAdDisplayFailed: " + maxError.getMessage());
            }
        });
        myInterstitialAd.loadAd();
    }
}

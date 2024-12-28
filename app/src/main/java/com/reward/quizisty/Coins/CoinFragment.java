package com.reward.quizisty.Coins;

import static com.reward.quizisty.Activities.MainActivity.playTimeAppKey;
import static com.reward.quizisty.Fragments.VoucherChildFragment.getSortedArrayList;
import static com.reward.quizisty.Utils.Constants.AUTHORISATION;
import static com.reward.quizisty.Utils.Constants.BEARER;
import static com.reward.quizisty.Utils.Constants.CONTENT_TYPE;
import static com.reward.quizisty.Utils.Constants.CONTENT_TYPE_VALUE;
import static com.reward.quizisty.Utils.Constants.VOUCHER_MAIN_URL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.reward.quizisty.Activities.HighCoinActivity;
import com.reward.quizisty.Activities.VouchersActivity;
import com.reward.quizisty.Adapters.VoucherMainAdapter2;
import com.reward.quizisty.Modals.BannerModal;
import com.reward.quizisty.Modals.VoucherMainModal;
import com.reward.quizisty.Utils.Constants;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.LevelPlayRewardedVideoListener;
import com.pubscale.sdkone.offerwall.OfferWall;
import com.pubscale.sdkone.offerwall.models.OfferWallListener;
import com.pubscale.sdkone.offerwall.models.Reward;
import com.reward.quizisty.Activities.CoinHistoryActivity;
import com.reward.quizisty.Activities.DiamondHistoryActivity;
import com.reward.quizisty.Activities.MainActivity;
import com.reward.quizisty.Adapters.AdNetAdapter;
import com.reward.quizisty.Adapters.BannerAdapter;
import com.reward.quizisty.Adapters.BuyCoinAdapter;
import com.reward.quizisty.Adapters.WatchVidAdapter;
import com.reward.quizisty.Modals.AdNetModal;
import com.reward.quizisty.Modals.UserModal;
import com.reward.quizisty.R;
import com.reward.quizisty.Utils.ControlRoom;
import com.reward.quizisty.databinding.FragmentCoinBinding;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.playtimeads.PlaytimeAds;
import com.playtimeads.listeners.OfferWallInitListener;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.UnityAds;
import com.ironsource.mediationsdk.IronSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CoinFragment extends Fragment {

    private static final String TAG = "CoinFragment";
    FragmentCoinBinding binding;
    CoinViewModal coinViewModal;
    static boolean dailyCoins = false;
    AdNetAdapter adNetAdapter;

    private String unityGameID = "5685274";
    private Boolean testMode = true;
    private final String unityRewardedPlacementId = "Rewarded_Android_Bidding";
    private MaxRewardedAd myRewardedAd;
//    ViewPager2 viewPager;

    public CoinFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCoinBinding.inflate(inflater, container, false);
        coinViewModal = new ViewModelProvider((ViewModelStoreOwner) getViewLifecycleOwner()).get(CoinViewModal.class);

        ArrayList<AdNetModal> adNetList = new ArrayList<>();
        adNetAdapter = new AdNetAdapter(adNetList, requireContext());

        binding.swipeRefresh.setOnRefreshListener(() -> {
//            refresh users coins
            coinViewModal.fetchUserData(requireContext());
            adNetAdapter.notifyDataSetChanged();
            getVoucherMainList2();
        });

        binding.coinTxt.setOnClickListener(v -> startActivity(new Intent(getContext(), CoinHistoryActivity.class)));
        binding.diamondTv.setOnClickListener(v -> startActivity(new Intent(getContext(), DiamondHistoryActivity.class)));

        binding.vouDesc.setText(HtmlCompat.fromHtml(
                "Book your slot and get a chance to win vouchers of popular brands like Amazon, Flipkart and  more. <b>Kam Coins me Jyada Vouchers.</b>",
                0
        ));
        binding.voucherCard.setOnClickListener(v -> startActivity(new Intent(requireContext(), VouchersActivity.class)));


//        observe user data
        coinViewModal.getUserData().observe(getViewLifecycleOwner(), new Observer<UserModal>() {
            @Override
            public void onChanged(UserModal userModal) {
                //        Set coins value
                binding.coinTxt.setText(userModal.getCoins());
                binding.diamondTv.setText(userModal.getDiamonds());
                binding.swipeRefresh.setRefreshing(false);

            }
        });

        if (coinViewModal.getUserData().getValue() == null) {
            coinViewModal.fetchUserData(requireContext());
        }

//        High Coins banner clicked
        binding.highCoins.setOnClickListener(v -> {
//            ((MainActivity) requireActivity()).jumpToHighCoins();
            startActivity(new Intent(requireActivity(), HighCoinActivity.class));
        });



//        Banners Data
        coinViewModal.getBannersList().observe(getViewLifecycleOwner(), new Observer<ArrayList<BannerModal>>() {
            @Override
            public void onChanged(ArrayList<BannerModal> bannerList) {
                if (bannerList.isEmpty()) {
                    binding.bannerContainer.setVisibility(View.GONE);
                } else {


                    binding.bannerContainer.setAdapter(new BannerAdapter(bannerList, requireContext()));
                    binding.bannerContainer.setLayoutManager(new CarouselLayoutManager());



                }
            }
        });

        if (coinViewModal.getBannersList().getValue() == null) {
            coinViewModal.fetchBannersList(requireContext());
        }




        binding.pubCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OfferWall.launch(getActivity(), new OfferWallListener() {
                    @Override
                    public void onRewardClaimed(Reward reward) {
                        Toast.makeText(getActivity(), "Pubscale Reward Coins added successfully! "+reward.getAmount()+" coins" , Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onOfferWallShowed() {

                    }

                    @Override
                    public void onOfferWallClosed() {

                    }

                    @Override
                    public void onFailed(String s) {
                        Toast.makeText(getActivity(), "Something went wrong! pubscale", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });




//        Playtime ads offerwall
        binding.playTimeCard.setOnClickListener(v -> {
            if (PlaytimeAds.getInstance().isInitialized()) {
                PlaytimeAds.getInstance().open(requireContext());
            } else {
                Toast.makeText(requireContext(), "PlaytimeAds is not initialized", Toast.LENGTH_SHORT).show();
                // Initialize Playtime Ads SDK
                initializePlaytimeAds();
            }
        });

        initializeUnityAd();
        initialiseUnityLevelPlay();

//        Daily coins
        getDailyCheckInDataEligibilityFrag();
        binding.dailyCoinBtn.setOnClickListener(v -> {

            /*Uri excentivUri = Uri.parse("https://excentiv.com/offerwall/?userid="+
                    ControlRoom.getInstance().getId(requireActivity()) + "&key=" + EXCENTIV_API_KEY);
            Intent intent = new Intent(Intent.ACTION_VIEW, excentivUri);
            requireContext().startActivity(intent);*/


        });

//        vouchers
        getVoucherMainList2();
        binding.viewAllVoucher.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), VouchersActivity.class)));



//        Buy Coins Data
        coinViewModal.getBuyCoinList().observe(getViewLifecycleOwner(), buyCoinList -> {
            if (buyCoinList.isEmpty()) {
                binding.buyCoinRv.setVisibility(View.GONE);
            } else {
                binding.buyCoinRv.setAdapter(new BuyCoinAdapter(buyCoinList, requireContext()));
                binding.buyCoinRv.setLayoutManager(new LinearLayoutManager(requireContext()));
            }
        });

        if (coinViewModal.getBuyCoinList().getValue() == null) {
            coinViewModal.fetchBuyCoinList(requireContext());
        }

//        Watch Videos List Data
        coinViewModal.getWatchVideoList().observe(getViewLifecycleOwner(), watchVidList -> {
            if (watchVidList.isEmpty()) {
                binding.watchVidRv.setVisibility(View.GONE);
            } else {
                binding.watchVidRv.setAdapter(new WatchVidAdapter(watchVidList, requireContext()));
                binding.watchVidRv.setLayoutManager(new GridLayoutManager(requireContext(), 2));
            }
        });

        if (coinViewModal.getWatchVideoList().getValue() == null) {
            coinViewModal.fetchWatchVideoList(requireContext());
        }

//        Ad Networks


//        adNetList.add(new AdNetModal(R.drawable.money_daily, "Daily Coins"));
//        adNetList.add(new AdNetModal(R.drawable.bitlabs, "BitLabs",0));
//        adNetList.add(new AdNetModal(R.drawable.pollfish, "Pollfish",1));
//        adNetList.add(new AdNetModal(R.drawable.cpxresearch, "CPX Research",2));
//        adNetList.add(new AdNetModal(R.drawable.ayetstudios, "Ayet Studios",3));
        adNetList.add(new AdNetModal(R.drawable.scratch_win, "Scratch & Win",4));
        adNetList.add(new AdNetModal(R.drawable.spin_win, "Spin & Win",5));
        adNetList.add(new AdNetModal(R.drawable.excentiv, "Excentiv",6));
        adNetList.add(new AdNetModal(R.drawable.ewall, "EWALL",7));

        binding.adNetRv.setAdapter(adNetAdapter);
        binding.adNetRv.setLayoutManager(new GridLayoutManager(requireContext(), 2));


        return binding.getRoot();
    }

    private void getVoucherMainList2() {
        binding.voucherRv2.showShimmerAdapter();
        ArrayList<VoucherMainModal> voucherMainList = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, VOUCHER_MAIN_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200){
                                Log.d("getVoucherMainList", "onResponse: response Sucessfull: "+ response.getString("data"));
                                binding.voucherRv2.hideShimmerAdapter();
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String id = jsonObject.getString("id");
                                    String name = jsonObject.getString("name");
                                    String mrp = jsonObject.getString("mrp");
                                    int price_per_spot = jsonObject.getInt("price_per_spot");
                                    String total_spot = jsonObject.getString("total_spot");
                                    String empty_spot = jsonObject.getString("empty_spot");
                                    String winnig_code = jsonObject.getString("winnig_code");
                                    String winnig_daimonds = jsonObject.getString("winnig_daimonds");
                                    String short_desc = jsonObject.getString("short_desc");
                                    String details = jsonObject.getString("details");
                                    String images = jsonObject.getString("images");
                                    String full_status = jsonObject.getString("full_status");
                                    boolean full_status_bool;
                                    if (Integer.parseInt(full_status)==0){
                                        full_status_bool = false;
                                    }else {
                                        full_status_bool= true;
                                    }



                                    VoucherMainModal voucherMainModal = new VoucherMainModal(
                                            "2",empty_spot,total_spot,name,price_per_spot,short_desc,details,id,images,
                                            full_status_bool,winnig_code);

                                    voucherMainModal.setMrp(jsonObject.getString("mrp"));

                                    voucherMainList.add(voucherMainModal);
                                }
                                Log.d("getVoucherMainList", "onResponse: Voucher List Size: "+voucherMainList.size());
//                        checking voucher winners list empty.
                                if (voucherMainList.isEmpty()) {
                                    binding.voucherRv2.setVisibility(View.INVISIBLE);
                                    binding.vouMessage.setVisibility(View.VISIBLE);
                                } else {
                                    binding.voucherRv2.setVisibility(View.VISIBLE);
                                    binding.vouMessage.setVisibility(View.INVISIBLE);

                                    VoucherMainAdapter2 voucherDetailAdapter2  = new VoucherMainAdapter2(getSortedArrayList(voucherMainList),
                                            requireContext());
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false);
                                    binding.voucherRv2.setAdapter(voucherDetailAdapter2);
                                    binding.voucherRv2.setLayoutManager(layoutManager);
                                    binding.voucherRv2.setNestedScrollingEnabled(false);
                                }



                            }else if (!response.getBoolean("status") && response.getInt("code") == 201){
                                Log.d("getVoucherMainList", "onResponse: response Failed: "+ response.getString("data"));
                                binding.voucherRv2.hideShimmerAdapter();
                                binding.vouMessage.setVisibility(View.VISIBLE);
                                binding.vouMessage.setText(response.getString("data"));
                            }else {
                                Log.d("getVoucherMainList", "onResponse: Something went wrong");
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getVoucherMainList", "onResponse: error ResPonse:  " + error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(requireContext()));
                return header;
            }
        };
        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest);

    }

    private void initialiseUnityLevelPlay() {

        Placement placement = IronSource.getRewardedVideoPlacementInfo("Level_placement");
// Null can be returned instead of a placement if the placementName is not valid.
        if (placement != null) {
            String rewardName = placement.getRewardName();
            int rewardAmount = placement.getRewardAmount();
        }

        IronSource.setLevelPlayRewardedVideoListener(new LevelPlayRewardedVideoListener() {
            // Indicates that there's an available ad.
            // The adInfo object includes information about the ad that was loaded successfully
            // Use this callback instead of onRewardedVideoAvailabilityChanged(true)
            @Override
            public void onAdAvailable(AdInfo adInfo) {
                Log.d(TAG, "onAdAvailable: Levelplay"+ adInfo.toString());
                IronSource.showRewardedVideo("Level_placement");
            }
            // Indicates that no ads are available to be displayed
            // Use this callback instead of onRewardedVideoAvailabilityChanged(false)
            @Override
            public void onAdUnavailable() {}
            // The Rewarded Video ad view has opened. Your activity will loose focus
            @Override
            public void onAdOpened(AdInfo adInfo) {}
            // The Rewarded Video ad view is about to be closed. Your activity will regain its focus
            @Override
            public void onAdClosed(AdInfo adInfo) {}
            // The user completed to watch the video, and should be rewarded.
            // The placement parameter will include the reward data.
            // When using server-to-server callbacks, you may ignore this event and wait for the ironSource server callback
            @Override
            public void onAdRewarded(Placement placement, AdInfo adInfo) {}
            // The rewarded video ad was failed to show
            @Override
            public void onAdShowFailed(IronSourceError error, AdInfo adInfo) {}
            // Invoked when the video ad was clicked.
            // This callback is not supported by all networks, and we recommend using it
            // only if it's supported by all networks you included in your build
            @Override
            public void onAdClicked(Placement placement, AdInfo adInfo) {}
        });
    }

    private void initializeUnityAd() {

//        Unity
        UnityAds.initialize(requireContext(), unityGameID, testMode,
                new IUnityAdsInitializationListener() {
                    @Override
                    public void onInitializationComplete() {
                        Log.d("UnityAds", "onInitializationComplete: ");
                        loadUnityRewardedAd();
                    }

                    @Override
                    public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
                        Log.d("UnityAds", "onInitializationComplete: Unity Ad NOT initialised: "+ error.name());
                    }
                }
        );


    }

    private void loadUnityRewardedAd() {

        UnityAds.load(unityRewardedPlacementId, new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {
                Log.d("UnityAd", "onUnityAdsAdLoaded: Ad loaded successfully");
            }

            @Override
            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                Log.d("UnityAd", "onUnityAdsAdLoaded: Ad loaded Failed " + error.name() + message);
            }
        });
    }

    private void getDailyCheckInDataEligibilityFrag() {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.DAILY_CHECK_IN_GET_API, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        binding.dailyCoinBtn.setVisibility(View.VISIBLE);
                        binding.dailyCoinProgressbar.setVisibility(View.GONE);
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {


                                Log.d("getDailyCheckInDataEligibilityFrag", "onResponse: response Sucess: " + response.getString("data"));

                                JSONObject data = response.getJSONObject("data");

                                boolean dailyCheckIn = data.getBoolean("dayily_limit");     //"dayily_limit" = does user have any attempt available

                                dailyCoins = dailyCheckIn;
                                if (dailyCheckIn) {
                                    binding.dailyCoinBtn.setEnabled(true);
                                    binding.dailyCoinBtn.setText("Claim");
                                }else {
                                    binding.dailyCoinBtn.setEnabled(false);
                                    binding.dailyCoinBtn.setText("Claimed");
                                }



                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("getDailyCheckInDataEligibilityFrag", "onResponse: response Failed: " + response.getString("data"));

                            } else {
                                Log.d("getDailyCheckInDataEligibilityFrag", "onResponse: something went wrong");

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getDailyCheckInDataEligibilityFrag", "onResponse: error response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(requireActivity()));
                return header;
            }
        };
        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest);

    }

    private void initializePlaytimeAds() {

        String uid = ControlRoom.getInstance().getId(requireContext());
        PlaytimeAds.getInstance().init(requireContext(), playTimeAppKey, uid,
                new OfferWallInitListener() {
                    @Override
                    public void onInitSuccess() {
                        Log.e(TAG, "onInitSuccess");
                    }

                    @Override
                    public void onAlreadyInitializing() {
                        Log.e(TAG, "onAlreadyInitializing");
                    }

                    @Override
                    public void onInitFailed(String error) {
                        Log.e(TAG, "onInitFailed: " + error);
                    }
                });
    }


}
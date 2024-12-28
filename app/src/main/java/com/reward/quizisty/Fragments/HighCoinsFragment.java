package com.reward.quizisty.Fragments;

import static com.reward.quizisty.Utils.Constants.AUTHORISATION;
import static com.reward.quizisty.Utils.Constants.CONTENT_TYPE;
import static com.reward.quizisty.Utils.Constants.CONTENT_TYPE_VALUE;
import static com.reward.quizisty.Utils.Constants.OFFER18_OFFERS_API;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.reward.quizisty.Activities.MainActivity;
import com.reward.quizisty.Adapters.Offer18MainListAdapter;
import com.reward.quizisty.Modals.Offer18MainListModal;
import com.reward.quizisty.Utils.ControlRoom;
import com.reward.quizisty.Utils.GPSReceiver;
import com.reward.quizisty.databinding.FragmentHighCoinsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HighCoinsFragment extends Fragment {

    FragmentHighCoinsBinding binding;
    ArrayList<Offer18MainListModal> offer18MainList;
    public Offer18MainListAdapter offer18MainListAdapter;
    private static final String TAG = "HighCoinsFragment";
    GPSReceiver gpsReceiver;
    boolean gpsStatus = true;

    public HighCoinsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gpsReceiver = new GPSReceiver(requireContext());
        gpsReceiver.registerReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for requireContext() fragment
        binding = FragmentHighCoinsBinding.inflate(inflater, container, false);


        //      Checking location permission


        binding.permBtn.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).requestPermission();
        });
        ((MainActivity) requireActivity()).adapterListener(this);

        if (!isLocationEnabled()) {
            binding.gpsTxt.setVisibility(View.VISIBLE);
        }

        binding.highCoinsRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isLocationEnabled()) {
                ((MainActivity) requireActivity()).getLastLocation();
                }else {
                    Toast.makeText(requireContext(), "Please enable location.", Toast.LENGTH_SHORT).show();
                    binding.highCoinsRefresher.setRefreshing(false);
                }
            }
        });


        gpsReceiver.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLocationOn) {

                if (!isLocationOn) {
                    if (!gpsStatus) {
                        Log.d("locSer", "onChanged: gps is off ");
                        binding.trakierMainListRv.setVisibility(View.GONE);
                        binding.gpsTxt.setVisibility(View.VISIBLE);
                        binding.highCoinsRefresher.setRefreshing(false);
                        gpsStatus = true;
                    }
                }else {
                    if (gpsStatus) {
                        Log.d("locSer", "onChanged: gps is On ");
                        binding.trakierMainListRv.setVisibility(View.VISIBLE);
                        binding.gpsTxt.setVisibility(View.GONE);
//                        ((MainActivity) requireActivity()).getLastLocation();
//                        binding.highCoinsRefresher.setRefreshing(true);
                        gpsStatus = false;
                    }
                }
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            binding.highCoinsRefresher.setRefreshing(false);
            binding.permissionLayout.setVisibility(View.GONE);

        } else {
            binding.highCoinsRefresher.setRefreshing(false);
            binding.permissionLayout.setVisibility(View.VISIBLE);
        }
//        Location Service is disabled. Please enable 'Location' to see High Coins Offers.


    }

    @Override
    public void onDestroy() {
        gpsReceiver.unregisterReceiver();
        super.onDestroy();
    }

    public void fetchAllCampaigns() {

        offer18MainList = new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, OFFER18_OFFERS_API,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("locSer", "onChanged: fetchAllCampaigns under response");
                binding.highCoinsRefresher.setRefreshing(false);
                try {
                    if (response.getInt("response_code") == 200) {


//                        Log.d(TAG, "onResponse: ");

                        JSONArray campaigns = response.getJSONArray("data");

                        for (int i = 0; i < campaigns.length(); i++) {
                            int totalCoins = 0;
                            JSONObject campaignData = campaigns.getJSONObject(i);
                            String campaignId = campaignData.getString("offerid");


                            String campaignName = campaignData.getString("name");
                            String campaignShortDesc = campaignData.getString("short_description");
                            String campaignPrice = campaignData.getString("price");
                            String campaignIcon = campaignData.getString("logo");
                            String campaignClickUrl = campaignData.getString("click_url");
                            String campaignCountryCode = campaignData.getString("country_allow");


                            String campaignPosterImg = campaignData.getJSONArray("creatives")
                                    .getJSONObject(0)
                                    .getString("url");

                            if (campaignCountryCode.equals(((MainActivity) requireActivity()).LOCATION_COUNTRY)) {

                                Offer18MainListModal offer18MainModal = new Offer18MainListModal(
                                        campaignPosterImg, campaignIcon, campaignName, campaignShortDesc,
                                        campaignPrice, ""
                                );
                                offer18MainModal.setAdId(campaignId);
                                offer18MainModal.setClickUrl(campaignClickUrl);


                                offer18MainList.add(offer18MainModal);

                            }


                                /*JSONArray goals = campaignData.getJSONArray("goals");

                                for (int j= 0; j< goals.length(); j++){
                                    JSONObject singleGoal = goals.getJSONObject(j);

                                    int payout = singleGoal.getJSONArray("payouts").getJSONObject(0).getInt("payout");

                                    totalCoins = totalCoins + payout;

                                }*/


                        }

                        if (offer18MainList.isEmpty()) {
                            binding.trakierMainListRv.setVisibility(View.GONE);
                            binding.emptyCampaignTxt.setVisibility(View.VISIBLE);
                            binding.emptyCampaignTxt.setText("No campaigns found!");
//                            if (isLocationEnabled()) {
//                            } else {
//                                binding.emptyCampaignTxt.setVisibility(View.VISIBLE);
//                                binding.emptyCampaignTxt.setText("Location Service is disabled. Please enable 'Location' to see High Coins Campaigns.");
//                            }
                        } else {
                            binding.trakierMainListRv.setVisibility(View.VISIBLE);
                            binding.emptyCampaignTxt.setVisibility(View.GONE);
                            offer18MainListAdapter = new Offer18MainListAdapter(offer18MainList, requireContext());
                            binding.trakierMainListRv.setLayoutManager(new LinearLayoutManager(requireContext()));
                            binding.trakierMainListRv.setAdapter(offer18MainListAdapter);
                        }


                    } else {
                        Log.d("fetchAllCampaigns", "onResponse : Something went wrong: "
                                + response.getString("data"));
                    }
                } catch (JSONException e) {
                    Log.d("fetchAllCampaigns", "onResponse Failed : Json Exception: " + e.getMessage());
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("fetchAllCampaigns", "onResponse Failed : VolleyError: " + error.getMessage());
                binding.highCoinsRefresher.setRefreshing(false);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, "Bearer " + ControlRoom.getInstance().getAccessToken(requireContext()));
                return header;
            }
        };

        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest);
    }

    public boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);

        boolean locationEnabled;
        locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return locationEnabled;
    }

    public void enableLocationMsg() {
        binding.emptyCampaignTxt.setText("Location Service is disabled. Please enable 'Location' to see High Coins Offers.");
        binding.emptyCampaignTxt.setVisibility(View.VISIBLE);
    }
}
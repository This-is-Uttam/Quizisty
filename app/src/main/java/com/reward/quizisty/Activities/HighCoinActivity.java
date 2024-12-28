package com.reward.quizisty.Activities;

import static com.reward.quizisty.Activities.MainActivity.LOCATION_REQ_CODE;
import static com.reward.quizisty.Utils.Constants.AUTHORISATION;
import static com.reward.quizisty.Utils.Constants.CONTENT_TYPE;
import static com.reward.quizisty.Utils.Constants.CONTENT_TYPE_VALUE;
import static com.reward.quizisty.Utils.Constants.OFFER18_OFFERS_API;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.reward.quizisty.Adapters.Offer18MainListAdapter;
import com.reward.quizisty.Modals.Offer18MainListModal;
import com.reward.quizisty.R;
import com.reward.quizisty.Utils.ControlRoom;
import com.reward.quizisty.databinding.ActivityHighCoinBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HighCoinActivity extends AppCompatActivity {
    private static final String TAG = "HighCoinActivity";
    Offer18MainListAdapter offer18MainListAdapter;
    private static String LOCATION_CITY = "";
    private static String LOCATION_COUNTRY = "";
    ActivityHighCoinBinding binding;
    ArrayList<Offer18MainListModal> offer18MainList;
    MainActivity mainActivity = new MainActivity();
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHighCoinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.HCActivity.setNavigationOnClickListener(v -> {
            finish();
        });

        getAllHighCoinCampaigns();


        binding.permBtn.setOnClickListener(v -> {
            mainActivity.requestPermission();
        });
//        mainActivity.adapterListener(this);

        if (!isLocationEnabled()) {
            binding.gpsTxt.setVisibility(View.VISIBLE);
        }

        binding.highCoinsRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllHighCoinCampaigns();
            }
        });
    }

    private void getAllHighCoinCampaigns() {
        if (isLocationEnabled()) {
            getLastLocation();
        }else {
            Toast.makeText(HighCoinActivity.this, "Please enable location.", Toast.LENGTH_SHORT).show();
            binding.highCoinsRefresher.setRefreshing(false);
        }
    }

    public boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean locationEnabled;
        locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return locationEnabled;
    }

    public void getLastLocation() {
        Log.d("locSer", "onChanged: getLastLocation ");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    Geocoder geocoder = new Geocoder(HighCoinActivity.this, Locale.getDefault());
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1, addresses -> {
                                LOCATION_CITY = addresses.get(0).getLocality();
                                LOCATION_COUNTRY = addresses.get(0).getCountryCode();
                                fetchAllCampaigns();
                                Log.d("locSer", "onChanged: getLastLocation under fetch ");

                                Log.d(TAG, "getLastLocation: \n Latitude: " + addresses.get(0).getLatitude() +
                                        "\n Longitude: " + addresses.get(0).getLongitude() +
                                        "\n Address: " + addresses.get(0).getAddressLine(0) +
                                        "\n Locality: " + addresses.get(0).getLocality() +
                                        "\n Area: " + addresses.get(0).getAdminArea() +
                                        "\n Country: " + addresses.get(0).getCountryName());

                            });
                        } else {
                            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            if (addressList != null) {
                                LOCATION_CITY = addressList.get(0).getLocality();
                                LOCATION_COUNTRY = addressList.get(0).getCountryCode();
                               fetchAllCampaigns();


                                Log.d(TAG, "getLastLocation:(NOT tiramisu) \n Latitude: " + addressList.get(0).getCountryCode());
                            } else {
                                Toast.makeText(this, "AddressList NULL", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (IOException e) {
                        Toast.makeText(this, "Something went wrong! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }
                }

            });

        }

    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == LOCATION_REQ_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else if (!(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION))) {

                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this)
                        .setTitle("Permission Required!")
                        .setMessage("To access some location specific Offers in our app you need to manually grant the LOCATION permission from settings. Allow the permission from Settings.")
                        .setCancelable(false)
                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);

                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                materialAlertDialogBuilder.show();
            } else
                requestPermission();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

                            if (campaignCountryCode.equals(LOCATION_COUNTRY)) {

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
                            offer18MainListAdapter = new Offer18MainListAdapter(offer18MainList, HighCoinActivity.this);
                            binding.trakierMainListRv.setLayoutManager(new LinearLayoutManager(HighCoinActivity.this));
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
                header.put(AUTHORISATION, "Bearer " + ControlRoom.getInstance().getAccessToken(HighCoinActivity.this));
                return header;
            }
        };

        Volley.newRequestQueue(HighCoinActivity.this).add(jsonObjectRequest);
    }

    public void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


//        check if the Location is turned off.
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            boolean locationEnabled;
            locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);



            if (!locationEnabled) {
//            location service is off
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(HighCoinActivity.this);
                builder.setTitle("Location is disabled")
                        .setMessage("Location Service is disabled. Please enable 'Location' to see High Coins Campaigns.")
                        .setPositiveButton("Enable Location", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).show();
            }else {
                getLastLocation();
            }


        } else if (/*ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) &&*/
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this)
                    .setTitle("Permission Required!")
                    .setMessage("Please provide your location permission to access some specific offers to your location.")
                    .setCancelable(false)
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ActivityCompat.requestPermissions(HighCoinActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQ_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            materialAlertDialogBuilder.show();

        } else {

            ActivityCompat.requestPermissions(HighCoinActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQ_CODE);
        }
    }
}
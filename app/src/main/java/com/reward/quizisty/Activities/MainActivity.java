package com.reward.quizisty.Activities;


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
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.sdk.InitializationListener;
import com.pubscale.sdkone.offerwall.OfferWall;
import com.pubscale.sdkone.offerwall.OfferWallConfig;
import com.reward.quizisty.Coins.CoinFragment;
import com.reward.quizisty.Fragments.HighCoinsFragment;
import com.reward.quizisty.Fragments.MoreFragment;
import com.reward.quizisty.Fragments.RedeemFragment;
import com.reward.quizisty.Fragments.ReferFragment;
import com.reward.quizisty.R;
import com.reward.quizisty.Utils.ControlRoom;
import com.reward.quizisty.databinding.ActivityMainBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.playtimeads.PlaytimeAds;
import com.playtimeads.listeners.OfferWallInitListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final int LOCATION_REQ_CODE = 1001;
    private static final String PUBSCALE_APP_ID = "81177657";
    private static final String TAG = "MainActivity";
    private static final String LEVELPLAY_APP_KEY = "161189b0d";
    ActivityMainBinding binding;
    FusedLocationProviderClient fusedLocationProviderClient;
    ArrayList<Fragment> fragments;
    Fragment currentFrg;
    FragmentManager fragmentManager;
    public String LOCATION_CITY;
    public String LOCATION_COUNTRY;
    HighCoinsFragment highCoinsFragment;
    public static final String playTimeAppKey = "c6d838298afce645";

    AppUpdateManager appUpdateManager;
    ActivityResultLauncher<IntentSenderRequest> updateLauncher;

    ActivityResultLauncher<Intent> locationLauncher;

    public void adapterListener(HighCoinsFragment HCfragment) {
        Log.d(TAG, "adapterListener: called");
        highCoinsFragment = HCfragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragments = new ArrayList<>();
        fragments.add(new CoinFragment());
        fragments.add(new HighCoinsFragment());
        fragments.add(new ReferFragment());
        fragments.add(new RedeemFragment());
        fragments.add(new MoreFragment());


//        ViewPager2 mainViewPager = binding.mainViewPager;

//        mainViewPager.setAdapter(new MainViewPagerAdapter(this, fragments));
//        mainViewPager.setOffscreenPageLimit(5);

        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


            fragmentTransaction.add(R.id.fragment_container, fragments.get(0), "coinFragment");

            fragmentTransaction.add(R.id.fragment_container, fragments.get(1), "highCoinFragment");
            fragmentTransaction.hide(fragments.get(1));

            fragmentTransaction.add(R.id.fragment_container, fragments.get(2), "referFragment");
            fragmentTransaction.hide(fragments.get(2));

            fragmentTransaction.add(R.id.fragment_container, fragments.get(3), "giftFragment");
            fragmentTransaction.hide(fragments.get(3));

            fragmentTransaction.add(R.id.fragment_container, fragments.get(4), "moreFragment");
            fragmentTransaction.hide(fragments.get(4));

            fragmentTransaction.commit();

            currentFrg = fragments.get(0);
        } else {
            fragments.set(0, getSupportFragmentManager().findFragmentByTag("coinFragment"));
            fragments.set(1, getSupportFragmentManager().findFragmentByTag("highCoinFragment"));
            fragments.set(2, getSupportFragmentManager().findFragmentByTag("referFragment"));
            fragments.set(3, getSupportFragmentManager().findFragmentByTag("giftFragment"));
            fragments.set(4, getSupportFragmentManager().findFragmentByTag("moreFragment"));

            String curFragTag = savedInstanceState.getString("currentFragmentTag");
            currentFrg = getSupportFragmentManager().findFragmentByTag(curFragTag);

            if (curFragTag == null) {
                Toast.makeText(this, "Current fragment null", Toast.LENGTH_SHORT).show();
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.show(currentFrg).commit();
        }


        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.coinFragmentMenu) {
                    changeFragment(currentFrg, fragments.get(0));
                    currentFrg = fragments.get(0);
                } else if (item.getItemId() == R.id.highCoinsFragmentMenu) {
                    changeFragment(currentFrg, fragments.get(1));
                    currentFrg = fragments.get(1);
                } else if (item.getItemId() == R.id.referFragmentMenu) {
                    changeFragment(currentFrg, fragments.get(2));
                    currentFrg = fragments.get(2);
                } else if (item.getItemId() == R.id.redeemFragmentMenu) {
                    changeFragment(currentFrg, fragments.get(3));
                    currentFrg = fragments.get(3);
                } else if (item.getItemId() == R.id.moreFragmentMenu) {
                    changeFragment(currentFrg, fragments.get(4));
                    currentFrg = fragments.get(4);
                }
                return true;
            }
        });


//        Getting Location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        requestPermission();

        initPubscale();

        IronSource.init(this, LEVELPLAY_APP_KEY, new InitializationListener() {
            @Override
            public void onInitializationComplete() {
                // ironSource SDK is initialized
                Log.d(TAG, "onInitializationComplete: levelplay");
            }
        });




//        Playtime ads integration

        PlaytimeAds.getInstance().destroy(); // clears your previous session
        String uid = ControlRoom.getInstance().getId(this);
        PlaytimeAds.getInstance().init(this, playTimeAppKey, uid,
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

//        location launcher initialise
        /*locationLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                    Log.d(TAG, "onActivityResult: result okk"+ o.getResultCode());
                if (o.getResultCode() == RESULT_OK) {
                }
            }
        });*/

        // check in-app update....
        updateLauncher = registerForActivityResult(
                new ActivityResultContracts.StartIntentSenderForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // handle callback
                        if (result.getResultCode() != RESULT_OK) {
                            Log.d("newUpdate", "onActivityResult: Update flow failed! Result code " + result.getResultCode());
                        }
                    }
                });

        checkForAppImmediateUpdate();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (binding.bottomNavigationView.getSelectedItemId() == R.id.coinFragmentMenu) {
                    // close app
                    finish();
                }else {
                    binding.bottomNavigationView.setSelectedItemId(R.id.coinFragmentMenu);
                }

            }
        });

        initialiseApplovin();
    }

    private void initialiseApplovin() {
        // Make sure to set the mediation provider value to "max" to ensure proper functionality
        AppLovinSdk.getInstance( this ).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk( this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(AppLovinSdkConfiguration appLovinSdkConfiguration) {
                Log.d(TAG, "onSdkInitialized: Applovin");
            }

        } );
    }

    public void initPubscale() {
        // Pubscale Offerwall
        OfferWallConfig offerWallConfig =
                new OfferWallConfig.Builder(this, PUBSCALE_APP_ID)
                        .setUniqueId(ControlRoom.getInstance().getId(this)) //optional, used to represent the user of your application
//                        .setLoaderBackgroundBitmap(backgroundBitmap)//optional
//                        .setLoaderForegroundBitmap(foregroundBitmap)//optional
                        .setFullscreenEnabled(false)//optional
                        .build();

        // Pubscale Initialization
        OfferWall.init(offerWallConfig);
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
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
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
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQ_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            materialAlertDialogBuilder.show();

        } else {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQ_CODE);
        }
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("currentFragmentTag", currentFrg.getTag());
    }

    private void changeFragment(Fragment currentFragment, Fragment nextFragment) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(currentFragment);
        fragmentTransaction.show(nextFragment);
        fragmentTransaction.commit();

    }

    @Override
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

    public void getLastLocation() {
        Log.d("locSer", "onChanged: getLastLocation ");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1, addresses -> {
                                LOCATION_CITY = addresses.get(0).getLocality();
                                LOCATION_COUNTRY = addresses.get(0).getCountryCode();
                                highCoinsFragment.fetchAllCampaigns();
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
                                highCoinsFragment.fetchAllCampaigns();


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

    public void jumpToHighCoins() {
        binding.bottomNavigationView.setSelectedItemId(R.id.highCoinsFragmentMenu);
    }

    private void checkForAppImmediateUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this);
        Log.d("newUpdate", "appUpdateManager created");


        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        Log.d("newUpdate", "appUpdateInfoTask initialised " + appUpdateInfoTask.isComplete());

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                                                   @Override
                                                   public void onSuccess(AppUpdateInfo appUpdateInfo) {
                                                       Log.d("newUpdate", "checkForAppImmediateUpdate: Update available");
                                                       if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                                                               // This example applies an immediate update. To apply a flexible update
                                                               // instead, pass in AppUpdateType.FLEXIBLE
                                                               && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                                                           Log.d("newUpdate", "checkForAppImmediateUpdate: Update available");
                                                           // Request the update.

                                                           appUpdateManager.startUpdateFlowForResult(
                                                                   // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                                                   appUpdateInfo,
                                                                   // an activity result launcher registered via registerForActivityResult
                                                                   updateLauncher,
                                                                   // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                                                                   // flexible updates.
                                                                   AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                                                                           .setAllowAssetPackDeletion(true)
                                                                           .build());
                                                       } else {

                                                           // Run the app normally


                                                       }
                                                   }

                                               }

        );

    }

    @Override
    protected void onResume() {
        super.onResume();

        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                // If an in-app update is already running, resume the update.
                appUpdateManager.startUpdateFlowForResult(appUpdateInfo, updateLauncher, AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build());
            }
        });
    }



}
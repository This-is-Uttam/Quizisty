package com.reward.quizisty.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;

import androidx.lifecycle.LiveData;

public class GPSReceiver extends LiveData<Boolean> {
  /*  @Override
    protected void onActive() {
        registerReceiver();
        checkGpsStatus();
    }

    @Override
    protected void onInactive() {
        unregisterReceiver();
    }
*/
    Context context;


    public GPSReceiver(Context context) {
        this.context = context;
    }

    private BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkGpsStatus();
        }
    };

    private void checkGpsStatus() {
        if (isLocationOn()) {
            postValue(true);
        } else postValue(false);
    }

    private boolean isLocationOn() {
        return ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE))
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void registerReceiver() {
        context.registerReceiver(gpsReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
    }

    public void unregisterReceiver() {
        context.unregisterReceiver(gpsReceiver);
    }

}

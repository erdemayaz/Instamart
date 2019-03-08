package com.ayaz.instamart.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

import com.ayaz.instamart.marttool.Constant;
import com.ayaz.instamart.sdk.Preferences;

import java.util.Date;

public class LocationService extends Service {

    LocationListener locationListener;
    LocationManager locationManager;
    Date date = new Date();

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                date.setTime(System.currentTimeMillis());
                Preferences.saveLastLocationTime(getApplicationContext(), date);
                Intent intentChanged = new Intent(Constant.LOCATION_UPDATED);
                intentChanged.putExtra(Constant.LONGITUDE, location.getLongitude());
                intentChanged.putExtra(Constant.LATITUDE, location.getLatitude());
                sendBroadcast(intentChanged);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 0, locationListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null){
            locationManager.removeUpdates(locationListener);
        }
    }
}

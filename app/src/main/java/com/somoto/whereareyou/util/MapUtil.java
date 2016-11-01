package com.somoto.whereareyou.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

public class MapUtil {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 3;

    public static Location getLastBestLocation(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            return null;
        }

        LocationManager mLocationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        Location locationGPS = null;
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        Location locationNet=null;
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            // No network provider is enabled
        } else {

            if (isNetworkEnabled) {
               /* mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);*/
                Log.d("Network", "Network");

                if (mLocationManager != null) {
                    locationNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }

            if (isGPSEnabled) {
                if (locationNet == null) {
                    /* mLocationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);*/
                    Log.d("GPS Enabled", "GPS Enabled");

                    if (mLocationManager != null) {
                        locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }

                }
            }
        }

        long GPSLocationTime = 0;
        if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if ( 0 < GPSLocationTime - NetLocationTime ) {
            return locationGPS;
        }
        else {
            return locationNet;
        }
    }

}

package com.somoto.whereareyou.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.LocationManager;
import android.location.Location;
import android.content.Context;
import android.widget.Toast;

import com.somoto.whereareyou.R;
import com.somoto.whereareyou.internet.Internet;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabClicked();
            }
        });
    }

    private void fabClicked(){
        Random rand = new Random();
        long randLong = rand.nextLong();
        String link = Internet.HOST+"/"+randLong;
        String message = getString(R.string.message);
        String fullMessage = message+" "+link;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, fullMessage);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    /**
     * @return the last know best location
     */
    public Location getLastBestLocation() {

        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            /*ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    'MY_PERMISSION_ACCESS_COURSE_LOCATION' );*/
        }


        LocationManager  mLocationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
// Getting GPS status
        Location locationGPS = null;
        boolean isGPSEnabled = mLocationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // Getting network status
        Location locationNet=null;
        boolean isNetworkEnabled = mLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we
     * just add a marker near Africa.
     */
    @Override
    public void onMapReady(GoogleMap map) {

        Location location = getLastBestLocation();

        if (location != null) {
            // Add a marker in Sydney and move the camera
            LatLng current_location = new LatLng(location.getLatitude(),  location.getLongitude());
            map.addMarker(new MarkerOptions().position(current_location).title("Current location"));
            map.moveCamera(CameraUpdateFactory.newLatLng(current_location));
        }


    }
}

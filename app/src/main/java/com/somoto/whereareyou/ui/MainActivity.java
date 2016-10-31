package com.somoto.whereareyou.ui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Location;
import com.somoto.whereareyou.R;
import com.somoto.whereareyou.internet.HttpResponse;
import com.somoto.whereareyou.internet.Internet;
import com.somoto.whereareyou.internet.InternetDataListener;
import com.somoto.whereareyou.internet.PostDataTask;
import com.somoto.whereareyou.util.MapUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
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
        final int umid = rand.nextInt(9999);
        sendInvitation(umid);
        Map<String,String> map = new HashMap<>();
        map.put("umid", ""+umid);
        new PostDataTask(new InternetDataListener<HttpResponse>() {
            @Override
            public void handleData(HttpResponse data) {
                if(data.statusCode!=200){
                    Snackbar.make(toolbar, data.error, Snackbar.LENGTH_LONG).show();
                }
                else {
                    sendInvitation(umid);
                }
            }
        }, "Users", map).execute();
    }
    private void sendInvitation(int umid){
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String link = Internet.HOST+"/share_loc.html?umid="+umid+"&androidid="+android_id;
        String message = getString(R.string.message);
        String fullMessage = message+" "+link;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, fullMessage);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
        //SharedPrefs.addUmid(context, ""+umid);
    }

    @Override
    public void onMapReady(GoogleMap map) {

        Location location = MapUtil.getLastBestLocation(this);

        if (location != null) {
            LatLng current_location = new LatLng(location.getLatitude(),  location.getLongitude());
            map.addMarker(new MarkerOptions().position(current_location).title("Current location"));
            map.moveCamera(CameraUpdateFactory.newLatLng(current_location));
        }


    }
}

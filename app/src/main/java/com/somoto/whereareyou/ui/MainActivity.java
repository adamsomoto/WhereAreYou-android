package com.somoto.whereareyou.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Location;
import com.somoto.whereareyou.R;
import com.somoto.whereareyou.internet.GetDataTask;
import com.somoto.whereareyou.internet.HttpResponse;
import com.somoto.whereareyou.internet.Internet;
import com.somoto.whereareyou.internet.InternetDataListener;
import com.somoto.whereareyou.internet.PostDataTask;
import com.somoto.whereareyou.util.MapUtil;
import com.somoto.whereareyou.util.MyJsonParser;
import com.somoto.whereareyou.util.MyLog;
import com.somoto.whereareyou.util.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    private Toolbar toolbar;
    private GoogleMap map;
    private boolean isResumed;
    private boolean isCentered;

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

    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;
        refresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResumed = false;
    }

    private void refresh(){
        MyLog.i("refresh");
        new GetDataTask(new InternetDataListener<String>() {
            @Override
            public void handleData(String data) {
                handleResponse(data);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(isResumed) {
                            refresh();
                        }
                    }
                }, 10000);
            }
        }).execute(Internet.USERS);
    }

    private void handleResponse(String data){
        if(data==null){
            Snackbar.make(toolbar, "Internet problem", Snackbar.LENGTH_LONG).show();
        }
        try {
            if(map==null){
                return;
            }
            map.clear();
            List<User> list = MyJsonParser.parseJsonArray(data, User.class);
            for(User iter : list) {
                if(iter.latitude==null || iter.longitude==null){
                    continue;
                }
                LatLng latLng = new LatLng(Double.parseDouble(iter.latitude), Double.parseDouble(iter.longitude));
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(iter.umid);
                map.addMarker(markerOptions);
            }
            addMyMarker();
        }
        catch (Exception e){
            MyLog.e(e);
        }
    }

    private void addMyMarker(){
        Location location = MapUtil.getLastBestLocation(this);
        if (location != null) {
            LatLng current_location = new LatLng(location.getLatitude(),  location.getLongitude());
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(android.R.drawable.btn_star_big_on);
            map.addMarker(new MarkerOptions().position(current_location).title("Current location").icon(icon));
            if(!isCentered) {
                map.moveCamera(CameraUpdateFactory.newLatLng(current_location));
                isCentered = true;
            }
        }
    }

    private void fabClicked(){
        Random rand = new Random();
        final int umid = rand.nextInt(100000);
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
        this.map = map;
        map.moveCamera(CameraUpdateFactory.zoomTo(15));
        addMyMarker();
    }

}

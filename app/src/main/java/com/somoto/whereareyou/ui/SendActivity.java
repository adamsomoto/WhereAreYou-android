package com.somoto.whereareyou.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.somoto.whereareyou.R;
import com.somoto.whereareyou.internet.GetDataTask;
import com.somoto.whereareyou.internet.HttpResponse;
import com.somoto.whereareyou.internet.Internet;
import com.somoto.whereareyou.internet.InternetDataListener;
import com.somoto.whereareyou.internet.PostDataTask;
import com.somoto.whereareyou.util.MyJsonParser;
import com.somoto.whereareyou.util.MyLog;
import com.somoto.whereareyou.util.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SendActivity extends AppCompatActivity {

    private ListView listView;
    private UserAdapter adapter;
    private boolean isResumed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new UserAdapter(this);
        listView.setAdapter(adapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
            Snackbar.make(listView, "Internet problem", Snackbar.LENGTH_LONG).show();
        }
        try {
            List<User> list = MyJsonParser.parseJsonArray(data, User.class);
            adapter.clear();
            adapter.addAll(list);
        }
        catch (Exception e){
            MyLog.e(e);
        }
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
                    Snackbar.make(listView, data.error, Snackbar.LENGTH_LONG).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
}

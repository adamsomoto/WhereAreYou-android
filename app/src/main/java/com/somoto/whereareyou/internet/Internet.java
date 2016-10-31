package com.somoto.whereareyou.internet;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import com.somoto.whereareyou.R;

import java.util.Random;

public class Internet {

    public static final String HOST = "https://whereareyou-148008.appspot.com";

    public static void invite(Context context){
        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Random rand = new Random();
        int umid = rand.nextInt(9999);
        String link = Internet.HOST+"/share_loc.html?umid="+umid+"&androidid="+android_id;
        String message = context.getString(R.string.message);
        String fullMessage = message+" "+link;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, fullMessage);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
        //TODO write the umid in SharedPrefs
    }

}

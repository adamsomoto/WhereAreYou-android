package com.somoto.whereareyou.util;

import android.util.Log;

public class MyLog {

    public static String TAG = "MyLog";

    public static void i(String msg){
        try {
            Log.i(TAG, msg);
        }
        catch (Exception e){
        }
    }

    public static int e(Exception e){
        if(e==null || e.getMessage()==null){
            return Log.e(TAG, "Exception null");
        }
        else{
            return Log.e(TAG, e.getMessage());
        }
    }

}

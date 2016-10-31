package com.somoto.whereareyou.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

public class SharedPrefs {

    private static final String UMID = "umid";

    public static Set<String> getUmidSet(Context context){
        return getSharedPrefs(context).getStringSet(UMID, new HashSet<String>());
    }

    public static void addUmid(Context context, String s){
        Set<String> set = getUmidSet(context);
        set.add(s);
        setUmidSet(context, set);
    }

    private static void setUmidSet(Context context, Set<String> set){
        getSharedPrefs(context).edit().putStringSet(UMID, set).commit();
    }

    private static SharedPreferences getSharedPrefs(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}
package com.somoto.whereareyou.internet;

import com.somoto.whereareyou.util.MyLog;

import org.apache.commons.io.IOUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Internet {

    public static final String HOST = "https://whereareyou-148008.appspot.com";
    public static final String USERS = "Users";
    private static final String UTF8 = "UTF-8";

    public static String httpGET(String page) {
        try {
            URL url = new URL(HOST + "/" + URLEncoder.encode(page, UTF8));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn.getResponseCode() != 200) {
                return null;
            }
            String response = IOUtils.toString(conn.getInputStream(), UTF8);
            return response;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static HttpResponse httpPOST(String table, String body) {
        try {
            URL url = new URL(HOST + "/" + table);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            IOUtils.write(body.getBytes(), conn.getOutputStream());
            return new HttpResponse(conn);
        } catch (Exception e) {
            MyLog.e(e);
            return new HttpResponse(400, e.getMessage());
        }
    }

}

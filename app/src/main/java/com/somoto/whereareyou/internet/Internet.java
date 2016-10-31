package com.somoto.whereareyou.internet;

import com.somoto.whereareyou.util.MyLog;

import org.apache.commons.io.IOUtils;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

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

    public static HttpResponse httpPOST(String table, Map<String, String> postDataParams) {
        try {
            URL url = new URL(HOST + "/" + table);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            IOUtils.write(getPostDataString(postDataParams), conn.getOutputStream());
            return new HttpResponse(conn);
        } catch (Exception e) {
            MyLog.e(e);
            return new HttpResponse(400, e.getMessage());
        }
    }

    private static String getPostDataString(Map<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), UTF8));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), UTF8));
        }

        return result.toString();
    }

}

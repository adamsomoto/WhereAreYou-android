package com.somoto.whereareyou.internet;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;

public class HttpResponse {

    public int statusCode;
    public String body;
    public String error;

    public HttpResponse(HttpURLConnection conn) throws IOException {
        statusCode = conn.getResponseCode();
        if(statusCode==200){
            body = IOUtils.toString(conn.getInputStream());
        }
        else{
            error = IOUtils.toString(conn.getErrorStream());
        }
    }

    HttpResponse(int statusCode, String body){
        this.statusCode = statusCode;
        this.body = body;
    }


}
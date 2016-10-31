package com.somoto.whereareyou.internet;

import android.os.AsyncTask;

import java.util.Map;

public class PostDataTask extends AsyncTask<String, Integer, HttpResponse> {

    private InternetDataListener<HttpResponse> internetDataListener;
    private String table;
    private Map<String, String> postDataParams;

    public PostDataTask(InternetDataListener<HttpResponse> internetDataListener, String table, Map<String, String> postDataParams){
        this.internetDataListener = internetDataListener;
        this.table = table;
        this.postDataParams = postDataParams;
    }

    @Override
    protected HttpResponse doInBackground(String... dummy) {
        return Internet.httpPOST(table, postDataParams);
    }

    protected void onProgressUpdate(Integer... progress) {
    }

    @Override
    protected void onPostExecute(HttpResponse b) {
        internetDataListener.handleData(b);
    }

}
package com.somoto.whereareyou.internet;

import android.os.AsyncTask;

public class PostDataTask extends AsyncTask<String, Integer, HttpResponse> {

    private InternetDataListener<HttpResponse> internetDataListener;
    private String table;
    private String body;

    public PostDataTask(InternetDataListener<HttpResponse> internetDataListener, String table, String body){
        this.internetDataListener = internetDataListener;
        this.table = table;
        this.body = body;
    }

    @Override
    protected HttpResponse doInBackground(String... dummy) {
        return Internet.httpPOST(table, body);
    }

    protected void onProgressUpdate(Integer... progress) {
    }

    @Override
    protected void onPostExecute(HttpResponse b) {
        internetDataListener.handleData(b);
    }

}
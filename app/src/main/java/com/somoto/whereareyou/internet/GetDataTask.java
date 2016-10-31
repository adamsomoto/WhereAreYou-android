package com.somoto.whereareyou.internet;

import android.os.AsyncTask;

public class GetDataTask extends AsyncTask<String, Integer, String> {

    private InternetDataListener<String> internetDataListener;

    public GetDataTask(InternetDataListener internetDataListener){
        this.internetDataListener = internetDataListener;
    }

    @Override
    protected String doInBackground(String... pages) {
        return Internet.httpGET(pages[0]);
    }

    protected void onProgressUpdate(Integer... progress) {
    }

    @Override
    protected void onPostExecute(String s) {
        internetDataListener.handleData(s);
    }

}
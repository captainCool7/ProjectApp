package com.captaincool.projectapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground(String... strings) {
        URL url;
        String result = "";
        HttpURLConnection connection;
        try {
            url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            InputStream in = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();
            while (data != -1) {
                char current = (char) data;
                result += current;
                data = reader.read();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("res", "error2");
            return null;
        }
    }
}

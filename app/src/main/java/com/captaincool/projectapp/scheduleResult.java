package com.captaincool.projectapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseUser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class scheduleResult extends AppCompatActivity {
    final static String TAG = "myapp";
    String orderid, custid;
    TextView resultView;
    public static final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static Random RANDOM = new Random();
    public class DownloadTask extends AsyncTask<String, Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
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
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i(TAG,"Result is "+s);
            resultView.setText(s);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_result);
        resultView = findViewById(R.id.resultView);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        final int price = intent.getIntExtra("price",1100);
        final int id = intent.getIntExtra("id",1001);
        Log.i(TAG, "Url is " + url);
        orderid = randomString(9);
//        Log.i(TAG,"Url is "+ intent.getStringExtra("url"));
        try {
            DownloadTask task = new DownloadTask();
            String returnResult = String.valueOf(task.execute(url));
        } catch (Exception e) {
            Log.i(TAG, "Error is " + e);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Button btn = (Button) findViewById(R.id.start_transaction);
        final ParseUser user = ParseUser.getCurrentUser();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(scheduleResult.this, checksum.class);
                intent.putExtra("orderid",""+orderid);
                intent.putExtra("custid",user.toString());
                intent.putExtra("price",price);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        if (ContextCompat.checkSelfPermission(scheduleResult.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(scheduleResult.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }
    }
    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }
        return sb.toString();
    }
}


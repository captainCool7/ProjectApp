package com.captaincool.projectapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;

public class PNRStatus extends AppCompatActivity {
    EditText editText;
    TextView textView2,textView4,textView6,textView8,textView10,textView12,textView14,textView16,textView18;
    ProgressDialog progress;
    public class DownloadTask extends AsyncTask<String, Void,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            String result="";
            URL url;
            HttpURLConnection urlConnection = null;
            try{
                url = new URL(strings[0]);
                urlConnection =(HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1)
                {
                    char current = (char) data;
                    result +=current;
                    data = reader.read();
                }
                Log.i("done",result);
                return result;
            }catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                JSONObject jsonObject = new JSONObject(s);
                String pnrNumber = jsonObject.getString("PnrNumber");
                String trainNumber = jsonObject.getString("TrainNumber");
                String trainName = jsonObject.getString("TrainName");
                String chartPrepared = jsonObject.getString("ChatPrepared");
                String journeyClass = jsonObject.getString("JourneyClass");
                String from = jsonObject.getString("From");
                String to = jsonObject.getString("To");
                String journeyDate = jsonObject.getString("JourneyDate");
                String Passanger = jsonObject.getString("Passangers");
                JSONArray arr = new JSONArray(Passanger);
//                for(int i = 0;i<arr.length();i++)
//                {
//                    JSONObject jsonPart = arr.getJSONObject(i);
//                    Log.i("done","Station Code is "+jsonPart.getString("StationCode"));
//                    Log.i("done","Departure time was "+jsonPart.getString("ScheduleDeparture"));
//                    value += jsonPart.getString("StationName") +"       "+jsonPart.getString("ScheduleDeparture")+"\n";
//                }
                String noOfPassangers = String.valueOf(arr.length());
                progress.dismiss();
                textView2.setText(pnrNumber);
                textView4.setText(trainNumber);
                textView6.setText(trainName);
                textView8.setText(chartPrepared);
                textView10.setText(journeyClass);
                textView12.setText(from);
                textView14.setText(to);
                textView16.setText(journeyDate);
                textView18.setText(noOfPassangers);
            }catch (Exception e)
            {
                Log.i("done","Error is "+e);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnrstatus);
        editText = findViewById(R.id.editText);
        textView2 = findViewById(R.id.textView2);
        textView4 = findViewById(R.id.textView4);
        textView6 = findViewById(R.id.textView6);
        textView8 = findViewById(R.id.textView8);
        textView10 = findViewById(R.id.textView10);
        textView12 = findViewById(R.id.textView12);
        textView14 = findViewById(R.id.textView14);
        textView16 = findViewById(R.id.textView16);
        textView18 = findViewById(R.id.textView18);
    }
    public void getPnrDetail(View view) {
        try {
            DownloadTask task = new DownloadTask();
            String encodedNum = URLEncoder.encode(editText.getText().toString(), "UTF-8");
            Log.i("done","pnr number is "+encodedNum);
            task.execute("https ://indianrailapi.com/api/v2/PNRCheck/apikey/a57fdcb882fc75db813393b6ccb9966f/PNRNumber/"+ encodedNum +"/Route/1/");
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            progress = new ProgressDialog(this);
            progress.setMessage("Loading.....");
            progress.show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("done","Error is "+e);
            Toast.makeText(getApplicationContext(), "Could  not able to get detail :(", Toast.LENGTH_SHORT).show();
        }
    }

}

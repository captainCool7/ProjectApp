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
import android.widget.ProgressBar;
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

public class LiveTrainSatus extends AppCompatActivity {
    EditText editText;
    TextView dateText,sDate,Train,cStatus;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    String value = "";
    String dateValue ;
    ProgressDialog progressDialog;
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
                String detail = jsonObject.getString("TrainRoute");
                String CurrentStatusObject = jsonObject.getString("CurrentStation");
                JSONObject CurrentStatus = new JSONObject(CurrentStatusObject);
                String StationName = CurrentStatus.getString("StationName");
                Log.i("done",StationName);
                String TrainNumber = jsonObject.getString("TrainNumber");
                Log.i("done",TrainNumber);
                JSONArray arr = new JSONArray(detail);
                for(int i = 0;i<arr.length();i++)
                {
                    JSONObject jsonPart = arr.getJSONObject(i);
                    Log.i("done","Station Code is "+jsonPart.getString("StationCode"));
                    Log.i("done","Departure time was "+jsonPart.getString("ScheduleDeparture"));
                    value += jsonPart.getString("StationName") +"       "+jsonPart.getString("ScheduleDeparture")+"\n";
                }
                progressDialog.dismiss();
                sDate.setText("TrainNumber "+TrainNumber);
                cStatus.setText("Current Station "+StationName);
                Train.setText(value);
                sDate.setVisibility(View.VISIBLE);
                cStatus.setVisibility(View.VISIBLE);
                Train.setVisibility(View.VISIBLE);
            }catch (Exception e)
            {
                Log.i("done","Error is "+e);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_train_satus);
        editText = findViewById(R.id.editText);
        dateText = findViewById(R.id.dateText);
        sDate = findViewById(R.id.sDate);
        cStatus = findViewById(R.id.cStatus);
        Train = findViewById(R.id.Train);
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        LiveTrainSatus.this,
                        mDateSetListener,
                        year,month,day
                );
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1 += 1;
                String month = String.format("%02d",i1);
                String day = String.format("%02d",i2);
                dateValue = String.valueOf(i)+""+ month +""+day;
                dateText.setText(i +"/"+ month +"/"+day);
                Log.i("done","date is :" +dateText.getText()+ editText.getText().toString());
            }
        };
    }

    public void getDetail(View view) {
        try {
            DownloadTask task = new DownloadTask();
            String encodedNum = URLEncoder.encode(editText.getText().toString(), "UTF-8");
            task.execute("https://indianrailapi.com/api/v2/livetrainstatus/apikey/a57fdcb882fc75db813393b6ccb9966f/trainnumber/"+ encodedNum +"/date/"+ dateValue +"/");
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading......");
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could  not able to get Detail :(", Toast.LENGTH_SHORT).show();
        }
    }

}

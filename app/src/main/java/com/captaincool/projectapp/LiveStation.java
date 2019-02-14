package com.captaincool.projectapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class LiveStation extends AppCompatActivity {
    ProgressDialog progressDialog;
    EditText editText;
    TextView Train,arrival,dep;
    ListView listView;
    ArrayList<String> nameList,arrivalList,depList;
    ArrayAdapter<String> arrayAdapter;

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
                Log.i("done",result);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                JSONObject jsonObject = new JSONObject(s);
                String detail = jsonObject.getString("Trains");
                JSONArray arr = new JSONArray(detail);
                for(int i = 0;i<arr.length();i++)
                {
                    JSONObject jsonPart = arr.getJSONObject(i);
                    String Tname = jsonPart.getString("Name");
                    String arrTime = jsonPart.getString("ScheduleArrival");
                    String depTime = jsonPart.getString("ScheduleDeparture");
                    Log.i("done","Station Code is "+Tname);
                    Log.i("done","Departure time was "+jsonPart.getString("ScheduleDeparture"));
                    nameList.add(Tname);
                    arrivalList.add(arrTime);
                    depList.add(depTime);
                }
                progressDialog.dismiss();
                listView.setVisibility(View.VISIBLE);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Train.setText(nameList.get(i));
                        arrival.setText(arrivalList.get(i));
                        dep.setText(depList.get(i));
                    }
                });
                dep.setVisibility(View.VISIBLE);
                arrival.setVisibility(View.VISIBLE);
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
        setContentView(R.layout.activity3_live_station);
        arrival = findViewById(R.id.arrival);
        editText = findViewById(R.id.editText);
        dep= findViewById(R.id.dep);
        listView = findViewById(R.id.listView);
        nameList = new ArrayList<String>();
        arrivalList = new ArrayList<String>();
        depList = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,nameList);
        listView.setAdapter(arrayAdapter);
    }

    public void getStationDetail(View v)
    {
        try {
            LiveStation.DownloadTask task = new LiveStation.DownloadTask();
            String encodedName = URLEncoder.encode(editText.getText().toString(), "UTF-8");
            task.execute("https://indianrailapi.com/api/v2/LiveStation/apikey/a57fdcb882fc75db813393b6ccb9966f/StationCode/"+ encodedName +"/hours/4/");
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading......");
            progressDialog.show();
        } catch (Exception e) {
            Log.i("done","Error is "+e);
            Toast.makeText(getApplicationContext(), "Could  not able to get Detail :(", Toast.LENGTH_SHORT).show();
        }
    }
}

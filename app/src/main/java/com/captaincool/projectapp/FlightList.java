package com.captaincool.projectapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FlightList extends AppCompatActivity {

    final String TAG = "Rapp";
    int arrayLength;
    ArrayList<String> originArray = new ArrayList<>();
    ArrayList<String> desArray = new ArrayList<>();
    ArrayList<String> stopsArray = new ArrayList<>();
    ArrayList<String> depArray = new ArrayList<>();
    ArrayList<Integer> priceArray = new ArrayList<>();
    ListView listView;
    ProgressDialog progress;

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return arrayLength;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View cview = getLayoutInflater().inflate(R.layout.flight_layout,null);
            TextView src = cview.findViewById(R.id.src1);
            TextView depTime = cview.findViewById(R.id.depTime);
            TextView Des = cview.findViewById(R.id.Des);
            TextView stops = cview.findViewById(R.id.stops);
            TextView price = cview.findViewById(R.id.price);
            src.setText(originArray.get(i));
            depTime.setText(depArray.get(i));
            Des.setText(desArray.get(i));
            stops.setText(stopsArray.get(i));
            price.setText(priceArray.get(i).toString());
            return cview;
        }
    }

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
                JSONObject dataObject = jsonObject.getJSONObject("data");
                JSONArray jsonArray = dataObject.getJSONArray("onwardflights");
                Log.i(TAG,"Array is "+jsonArray);
                arrayLength = jsonArray.length();
                Log.i(TAG,"Array Length is"+arrayLength);
                for(int i=0;i<arrayLength;i++)
                {
                    JSONObject jsonPart = jsonArray.getJSONObject(i);
                    String objectOrigin = jsonPart.getString("origin");
                    String objectDep = jsonPart.getString("deptime");
                    String objectDes = jsonPart.getString("destination");
                    String objectStops = jsonPart.getString("stops");
                    JSONObject fareObject = jsonPart.getJSONObject("fare");
                    Log.i(TAG,"fare is" + fareObject);
                    int objectFare = fareObject.getInt("grossamount");
                    originArray.add(objectOrigin);
                    depArray.add(objectDep);
                    desArray.add(objectDes);
                    stopsArray.add(objectStops);
                    priceArray.add(objectFare);
                }
                progress.dismiss();
            }catch (Exception e)
            {
                Log.i("done","Error is "+e);
                e.printStackTrace();
            }
            Log.i(TAG,"Working 2");
            setArrayAdapter();
        }
    }

    private void setArrayAdapter() {
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int fare = priceArray.get(i);
                Toast.makeText(FlightList.this,"Fare is "+fare,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_list);
        listView= findViewById(R.id.flistView);
        Intent intent = getIntent();
        String url =intent.getStringExtra("url");
        DownloadTask task = new DownloadTask();
        Log.i(TAG,"Working 1");
        task.execute(url);
        progress = new ProgressDialog(this);
        progress.setMessage("Loading.....");
        progress.show();
        Log.i(TAG,"Working 3");
    }
}

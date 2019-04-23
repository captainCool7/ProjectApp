package com.captaincool.projectapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class trainSchedule extends AppCompatActivity {
    EditText trainNo;
    Button mButton;
    ListView listView;
    ProgressDialog progress;
    final String TAG = "Rapp";
    String data;
    int arrayLength;
    ArrayList<String> noArray = new ArrayList<>();
    ArrayList<String> nameArray = new ArrayList<>();
    ArrayList<String> distanceArray = new ArrayList<>();

    class CustomAdapter extends BaseAdapter
    {

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
            View cview = getLayoutInflater().inflate(R.layout.seat_layout,null);
            TextView dateView = cview.findViewById(R.id.dateView);
            TextView avaView = cview.findViewById(R.id.avaView);
            TextView confirmView = cview.findViewById(R.id.confirmView);
            dateView.setText(noArray.get(i));
            avaView.setText(nameArray.get(i));
            confirmView.setText(distanceArray.get(i));
            return cview;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_schedule);
        trainNo = findViewById(R.id.trainNo);
        mButton = findViewById(R.id.searchButton);
        listView = findViewById(R.id.scheduleList);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataSearch();
            }
        });
        noArray.add("Sr NO");
        nameArray.add("Name");
        distanceArray.add("Distance");
    }

    private void dataSearch() {
        String url;
        if(TextUtils.isEmpty(trainNo.getText()))
        {
            trainNo.setError("No is required");
        }
        else
        {
            progress = new ProgressDialog(this);
            progress.setMessage("Loading.....");
            progress.show();
            Log.i(TAG, "Here");
            DownloadTask task = new DownloadTask();
            url = "http://indianrailapi.com/api/v2/TrainSchedule/apikey/a57fdcb882fc75db813393b6ccb9966f/TrainNumber/"+ trainNo.getText() +"/";
            Log.i(TAG,"Url is "+url);
            try {
                data  = task.execute(url).get();
                toMvc(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void toMvc(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray RouteArray = jsonObject.getJSONArray("Route");
            Log.i(TAG,"Array is "+RouteArray);
            arrayLength = RouteArray.length();
            for (int i = 0 ;i<arrayLength;i++)
            {
                JSONObject jsonPart = RouteArray.getJSONObject(i);
                String aNo = jsonPart.getString("SerialNo");
                String aName = jsonPart.getString("StationName");
                String aDistance = jsonPart.getString("Distance");
                noArray.add(aNo);
                nameArray.add(aName);
                distanceArray.add(aDistance);
            }
            setArrayAdapter();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setArrayAdapter() {
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
        progress.dismiss();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String fare= distanceArray.get(i);
                Toast.makeText(trainSchedule.this,"Fare is "+fare,Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.captaincool.projectapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class TrainFair extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText trainNo,sFrom,sTo;
    Spinner quota;
    ListView fareList;
    Button searchButton;
    String item="GN";
    ProgressDialog progress;
    String TAG="Rapp";
    int arrayLength;
    ArrayList<String> nameArray = new ArrayList<>();
    ArrayList<String> fareArray = new ArrayList<>();

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
            View cview = getLayoutInflater().inflate(R.layout.train_fair_layout,null);
            TextView nameView = cview.findViewById(R.id.fairName);
            TextView fareView = cview.findViewById(R.id.FairPrice);
            nameView.setText(nameArray.get(i));
            fareView.setText(fareArray.get(i));
            return cview;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_fair);
        trainNo = findViewById(R.id.trainNo);
        sFrom = findViewById(R.id.sFrom);
        sTo = findViewById(R.id.sTo);
        quota = findViewById(R.id.quota);
        fareList = findViewById(R.id.fareList);
        searchButton = findViewById(R.id.searchButton);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.fair_quota,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        quota.setOnItemSelectedListener(this);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fareResult();
            }
        });
    }

    private void fareResult() {
        String url = null;
        String data;
        if(TextUtils.isEmpty(trainNo.getText()))
        {
            trainNo.setError("No is required");
        }
        else if(TextUtils.isEmpty(sFrom.getText()))
        {
            sFrom.setError("Please choose the source");
        }
        else if(TextUtils.isEmpty(sTo.getText()))
        {
            sTo.setError("Please choose the destination");
        }
        else
        {
            progress = new ProgressDialog(this);
            progress.setMessage("Loading.....");
            progress.show();
            Log.i(TAG, "Here");
            DownloadTask task = new DownloadTask();
            url = "https://indianrailapi.com/api/v2/TrainFare/apikey/a57fdcb882fc75db813393b6ccb9966f/TrainNumber/"+trainNo.getText()+
                    "/From/"+sFrom.getText() +"/To/"+ sTo.getText() +"/Quota/"+item;
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
            JSONArray FareArray = jsonObject.getJSONArray("Fares");
            Log.i(TAG,"Array is "+FareArray);
            arrayLength = FareArray.length();
            for (int i = 0 ;i<arrayLength;i++)
            {
                JSONObject jsonPart = FareArray.getJSONObject(i);
                String aName = jsonPart.getString("Name");
                String aFare = jsonPart.getString("Fare");
                nameArray.add(aName);
                fareArray.add("₹"+aFare);
            }
            setArrayAdapter();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setArrayAdapter() {
        CustomAdapter customAdapter = new CustomAdapter();
        fareList.setAdapter(customAdapter);
        progress.dismiss();
        fareList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String fare= fareArray.get(i);
                Toast.makeText(TrainFair.this,"Fare is "+fare,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        item = String.valueOf(adapterView.getItemAtPosition(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}

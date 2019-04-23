package com.captaincool.projectapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;

public class planPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button b;
    final static String TAG = "Rapp";
    Spinner mSpinner;
    ArrayList<Integer> list = new ArrayList<>();
    int days=0, selectedDay;
    String url,returnResult ;
    TextView time;
    Date uDate;
    ParseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_page);

        mSpinner = findViewById(R.id.spinner);
        time = findViewById(R.id.time);
        b = findViewById(R.id.b);
        user = ParseUser.getCurrentUser();
        uDate = user.getDate("pkgDate");
//        String code = String.valueOf(user.getNumber("pkgCode"));
            String code = "1001";
        Log.i(TAG, "Code is " + code);
            getNoDays(code);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityPage();
                }
            });
    }

    private void getNoDays(String code) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Places");
        query.whereEqualTo("id",code);
        Log.i(TAG,"Working1");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
//                days = Integer.parseInt(String.valueOf(object.getNumber("Days")));
//                url = object.getString("url");
//                Log.i(TAG," "+object.getInt("Days"));
                Log.i(TAG," "+url);
                setUpSpinner();
            }
        });
    }

    private void setUpSpinner() {
        Log.i(TAG,"Working3");
        for(int j=1;j<=3;j++)
        {
            list.add(j);
            Log.i(TAG,"Successfully Added "+j);
        }
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mSpinner.setOnItemSelectedListener(planPage.this);
        mSpinner.setAdapter(adapter);
        try {
            DownloadTask task = new DownloadTask();
            returnResult = task.execute("https://api.myjson.com/bins/ckw58").get();
            Log.i(TAG,  returnResult);
        } catch (Exception e) {
            Log.i(TAG, "Error is " + e);
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i(TAG,"value selected is "+adapterView.getItemAtPosition(i));
        selectedDay =(Integer) adapterView.getItemAtPosition(i);
        Log.i(TAG, String.valueOf(selectedDay));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        selectedDay = 1;
    }

    public void ActivityPage()
    {
        try {
            Log.i(TAG, "here");
//            Intent i = new Intent(getApplicationContext(), detailedPage.class);
//            i.putExtra("value", selectedDay);
//            i.putExtra("data", returnResult);
//            startActivity(i);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.i(TAG, "Error is " + e);
        }
    }
}

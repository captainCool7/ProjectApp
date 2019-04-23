package com.captaincool.projectapp;

import android.content.Intent;
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

import java.util.ArrayList;

public class detailedPage extends AppCompatActivity {

    TextView selectedDateView;
    String data;
    int selectedDay,aDay;
    int arrayLength;
    ListView listView;
    ArrayList<String> titleArray = new ArrayList<>();
    ArrayList<String> descriptionArray = new ArrayList<>();
    ArrayList<Integer> hourArray = new ArrayList<>();
    ArrayList<Integer> minArray = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_page);
        listView = findViewById(R.id.planList);
        selectedDateView= findViewById(R.id.selectedDayViiew);
        Intent i = getIntent();
        data = i.getStringExtra("data");
        selectedDay = i.getIntExtra("value",1);
        selectedDateView.setText(String.valueOf(selectedDay));
        aDay = selectedDay-1;
        setPlanMVC(data);
    }

    private void setPlanMVC(String data) {
        try
        {
            JSONObject object = new JSONObject(data);
            Log.i("Rapp",""+object);
            JSONArray scheduleArray = object.getJSONArray("Schedule");
            arrayLength = scheduleArray.length();
            Log.i("Rapp",""+arrayLength);
            for(int i =0 ;i<scheduleArray.length();i++)
            {
                JSONObject ObjectPart = scheduleArray.getJSONObject(i);
                int objectDay = ObjectPart.getInt("Day");
                Log.i("Rapp",""+aDay);
                if(objectDay == aDay)
                {
                    String oTitle = ObjectPart.getString("Title");
                    String oDesc = ObjectPart.getString("Description");
                    int oHour = ObjectPart.getInt("Hour");
                    int oMin = ObjectPart.getInt("Minute");
                    Log.i("Rapp",""+i);
                    titleArray.add(oTitle);
                    descriptionArray.add(oDesc);
                    hourArray.add(oHour);
                    minArray.add(oMin);
                }else
                {
                    Log.i("Rapp","Switching to the next");
                    Log.i("Rapp",""+i);
                }
            }
            setArrayAdapter();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setArrayAdapter() {
        CustomAdapter customAdapter =new  CustomAdapter();
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int fare = hourArray.get(i);
                Toast.makeText(detailedPage.this,"Fare is "+fare,Toast.LENGTH_SHORT).show();
            }
        });
    }

    class CustomAdapter extends BaseAdapter {

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
            View cview = getLayoutInflater().inflate(R.layout.plan_page_layout,null);
            TextView vtitle = cview.findViewById(R.id.titleView);
            TextView vdes = cview.findViewById(R.id.descView);
            TextView vtime = cview.findViewById(R.id.timeView);
            vtitle.setText(titleArray.get(i));
            vdes.setText(descriptionArray.get(i));
            vtime.setText(String.valueOf(hourArray.get(i))+": "+String.valueOf(minArray.get(i)));
            return cview;
        }
    }
}

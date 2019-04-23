package com.captaincool.projectapp;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SeatAvailability extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String item = "SL";
    String TAG="Rapp";
    String y,m,dateOfMonth,data;
    EditText trainNo,from,to;
    Button selectDate,searchButton;
    Calendar c;
    Date d;
    DatePickerDialog dpd;
    int arrayLength;
    ListView listView;
    ArrayList<String> dateArray = new ArrayList<>();
    ArrayList<String> availibiltyArray = new ArrayList<>();
    ArrayList<String> confirmArray = new ArrayList<>();
    ProgressDialog progress;

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
            dateView.setText(dateArray.get(i));
            avaView.setText(availibiltyArray.get(i));
            confirmView.setText(confirmArray.get(i));
            return cview;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_availability);
        Spinner spinner = findViewById(R.id.spinner);
        trainNo = findViewById(R.id.trainNo);
        from  = findViewById(R.id.from);
        to = findViewById(R.id.to);
        selectDate = findViewById(R.id.selectDate);
        searchButton= findViewById(R.id.seatSearch);
        listView = findViewById(R.id.seatList);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.seat_class,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(this);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchResult();
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
    public void selectingDate(View v)
    {
        c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DAY_OF_MONTH);
        dpd = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                d = new Date();
                d.setYear(i);
                d.setMonth(i1+1);
                d.setDate(i2);
            }
        },year,month,date);
        Log.i(TAG,"Date is "+d);
        dpd.show();
    }

    private void searchResult()
    {
        if(d != null)
        {
            y = String.valueOf(d.getYear());
            m = String.format("%02d",d.getMonth());
            dateOfMonth = String.format("%02d",d.getDate());}
        Log.i(TAG,y+""+m+""+dateOfMonth);

        String url = null;
        if(TextUtils.isEmpty(trainNo.getText()))
        {
            trainNo.setError("No is required");
        }
        else if(TextUtils.isEmpty(from.getText()))
        {
            from.setError("Please choose the source");
        }
        else if(TextUtils.isEmpty(to.getText()))
        {
            to.setError("Please choose the destination");
        } else if(d == null)
        {
            selectDate.setError("Please select a date");
        }
        else {
            progress = new ProgressDialog(this);
            progress.setMessage("Loading.....");
            progress.show();
            Log.i(TAG, "Here");
            DownloadTask task = new DownloadTask();
            url = "https://indianrailapi.com/api/v2/SeatAvailability/apikey/a57fdcb882fc75db813393b6ccb9966f/TrainNumber/"+ trainNo.getText() +
                    "/From/"+ from.getText() +"/To/"+ to.getText() +"/Date/"+y+m+dateOfMonth+"/Quota/GN/Class/"+item;
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
            JSONArray AvailabilityArray = jsonObject.getJSONArray("Availability");
            Log.i(TAG,"Array is "+AvailabilityArray);
            arrayLength = AvailabilityArray.length();
            for (int i = 0 ;i<AvailabilityArray.length();i++)
            {
                JSONObject jsonPart = AvailabilityArray.getJSONObject(i);
                String aDate = jsonPart.getString("JourneyDate");
                String aAvability = jsonPart.getString("Availability");
                String aConfirm = jsonPart.getString("Confirm");
                dateArray.add(aDate);
                availibiltyArray.add(aAvability);
                confirmArray.add(aConfirm);
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
                String fare= dateArray.get(i);
                Toast.makeText(SeatAvailability.this,"Fare is "+fare,Toast.LENGTH_SHORT).show();
            }
        });
    }
    }

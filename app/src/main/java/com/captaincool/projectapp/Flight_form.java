package com.captaincool.projectapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class Flight_form extends AppCompatActivity {

    EditText src,des,setDate,noOfAdults,noOfChild,noOfInf;
    Button search;
    DatePickerDialog dpd;
    Calendar c;
    Date d;
    RadioGroup rg,cg ;
    RadioButton rb,cb;
    final String TAG = "Rapp";
    String y,m,dateOfMonth,jclass;
    int jtype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_form);
        src = findViewById(R.id.src1);
        des = findViewById(R.id.des);
        setDate = findViewById(R.id.setDate);
        noOfAdults = findViewById(R.id.noOfAdults);
        noOfChild = findViewById(R.id.noOfChild);
        noOfInf = findViewById(R.id.noOfInf);
        search = findViewById(R.id.search);
        cg  = findViewById(R.id.classGroup);
        rg  = findViewById(R.id.typeClass);
        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate();
            }
        });
//        setDate.setText(d.toString());

    }

    private void pickDate() {
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
                d.setMonth(i1);
                d.setDate(i2);
            }
        },year,month,date);
        Log.i(TAG,"Date is "+d);
        dpd.show();
    }

    public void searchData(View v)
    {
        int validationStatus = 0;
        validationStatus = chackValidation();
        String url=null;
        if(d != null)
        {
            y = String.valueOf(d.getYear());
            m = String.format("%02d",d.getMonth()+1);
            dateOfMonth = String.format("%02d",d.getDate());}
            Log.i(TAG,y+""+m+""+dateOfMonth);

        if(validationStatus == 1)
        {
            if(cb.getId() == R.id.biz)
            {
                jclass = "B";
            } else {jclass = "E";}
            if(rb.getId() == R.id.dom)
            {
                jtype = 100;
            } else{jtype = 0;}

            try {
                Log.i(TAG,"cb is : "+cb.getText());
                Log.i(TAG,"rb is : "+rb.getText());
                  url = "https://developer.goibibo.com/api/search/?app_id=b40ea9c9&app_key=9f2df9004896272f0720cdd0bf7918de&format=json&source="
                        + src.getText() +"&destination="+ des.getText() +
                        "&dateofdeparture="+y+m+dateOfMonth+"&seatingclass="+ jclass +"&adults="+ noOfAdults.getText() +
                        "&children="+ noOfChild.getText() +"&infants="+ noOfInf.getText() +"&counter="+jtype;
                Log.i(TAG,"String is : "+url);
                Intent i = new Intent(this,FlightList.class);
                i.putExtra("url",url);
                startActivity(i);
//                data = task.execute(url).get();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this,"Cannot able to get Data :(",Toast.LENGTH_SHORT).show();
            }
        }
//        Log.i(TAG,"Data is : "+data);
    }

    private int chackValidation() {
        int selectedClassId = cg.getCheckedRadioButtonId();
        int selectedTypeClass = rg.getCheckedRadioButtonId();
        rb = findViewById(selectedTypeClass);
        cb = findViewById(selectedClassId);
        if(TextUtils.isEmpty(src.getText()))
        {
            src.setError("Name is required");
            return 0;
        }
        else if(TextUtils.isEmpty(des.getText()))
        {
            des.setError("Please choose the destination");
            return 0;
        } else if(d == null)
        {
            setDate.setError("Please select a date");
            return 0;
        }
        else{
            Log.i(TAG,"Here");
        return 1;}
    }
}

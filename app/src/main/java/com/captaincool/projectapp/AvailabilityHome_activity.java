package com.captaincool.projectapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AvailabilityHome_activity extends AppCompatActivity {

    public void CheckTrainAvailability(View view)
    {
        Intent intent = new Intent(this,TrainOptions.class);
        startActivity(intent);
    }
    public void BusForm(View view)
    {
        Intent intent = new Intent(this,Bus_form.class);
        startActivity(intent);
    }
    public void FlightForm(View view)
    {
        Intent intent = new Intent(this,Flight_form.class);
        startActivity(intent);
    }
    public void HotelForm(View view)
    {
        Intent intent = new Intent(this,Hotel_form.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability_home_activity);
    }
}

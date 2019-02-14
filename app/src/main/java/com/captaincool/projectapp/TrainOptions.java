package com.captaincool.projectapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TrainOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_options);
    }

    public void liveTrain(View view)
    {
        Intent intent = new Intent(this,LiveTrainSatus.class);
        startActivity(intent);
    }
    public void pnrStatus(View view)
    {
        Intent intent = new Intent(this,PNRStatus.class);
        startActivity(intent);
    }
    public void liveStation(View view)
    {
        Intent intent = new Intent(this,LiveStation.class);
        startActivity(intent);
    }
    public void cancelledTrain(View view)
    {
        Intent intent = new Intent(this,TrainOptions.class);
        startActivity(intent);
    }
    public void divertedTrain(View view)
    {
        Intent intent = new Intent(this,TrainOptions.class);
        startActivity(intent);
    }
    public void trainFare(View view)
    {
        Intent intent = new Intent(this,TrainOptions.class);
        startActivity(intent);
    }
}

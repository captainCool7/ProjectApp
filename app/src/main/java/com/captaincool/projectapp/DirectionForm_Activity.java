package com.captaincool.projectapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DirectionForm_Activity extends AppCompatActivity {

    public void getDirection(View view)
    {
        Intent intent = new Intent(this,DirectionResult_activity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_form_);
    }
}

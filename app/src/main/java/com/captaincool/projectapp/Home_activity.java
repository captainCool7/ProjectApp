package com.captaincool.projectapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.parse.ParseUser;

public class Home_activity extends AppCompatActivity {
    TextView msg;
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;
    private CheckHome mCheckHome;
    private ScheduleHome mScheduleHome;
    private DirectionHome mDirectionHome;

    public void CheckAvailability(View view)
    {
        Intent intent = new Intent(this,AvailabilityHome_activity.class);
        startActivity(intent);
    }

    public void CreateSchedule(View view)
    {
        Intent intent = new Intent(this,ScheduleForm.class);
        startActivity(intent);
    }

    public void DirectionForm(View view)
    {
        Intent intent = new Intent(this,DirectionForm_Activity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_activity);

        ParseUser CurrentUser = ParseUser.getCurrentUser();

        msg = findViewById(R.id.msg);

        if(CurrentUser != null)
        {
            msg.setText(CurrentUser.getString("name"));
        }
        mMainFrame =(FrameLayout) findViewById(R.id.nav_View);
        mMainNav =(BottomNavigationView) findViewById(R.id.nav_bar);

        mCheckHome = new CheckHome();
        mScheduleHome = new ScheduleHome();
        mDirectionHome = new DirectionHome();
        setFragment(mCheckHome);

        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.HomeItem:
                        setFragment(mCheckHome);
                        return true;
                    case R.id.ListItem:
                        setFragment(mScheduleHome);
                        return true;
                    case R.id.DirectionItem:
                        setFragment(mDirectionHome);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
    public void logOut(View v)
    {
        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Loading.....");
        progress.show();
        ParseUser.logOut();
        Intent intent = new Intent(Home_activity.this,LogIn_activity.class);
        startActivity(intent);
        finish();
        progress.dismiss();
    }
    private  void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_View,fragment);
        fragmentTransaction.commit();
    }
}

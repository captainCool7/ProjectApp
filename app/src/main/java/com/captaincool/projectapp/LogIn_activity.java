package com.captaincool.projectapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LogIn_activity extends AppCompatActivity {

    EditText username,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_activity);
        username = findViewById(R.id.username);
        pass = findViewById(R.id.pass);
        if(ParseUser.getCurrentUser()!=null)
        {
            Intent intent = new Intent(this,Home_activity.class);
            startActivity(intent);
            finish();
        }
    }
    public void Login(View view)
    {
        if(TextUtils.isEmpty(username.getText()))
        {
            username.setError("Username is required");
        } else
        if(TextUtils.isEmpty(pass.getText()))
        {
            username.setError("Password is required");
        } else
        {
            final ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Loading.....");
            progress.show();
            ParseUser.logInInBackground(username.getText().toString(),pass.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    progress.dismiss();
                    if (parseUser != null) {
                        Toast.makeText(LogIn_activity.this,"Welcome back",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LogIn_activity.this,Home_activity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ParseUser.logOut();
                        Toast.makeText(LogIn_activity.this,e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void signUp(View v)
    {
        Intent intent = new Intent(this,SignUp_activity.class);
        startActivity(intent);
    }
}

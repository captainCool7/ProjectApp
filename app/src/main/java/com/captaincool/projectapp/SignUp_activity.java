package com.captaincool.projectapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.regex.Pattern;

public class SignUp_activity extends AppCompatActivity {

    EditText name,mail,pass,cpass;
    Pattern namePattern = Pattern.compile("^[A-Za-z\\s]+[A-Za-z\\s]*$");
    Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_activity);
        name = findViewById(R.id.name);
        mail = findViewById(R.id.mail);
        pass = findViewById(R.id.pass);
        cpass = findViewById(R.id.cpass);
    }

    public void signupButton(View v)
    {
        if(TextUtils.isEmpty(name.getText()))
        {
            name.setError("Name is required");
        } else if(TextUtils.isEmpty(mail.getText()))
        {
            mail.setError("Email is required");
        } else if(TextUtils.isEmpty(pass.getText()))
        {
            pass.setError("Password is required");
        } else if(!pass.getText().toString().equals(cpass.getText().toString()))
        {
            cpass.setError("Password should be same");
        } else if(!Patterns.EMAIL_ADDRESS.matcher(mail.getText()).matches())
        {
            mail.setError("Please Provide Correct email address");
        } else if(!namePattern.matcher(name.getText()).matches())
        {
            name.setError("Not a correct name");
        } else if(!passwordPattern.matcher(pass.getText()).matches())
        {
            pass.setError("Password is not Correct");
        }
        else
        {
            final ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Loading.....");
            progress.show();
            ParseUser user = new ParseUser();
// Set the user's username and password, which can be obtained by a forms
            user.setUsername(mail.getText().toString().trim());
            user.setEmail(mail.getText().toString().trim());
            user.setPassword(pass.getText().toString());
            user.put("name",name.getText().toString().trim());
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    progress.dismiss();
                    if (e == null) {
                        Toast.makeText(SignUp_activity.this,"Welcome!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUp_activity.this,Home_activity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ParseUser.logOut();
                        Toast.makeText(SignUp_activity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}

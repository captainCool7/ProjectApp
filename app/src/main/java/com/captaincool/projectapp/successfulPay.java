package com.captaincool.projectapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static android.widget.Toast.LENGTH_SHORT;

public class successfulPay extends AppCompatActivity {
    final static String TAG = "Rapp";
    Button button;
    DatePickerDialog datePicker;
    Date myDate,cDate;
    ParseUser user;
    TextView dateView;
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> Desc = new ArrayList<>();
    ArrayList<Integer> Day = new ArrayList<>();
    ArrayList<Integer> Hour = new ArrayList<>();
    ArrayList<Integer> Min = new ArrayList<>();
    Random mRandom = new Random();
    ProgressDialog progress;
    int status =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_pay);
        button = findViewById(R.id.button);
        dateView = findViewById(R.id.dateView);
        button.setEnabled(false);
        user = ParseUser.getCurrentUser();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, successfulPay.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "test", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            Notification.Builder builder = new Notification.Builder(this, "default")
                    .setContentTitle("Congratulations!!")
                    .setContentText("Glad you like are product")
                    .setSmallIcon(android.R.drawable.arrow_up_float)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentIntent(pendingIntent);
            manager.notify(0, builder.build());
        }
    }

    public void setTime(View v)
    {
        Calendar date = Calendar.getInstance();
        myDate = new Date();
        int day = date.get(Calendar.DAY_OF_MONTH);
        int month = date.get(Calendar.MONTH);
        int year = date.get(Calendar.YEAR);
        cDate = new Date(year,month,day);
        Log.i(TAG,"Time is set to: "+day+"/"+month+"/"+year);
        Log.i(TAG,"Time is set to: "+myDate.getDate());
        Log.i(TAG,"Time is set to: "+date.get(Calendar.DATE));
        Log.i(TAG,"Time is set to: "+date.get(Calendar.DAY_OF_WEEK));
        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                dateView.setText(d+"/"+m+"/"+y);
                myDate.setDate(d);
                myDate.setMonth(m);
                myDate.setYear(y);
                myDate.setHours(0);
                myDate.setMinutes(0);
                myDate.setSeconds(0);
                user.put("pkgDate",myDate);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        status =1;
                        Log.i(TAG,"Package is set to: "+ user.get("pkgDate"));
                    }
                });
            }
        },year,month,day);
        datePicker.show();
    }

    public void DateList(View v){
        String data=null;

        if(myDate == null)
        {
            dateView.setText("Please select the date");
        } else
            if(myDate.before(cDate))
            {
                dateView.setText("Please select the correct date");
            }
            else
        {
            progress = new ProgressDialog(this);
            progress.setMessage("Pleases wait....");
            DownloadTask task = new DownloadTask();
            try {
                data = task.execute("https://api.myjson.com/bins/l4pbg").get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i(TAG,"Result is "+data);
            try {
                JSONObject object = new JSONObject(data);
                String name = object.getString("name");
                int NoOfDays = object.getInt("Days");
                JSONArray scheduleArray = object.getJSONArray("Schedule");
                Log.i(TAG,"Schedule is "+scheduleArray);
                for(int i=0;i<scheduleArray.length();i++)
                {
                    JSONObject jsonPart = scheduleArray.getJSONObject(i);
                    title.add(jsonPart.getString("Title"));
                    Desc.add(jsonPart.getString("Description"));
                    Day.add(jsonPart.getInt("Day"));
                    Hour.add(jsonPart.getInt("Hour"));
                    Min.add(jsonPart.getInt("Minute"));
                    Log.i(TAG,"Element at "+i+" has "+jsonPart.getInt("Day")+" | "+jsonPart.getInt("Hour")+" | "+jsonPart.getInt("Minute"));
                }
//                insertData();
                setNotification();
            }catch (Exception e)
            {
                e.printStackTrace();
                Log.i(TAG,"Exception is "+e.getMessage());
            }
            Log.i(TAG,"Working fine");
        }
    }

    public void setNotification()
    {
        Calendar calendar = Calendar.getInstance();
        for(int i =0;i<title.size();i++) {
            String cTitle = title.get(i);
            String cDesc = Desc.get(i);
            calendar.set(myDate.getYear(),
                    myDate.getMonth(),
                    calulateDate(i),
                    Hour.get(i),
                    Min.get(i),0);
            Log.i("Rapp","Time remaining is "+calendar.getTimeInMillis());
            Log.i("Rapp","Calender is "+calendar.DAY_OF_MONTH);
            setAlarm(calendar.getTimeInMillis(),mRandom.nextInt(200),cTitle,cDesc);
        }
        Toast.makeText(this,"Succesfully Set notification", LENGTH_SHORT).show();
        progress.dismiss();
        button.setEnabled(true);
    }
    public int calulateDate(int i)
    {
        int actualDate = myDate.getDate()+Day.get(i);
        Log.i(TAG,"DAy is "+myDate.getDate());
        return actualDate;
    }

    private void setAlarm(long timeInMillis,int rcode,String title,String desc) {
        AlarmManager am =(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this,successfulPay.class);
        i.putExtra("title",title);
        i.putExtra("desc",desc);
        PendingIntent pi = PendingIntent.getBroadcast(this,rcode,i,0);
//        am.setRepeating(AlarmManager.RTC_WAKEUP,timeInMillis,AlarmManager.INTERVAL_DAY,pi);
        am.set(AlarmManager.RTC_WAKEUP,timeInMillis,pi);
        Log.i(TAG,"Alarm set "+rcode);
    }

    public void BackHome(View v) {
            Intent intent = new Intent(this, Home_activity.class);
            startActivity(intent);
    }
}

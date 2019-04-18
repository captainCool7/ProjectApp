package com.captaincool.projectapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class successfulPay extends AppCompatActivity {

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_pay);
        button = findViewById(R.id.button);
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
    public void BackHome(View v)
    {
        Intent intent = new Intent(this,Home_activity.class);
        startActivity(intent);
    }
}

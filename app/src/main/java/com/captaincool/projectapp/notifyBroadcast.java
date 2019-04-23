package com.captaincool.projectapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class notifyBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Your time is up", Toast.LENGTH_SHORT).show();
        String Title = intent.getStringExtra("title");
        String Desc = intent.getStringExtra("desc");
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "test", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            Notification.Builder builder = new Notification.Builder(context, "default")
                    .setContentTitle(Title)
                    .setContentText(Desc)
                    .setSmallIcon(android.R.drawable.arrow_up_float)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentIntent(pendingIntent);
            manager.notify(0, builder.build());
        }
    }
}

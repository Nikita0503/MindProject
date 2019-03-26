package com.mindproject.mindproject.support;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.mindproject.mindproject.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class TimeNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id", 0);
        String description = intent.getStringExtra("description");
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, "1")
                        .setSmallIcon(R.drawable.brain)
                        .setContentTitle("Vote me!")
                        .setContentText(description);

        Notification notification = builder.build();



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("1", "InMindChannel",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Notifications");
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            channel.enableVibration(true);
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(id, notification);

            Log.d("Notification", description);
        }


    }
}

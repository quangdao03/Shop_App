package com.example.shop_app.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.shop_app.R;
import com.example.shop_app.activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        RemoteMessage.Notification notification = message.getNotification();
        if (notification != null){
            return;
        }
        String title = notification.getTitle();
        String messages = notification.getBody();
        sendNotification(title,messages);
    }

    private void sendNotification(String title, String messages) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationbuilder = new NotificationCompat.Builder(this,MyApplication.CHANEL_ID)
                .setContentTitle(title)
                .setContentText(messages)
                .setSmallIcon(R.mipmap.fram)
                .setContentIntent(pendingIntent);
        Notification notification = notificationbuilder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null){
            notificationManager.notify(1,notification);
        }
    }


}

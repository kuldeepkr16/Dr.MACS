package com.example.lenovo.dra.Activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.lenovo.dra.R;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String messageTitle = remoteMessage.getNotification().getTitle();
        String messageBody = remoteMessage.getNotification().getBody();

        String click_action = remoteMessage.getNotification().getClickAction();
        String dataMessage = remoteMessage.getData().get("dataMessage");
        String fromName = remoteMessage.getData().get("fromName");

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.strethoscope)
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody);

        Intent resultIntent = new Intent(click_action);
        resultIntent.putExtra("dataMessage", dataMessage);
        resultIntent.putExtra("fromName",fromName);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        mBuilder.setContentIntent(resultPendingIntent);

        int mNotiId = (int) System.currentTimeMillis();
        NotificationManager mNotiMangr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotiMangr.notify(mNotiId, mBuilder.build());
    }
}







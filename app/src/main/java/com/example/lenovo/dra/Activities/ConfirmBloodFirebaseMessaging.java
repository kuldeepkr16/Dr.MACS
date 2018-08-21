package com.example.lenovo.dra.Activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.google.firebase.messaging.*;

public class ConfirmBloodFirebaseMessaging extends com.google.firebase.messaging.FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        String messageTitle = remoteMessage.getNotification().getTitle();
        String messageBody = remoteMessage.getNotification().getBody();

        String click_action = remoteMessage.getNotification().getClickAction();
        String phone_no = remoteMessage.getData().get("phone_no");
        String from_Name = remoteMessage.getData().get("from_Name");

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(com.example.lenovo.dra.R.drawable.strethoscope)
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody);

        Intent resultIntent = new Intent(click_action);
        resultIntent.putExtra("phone_no", phone_no);
        resultIntent.putExtra("from_Name",from_Name);
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

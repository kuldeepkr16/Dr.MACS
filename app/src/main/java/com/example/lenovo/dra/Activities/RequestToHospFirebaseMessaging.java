package com.example.lenovo.dra.Activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.example.lenovo.dra.R;
import com.google.firebase.messaging.*;

public class RequestToHospFirebaseMessaging extends com.google.firebase.messaging.FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String messageTitle = remoteMessage.getNotification().getTitle();
        String messageBody = remoteMessage.getNotification().getBody();

        String click_action_medi = remoteMessage.getNotification().getClickAction();
        String from_name = remoteMessage.getData().get("from_name");
        String chosen_date = remoteMessage.getData().get("chosen_date");
        String chosen_time = remoteMessage.getData().get("chosen_time");
        String phone = remoteMessage.getData().get("phone");
        String token_id = remoteMessage.getData().get("token_id");

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.strethoscope)
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody);

        Intent resultIntent = new Intent(click_action_medi);
        resultIntent.putExtra("from_name",from_name);
        resultIntent.putExtra("chosen_date", chosen_date);
        resultIntent.putExtra("chosen_time", chosen_time);
        resultIntent.putExtra("phone", phone);
        resultIntent.putExtra("token_id", token_id);

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

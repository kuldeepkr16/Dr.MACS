package com.example.lenovo.dra.Activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.example.lenovo.dra.R;
import com.google.firebase.messaging.*;

public class BloodFirebaseMessaging extends com.google.firebase.messaging.FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String messageTitle = remoteMessage.getNotification().getTitle();
        String messageBody = remoteMessage.getNotification().getBody();

        String click_action = remoteMessage.getNotification().getClickAction();
        String fromName = remoteMessage.getData().get("fromName");
        String blood_type = remoteMessage.getData().get("blood_type");
        String urgency = remoteMessage.getData().get("urgency");
        String phone_no = remoteMessage.getData().get("phone_no");
        String token_id = remoteMessage.getData().get("token_id");
        String NOW = remoteMessage.getData().get("NOW");

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.blood_drop)
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody);

        Intent resultIntent = new Intent(click_action);
        resultIntent.putExtra("fromName",fromName);
        resultIntent.putExtra("blood_type", blood_type);
        resultIntent.putExtra("urgency", urgency);
        resultIntent.putExtra("phone_no", phone_no);
        resultIntent.putExtra("token_id", token_id);
        resultIntent.putExtra("NOW", NOW);

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
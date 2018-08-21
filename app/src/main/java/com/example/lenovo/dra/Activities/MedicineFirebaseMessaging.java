package com.example.lenovo.dra.Activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.example.lenovo.dra.R;
import com.google.firebase.messaging.*;

public class MedicineFirebaseMessaging extends com.google.firebase.messaging.FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String messageTitle = remoteMessage.getNotification().getTitle();
        String messageBody = remoteMessage.getNotification().getBody();

        String click_action_medi = remoteMessage.getNotification().getClickAction();

        String from_name = remoteMessage.getData().get("from_name");
        String medi_name = remoteMessage.getData().get("medi_name");
        String imageURL = remoteMessage.getData().get("imageURL");
        String token_id = remoteMessage.getData().get("token_id");

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.medicines)
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody);

        Intent resultIntent = new Intent(click_action_medi);
        resultIntent.putExtra("from_name",from_name);
        resultIntent.putExtra("medi_name", medi_name);
        resultIntent.putExtra("imageURL", imageURL);
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

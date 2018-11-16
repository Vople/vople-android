package com.mobile.vople.vople.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mobile.vople.vople.MainActivity;
import com.mobile.vople.vople.R;

import java.util.Map;

/**
 * Created by parkjaemin on 16/11/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("newToken",s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> pushDataMap = remoteMessage.getData();
        sendPushNotification(pushDataMap);

    }

    private void sendPushNotification(Map<String, String> dataMap)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(dataMap.get("title"))
                .setContentText(dataMap.get("msg"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{1000, 1000})
                .setLights(Color.WHITE,1500,1500)
                .setContentIntent(contentIntent);

        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(0 /* ID of notification */, nBuilder.build());
    }
}

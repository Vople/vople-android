package com.mobile.vople.vople.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.mobile.vople.vople.server.SharedPreference;

/**
 * Created by parkjaemin on 16/11/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {


    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        SharedPreference.getInstance(null).put("FCM_TOKEN",refreshedToken);
        Log.d("newToken", refreshedToken);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        Log.d("MyFirebaseInstance",token);
    }
}

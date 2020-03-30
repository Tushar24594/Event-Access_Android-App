package in.tushar.eventaccess.services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

public class Service extends FirebaseMessagingService {
    private static final String TAG = Service.class.getName();

    public Service() {
    }
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        sendRegistrationToServer(token);
    }
}

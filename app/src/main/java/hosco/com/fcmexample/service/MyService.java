package hosco.com.fcmexample.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import hosco.com.fcmexample.MyBroadcastReceive;
import hosco.com.fcmexample.R;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;


public class MyService extends FirebaseMessagingService {

    private static final String TAG = MyService.class.getSimpleName();
    private static final String CHANNEL_ID = "CHANNEL_ID";
    static final String ACTION_SNOOZE = "OK";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
                Log.d(TAG, " " + remoteMessage.getData());

            } else {
                // Handle message within 10 seconds
//                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();
            sendNotification(title, 0, message);

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        sendRegistrationToServer(token);
    }


    void sendNotification(String title, int notificationId, String content) {

        // Create an explicit intent for an Activity in your app
        /* Intent intent = new Intent(ctx, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent, 0); */

        Intent snoozeIntent = new Intent(this, MyBroadcastReceive.class);
        snoozeIntent.setAction(ACTION_SNOOZE);
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);

        Log.e(TAG, snoozeIntent.getExtras().toString());

        Log.e(TAG, "snoozeIntent id: " + snoozeIntent.getIntExtra(EXTRA_NOTIFICATION_ID, -1));

        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(this, notificationId, snoozeIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_adidas)
//                    .setContentTitle(String.format("%s (id %d)", title, notificationId))
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(false)
                // Add the action button
                .addAction(R.drawable.ic_nike, getString(R.string.snooze),
                        snoozePendingIntent)
                ;

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());
    }

}

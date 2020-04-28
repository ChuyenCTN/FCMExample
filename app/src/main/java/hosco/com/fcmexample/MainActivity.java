package hosco.com.fcmexample;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static hosco.com.fcmexample.MyBroadcastReceive.ACTION_SNOOZE;

public class MainActivity extends AppCompatActivity {


    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final String TAG = "main";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        createNotificationChannel();

        Log.e(TAG, "onCreate");


    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
                .addAction(R.drawable.ic_launcher_foreground, getString(R.string.snooze),
                        snoozePendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());
    }

}

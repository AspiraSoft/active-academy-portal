package org.activeacademy.portal.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.activeacademy.portal.R;
import org.activeacademy.portal.db.NotificationStorage;
import org.activeacademy.portal.db.OnNotificationReceivedListener;

public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "ACTIVEPORTAL";
    private static int notificationId = 0;
    private final NotificationCompat.Builder nBuilder;

    private static OnNotificationReceivedListener listener;

    public MessagingService() {
        nBuilder = new NotificationCompat.Builder(this);
    }

    private void generateNotification(RemoteMessage.Notification notification) {
        nBuilder.setSmallIcon(R.mipmap.ic_launcher);
        nBuilder.setContentTitle(notification.getTitle());
        nBuilder.setContentText(notification.getBody());
        nBuilder.setLights(Color.GREEN, 100, 100);
        nBuilder.setVibrate(new long[]{100, 50, 50, 100, 5000});

        NotificationManager notifier = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notifier != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (notifier.areNotificationsEnabled()) {
                    notifier.notify(notificationId++, nBuilder.build());
                }
            } else {
                notifier.notify(notificationId++, nBuilder.build());
            }
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // If message contains a notification payload
        if (remoteMessage.getNotification() != null) {
            // Log event
            Log.d(TAG, "Notification received!");

            // Notify user
            generateNotification(remoteMessage.getNotification());

            // Save notification
            SharedPreferences prefs = getSharedPreferences("Khokha", MODE_PRIVATE);
            NotificationStorage.getInstance(prefs)
                    .addNotification(remoteMessage.getNotification())
                    .saveInstance(prefs);

            // Trigger event listener
            if (listener != null) {
                listener.onNotificationReceived();
            }

        }
    }

    public static void setOnNotificationReceivedListener(OnNotificationReceivedListener onNotificationReceivedListener) {
        listener = onNotificationReceivedListener;
    }
}

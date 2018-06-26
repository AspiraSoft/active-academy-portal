package org.activeacademy.portal.db;

import android.content.SharedPreferences;

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NotificationStorage implements Serializable {

    private static NotificationStorage ourInstance;
    private List<RemoteMessage.Notification> notifications = new ArrayList<>();

    private NotificationStorage() {

    }

    public static NotificationStorage getInstance() {
        if (ourInstance == null) {
            ourInstance = new NotificationStorage();
        }

        return ourInstance;
    }

    public static NotificationStorage getInstance(SharedPreferences preferences) {
        String json = preferences.getString("notifications", null);
        if (json != null) {
            Gson gson = new Gson();
            ourInstance = gson.fromJson(json, NotificationStorage.class);
        } else if (ourInstance == null) {
            ourInstance = new NotificationStorage();
        }
        return ourInstance;
    }

    public void saveInstance(SharedPreferences preferences) {
        if (ourInstance == null) return;
        Gson gson = new Gson();
        String json = gson.toJson(ourInstance);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("notifications", json);
        editor.apply();
    }

    public NotificationStorage addNotification(RemoteMessage.Notification notification) {
        notifications.add(notification);
        return this;
    }

    public List<RemoteMessage.Notification> getNotifications() {
        return notifications;
    }

    public void markAsRead() {
        notifications.clear();
    }

}

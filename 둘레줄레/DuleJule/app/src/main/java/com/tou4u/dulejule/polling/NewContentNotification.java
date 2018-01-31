package com.tou4u.dulejule.polling;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import com.tou4u.dulejule.R;
import com.tou4u.dulejule.contentviewer.main.MainActivity;

import java.util.HashSet;

public class NewContentNotification {

    private HashSet<Integer> notifyIDs;

    private static class Singleton {
        private static final NewContentNotification instance = new NewContentNotification();
    }

    public static NewContentNotification getInstance() {
        return Singleton.instance;
    }

    private NewContentNotification() {
        notifyIDs = new HashSet<>();
    }

    private int makeNotifyID() {
        int id;
        while (true) {
            id = (int) (Math.random() * 10000);
            if (!notifyIDs.contains(id)) {
                notifyIDs.add(id);
                break;
            }
        }
        return id;
    }

    private Notification getNotification(Context context, int notifyID, String contentTitle, String contentType) {

        Intent notiIntent = new Intent(context, MainActivity.class);
        notiIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notiIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent content = PendingIntent.getActivity(
                context, notifyID, notiIntent, PendingIntent.FLAG_ONE_SHOT);

        String notifyTitle = String.format("새로운 공지 : %s", contentType);

        return new Notification.Builder(context)
                .setTicker(notifyTitle)
                .setContentTitle(notifyTitle)
                .setContentText(contentTitle)
                .setSmallIcon(R.drawable.ic_new_notification)
                .setContentIntent(content)
                .setAutoCancel(true)
                .build();
    }

    public void notificate(Context context, NotificationManagerCompat manager, String contentTitle, String contentType) {
        int notifyID = makeNotifyID();
        Notification notification = getNotification(context, notifyID, contentTitle, contentType);
        manager.notify(notifyID, notification);
    }

}

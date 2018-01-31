package com.tou4u.dulejule.polling;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.tou4u.dulejule.contentviewer.main.MainActivity;
import com.tou4u.dulejule.downloading.RecentDownloader;
import com.tou4u.dulejule.util.DebugUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class PollService extends IntentService {
    private static final String TAG = "PollService";
    private static final long POLL_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
    //private static final long POLL_INTERVAL = 3000;

    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);
        DebugUtil.logD(TAG, "Service On : " + isOn);
        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), POLL_INTERVAL, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent
                .getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!isNetworkAvailableAndConnected()) {
            DebugUtil.logD(TAG, "Network UnConnect");
            return;
        }

        Calendar c = Calendar.getInstance();
        Date d = c.getTime();

        boolean pushState = QueryPreferences.getPushState(this);
        DebugUtil.logD(TAG, "PollService PushState : " + pushState + d.toString());

        Set<String> currentBase = QueryPreferences.getRecentBaseContent(this);
        Set<String> currentImage = QueryPreferences.getRecentImageContent(this);
        Set<String> currentEvent = QueryPreferences.getRecentEventContent(this);

        if (currentBase != null)
            DebugUtil.logD(TAG, "PollService current base : " + currentBase.toString());

        if (currentImage != null)
            DebugUtil.logD(TAG, "PollService current iamge : " + currentImage.toString());

        if (currentEvent != null)
            DebugUtil.logD(TAG, "PollService current event: " + currentEvent.toString());

        RecentDownloader recentDownloader = new RecentDownloader(this);
        String recentBase = recentDownloader.executeBaseAPI(currentBase);
        String recentImage = recentDownloader.executeImageAPI(currentImage);
        String recentEvent = recentDownloader.executeEventAPI(currentEvent);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        if (recentBase != null)
            DebugUtil.logD(TAG, "PollService recent base : " + recentBase.toString());

        if (recentImage != null)
            DebugUtil.logD(TAG, "PollService recent iamge : " + recentImage.toString());

        if (recentEvent != null)
            DebugUtil.logD(TAG, "PollService recent event: " + recentEvent.toString());

        if (recentBase != null)
            NewContentNotification.getInstance().notificate(this, notificationManager, recentBase, "대표");

        if (recentImage != null)
            NewContentNotification.getInstance().notificate(this, notificationManager, recentImage, "영상");

        if (recentEvent != null)
            NewContentNotification.getInstance().notificate(this, notificationManager, recentEvent, "행사");

    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();

        return isNetworkConnected;
    }

}

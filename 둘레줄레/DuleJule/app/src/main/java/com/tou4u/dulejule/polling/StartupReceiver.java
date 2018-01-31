package com.tou4u.dulejule.polling;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tou4u.dulejule.util.DebugUtil;

public class StartupReceiver extends BroadcastReceiver {
    private static final String TAG = "StartupReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        DebugUtil.logD(TAG, "Received broadcast intent: " + intent.getAction());
        boolean isOn = QueryPreferences.getPushState(context);
        PollService.setServiceAlarm(context, isOn);
    }
}

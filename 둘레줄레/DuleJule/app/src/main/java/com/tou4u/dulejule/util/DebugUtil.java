package com.tou4u.dulejule.util;

import android.util.Log;

import com.tou4u.dulejule.BuildConfig;

public class DebugUtil {

    public static final String TAG_HEAD = "CUSTUM DEBUG LOG";

    private static final String tag(String tail) {
        return String.format("%s ( %s )", TAG_HEAD, tail);
    }

    public static final void logD(String className, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(tag(className), message);
        }
    }

    public static final void logE(String className, String message, Exception e) {
        if (BuildConfig.DEBUG) {
            Log.e(tag(className), message, e);
        }
    }

}

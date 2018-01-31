package com.tou4u.dulejule.polling;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.Set;

public class QueryPreferences {

    public static final String PREF_PUSH_STATE = "PREF_PUSH_STATE";

    public static final String PREF_RECENT_BASE_CONTENT = "PREF_RECENT_BASE_CONTENT";
    public static final String PREF_RECENT_IMAGE_CONTENT = "PREF_RECENT_IMAGE_CONTENT";
    public static final String PREF_RECENT_EVENT_CONTENT = "PREF_RECENT_EVENT_CONTENT";

    public static boolean getPushState(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_PUSH_STATE, true);
    }

    public static void setPushState(Context context, boolean push) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_PUSH_STATE, push)
                .apply();
    }

    public static Set<String> getRecentBaseContent(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getStringSet(PREF_RECENT_BASE_CONTENT, null);
    }

    public static void setRecentBaseContent(Context context, Set<String> set) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putStringSet(PREF_RECENT_BASE_CONTENT, set)
                .apply();
    }

    public static Set<String> getRecentImageContent(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getStringSet(PREF_RECENT_IMAGE_CONTENT, null);
    }

    public static void setRecentImageContent(Context context, Set<String> set) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putStringSet(PREF_RECENT_IMAGE_CONTENT, set)
                .apply();
    }

    public static Set<String> getRecentEventContent(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getStringSet(PREF_RECENT_EVENT_CONTENT, null);
    }

    public static void setRecentEventContent(Context context, Set<String> set) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putStringSet(PREF_RECENT_EVENT_CONTENT, set)
                .apply();
    }

}

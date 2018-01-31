package com.tou4u.dulejule.contentviewer.main.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

    public static final int CODE_CONNECT = 0;
    public static final int CODE_UNCONNECT = 1;
    public static final int CODE_UNKNOWN = 2;

    private static class Singleton {
        private static final NetworkUtil instance = new NetworkUtil();
    }

    public static NetworkUtil getInstance() {
        return NetworkUtil.Singleton.instance;
    }

    public int isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            int status = activeNetwork.getType();
            if (status == ConnectivityManager.TYPE_WIFI || status == ConnectivityManager.TYPE_MOBILE)
                return CODE_CONNECT;
        }
        return CODE_UNCONNECT;
    }


}

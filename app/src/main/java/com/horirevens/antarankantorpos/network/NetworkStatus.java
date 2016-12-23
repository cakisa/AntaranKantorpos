package com.horirevens.antarankantorpos.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by horirevens on 12/22/16.
 */
public class NetworkStatus {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    public static final String MY_LOG = "log_MainActivity";

    public static int getConnectivityStatus(Context context) {
        Log.i(MY_LOG, "getConnectivityStatus");
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE
        );

        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    return TYPE_WIFI;
                }
                if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return TYPE_MOBILE;
                }
            }
            return TYPE_NOT_CONNECTED;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        Log.i(MY_LOG, "getConnectivityStatusString");
        int conn = NetworkStatus.getConnectivityStatus(context);
        String status = null;
        if (conn == NetworkStatus.TYPE_WIFI || conn == NetworkStatus.TYPE_MOBILE) {
            status = "1";
        }
        if (conn == NetworkStatus.TYPE_NOT_CONNECTED) {
            status = "0";
        }
        return status;
    }
}

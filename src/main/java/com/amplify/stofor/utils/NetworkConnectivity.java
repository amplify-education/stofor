package com.amplify.stofor.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.google.inject.Inject;

public class NetworkConnectivity {
    private final ConnectivityManager connectivityManager;

    @Inject
    public NetworkConnectivity(Context context) {
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public boolean isConnected() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public boolean isNotConnected() {
        return !isConnected();
    }

}

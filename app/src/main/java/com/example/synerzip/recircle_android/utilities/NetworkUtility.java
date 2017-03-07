package com.example.synerzip.recircle_android.utilities;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Prajakta Patil on 7/3/17.
 * Copyright © 2016 Synerzip. All rights reserved
 */
public class NetworkUtility {

    /**
     * checks for network availability
     *
     * @param context
     * @return boolean variable indicating the status
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}


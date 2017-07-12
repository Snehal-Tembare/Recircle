package com.example.synerzip.recircle_android.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.synerzip.recircle_android.MyApplication;

/**
 * Created by Prajakta Patil on 7/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
public  class NetworkUtility {

    /**
     * checks for network availability
     *
     * @return boolean variable indicating the status
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) MyApplication.getInstance().
                        getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }


}


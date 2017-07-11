package com.example.synerzip.recircle_android.utilities;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Prajakta Patil on 7/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
public class RCLog {

    public static final String RC_LOG_TAG = "ReCircle";

    /**
     * Method for showing toast
     *
     * @param context
     * @param toastMessage
     */
    public static void showToast(Context context, String toastMessage) {

        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
    }
    public static void showLongToast(Context context, String toastMessage) {

        Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show();
    }

    public static void debug(String message) {
        Log.d(RC_LOG_TAG, message);
    }
}
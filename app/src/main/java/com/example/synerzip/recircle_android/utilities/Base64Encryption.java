package com.example.synerzip.recircle_android.utilities;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by Prajakta Patil on 17/7/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
public class Base64Encryption {
    public static String encrypt(String s) {
        String key = "YXVzdGlucmV6aXBwdW5l";
        String string = s + key;
        String encryptedText = "";
        try {
            encryptedText = Base64.encodeToString(string.getBytes("UTF-8"), Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encryptedText;
    }
}

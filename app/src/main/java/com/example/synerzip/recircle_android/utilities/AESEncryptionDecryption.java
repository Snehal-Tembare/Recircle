package com.example.synerzip.recircle_android.utilities;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Prajakta Patil on 10/4/17.
 * Copyright © 2016 Synerzip. All rights reserved
 */

public class AESEncryptionDecryption {

    public static String encrypt(String seed, String cleartext)
            throws Exception {

        byte[] encryptedText = null;
        try {
            byte[] keyData = seed.getBytes();
            SecretKey ks = new SecretKeySpec(keyData, "AES");
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, ks);
            encryptedText = c.doFinal(cleartext.getBytes("UTF-8"));
            return Base64.encodeToString(encryptedText, Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        }
    }

    public static String decrypt(String seed, String encrypted)
            throws Exception {

        byte[] clearText = null;
        try {
            byte[] keyData = seed.getBytes();
            SecretKey ks = new SecretKeySpec(keyData, "AES");
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, ks);
            clearText = c.doFinal(Base64.decode(encrypted, Base64.DEFAULT));
            return new String(clearText, "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

}

package com.bestlabs.facerecoginination.others;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class Base64Utils {

    public static String intToBase64(int number) {
        // Convert the integer to a byte array using UTF-8 encoding
        byte[] byteArray;
        try {
            byteArray = String.valueOf(number).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        // Encode the byte array using Base64 encoding
        String base64String = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return base64String;
    }
}


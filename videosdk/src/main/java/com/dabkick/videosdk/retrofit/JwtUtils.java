package com.dabkick.videosdk.retrofit;

import android.util.Base64;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import timber.log.Timber;

/**
 * Created by developer on 12/7/17.
 */

// Utility class for Java Web Tokens
public class JwtUtils {

    // returns the time that the token expires (Unix time)
    public static String getExpireTime(String token) {
        try {
            String[] split = token.split("\\.");
            JSONObject time = new JSONObject(getJson(split[1]));
            return time.getString("exp");
        } catch (Exception e) {
            Timber.e(e);
            return null;
        }
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }
}

package com.dabkick.videosdk;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Handles all Preferences
 */
public class Prefs {

    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String FIREBASE_TOKEN = "firebase_token";

    private final static String filename = "promo_pref";

    private static SharedPreferences getPrefs() {
        return SdkApp.getAppContext().getSharedPreferences(filename, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor() {
        return getPrefs().edit();
    }

    public static String getAccessToken() {
        return getPrefs().getString(ACCESS_TOKEN, "");
    }

    public static void setAccessToken(String accessToken) {
        getEditor().putString(ACCESS_TOKEN, accessToken).apply();
    }

    public static String getRefreshToken() {
        return getPrefs().getString(REFRESH_TOKEN, "");
    }

    public static void setRefreshToken(String refreshToken) {
        getEditor().putString(REFRESH_TOKEN, refreshToken).apply();
    }

    public static String getFirebaseToken() {
        return getPrefs().getString(FIREBASE_TOKEN, "");
    }

    public static void setFirebaseToken(String firebaseToken) {
        getEditor().putString(FIREBASE_TOKEN, firebaseToken).apply();
    }

}
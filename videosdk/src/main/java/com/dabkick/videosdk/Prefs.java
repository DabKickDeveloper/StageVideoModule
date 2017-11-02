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
    private static final String DEVELOPER_ID = "developer_id";
    private static final String USER_ID = "user_id";
    private static final String DABNAME = "dabname";
    private static final String PROFILE_PIC_URL = "profile_pic_url";
    private static final String VIDEO_ENABLED = "video_enabled";
    private static final String AUDIO_ENABLED = "audio_enabled";


    private final static String filename = "prefs";

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

    public static String getDeveloperId() {
        return getPrefs().getString(DEVELOPER_ID, "");
    }

    public static void setDeveloperId(String developerId) {
        getEditor().putString(DEVELOPER_ID, developerId).apply();
    }

    public static String getUserId() {
        return getPrefs().getString(USER_ID, "");
    }

    public static void setUserId(String userId) {
        getEditor().putString(USER_ID, userId).apply();
    }

    public static String getDabname() {
        return getPrefs().getString(DABNAME, "");
    }

    public static void setDabname(String dabname) {
        getEditor().putString(DABNAME, dabname).apply();
    }

    public static String getProfilePicUrl() {
        return getPrefs().getString(PROFILE_PIC_URL, "");
    }

    public static void setProfilePicUrl(String param) {
        getEditor().putString(PROFILE_PIC_URL, param).apply();
    }

    public static boolean isVideoEnabled() {
        return getPrefs().getBoolean(VIDEO_ENABLED, false);
    }

    public static void setVideoEnabled(boolean param) {
        getEditor().putBoolean(VIDEO_ENABLED, param).apply();
    }

    public static boolean isAudioEnabled() {
        return getPrefs().getBoolean(AUDIO_ENABLED, false);
    }

    public static void setAudioEnabled(boolean param) {
        getEditor().putBoolean(AUDIO_ENABLED, param).apply();
    }

}
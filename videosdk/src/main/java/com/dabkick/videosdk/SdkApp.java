package com.dabkick.videosdk;

import android.app.Application;
import android.content.Context;

import timber.log.Timber;


public class SdkApp extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = this;

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static Context getAppContext() {
        return appContext;
    }
}

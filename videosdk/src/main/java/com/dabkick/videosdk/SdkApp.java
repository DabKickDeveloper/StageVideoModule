package com.dabkick.videosdk;

import android.app.Application;
import android.content.Context;

import timber.log.Timber;


public class SdkApp extends Application {

    private static Context appContext;
    private LivesessionComponent livesessionComponent;
    private DabKickSession dabKickSession;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = this;

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        livesessionComponent = DaggerLivesessionComponent.builder()
                .livesessionModule(new LivesessionModule())
                .build();

    }

    public DabKickSession getDabKickSession() {
        return dabKickSession;
    }

    public void setDabKickSession(DabKickSession dabKickSession) {
        this.dabKickSession = dabKickSession;
    }

    public LivesessionComponent getLivesessionComponent() {
        return livesessionComponent;
    }

    public static Context getAppContext() {
        return appContext;
    }

}
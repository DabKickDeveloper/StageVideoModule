package com.dabkick.videosdk;

import android.app.Application;
import android.content.Context;

import com.dabkick.videosdk.livesession.overviews.OverviewDatabase;

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

        OverviewDatabase.OverviewListener listener = () -> {

        };

        livesessionComponent = DaggerLivesessionComponent.builder()
                .livesessionModule(new LivesessionModule(listener))
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
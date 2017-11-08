package com.dabkick.videosdk;

import java.util.List;

/**
 * Provides interface with the developer app for setup
 */
public class DabKick {

    /**
     * Initializes SDK values
     */
    public static void initSdk(String developerId) {
        Prefs.setDeveloperId(developerId);
    }

    interface DabKickProviderInterface {
        void startDabKickWithVideos(List<DabKickVideoInfo> videoList); // TODO
        void returnToDabKickWithVideos(List<String> videoList); // TODO
        void provideCategories(int offset);
        void provideVideos(int offset);
    }

}
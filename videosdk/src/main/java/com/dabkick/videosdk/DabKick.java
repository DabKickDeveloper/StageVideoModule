package com.dabkick.videosdk;

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
}

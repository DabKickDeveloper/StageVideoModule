package com.dabkick.videosdk;


import java.util.ArrayList;

public class DabKickSession {


    private final DabKickVideoProvider dabKickVideoProvider;
    private final String developerId, developerKey;

    private DabKickSession(final String developerId, final String developerKey, final DabKickVideoProvider dabKickVideoProvider) {
        this.developerId = developerId;
        this.developerKey = developerKey;
        this.dabKickVideoProvider = dabKickVideoProvider;
    }

    public String getDeveloperId() {
        return developerId;
    }

    public String getDeveloperKey() {
        return developerKey;
    }

    public DabKickVideoProvider getDabKickVideoProvider() {
        return dabKickVideoProvider;
    }

    // builder pattern
    public static class DabKickBuilder {

        private final DabKickVideoProvider nestedDabKickVideoProvider;
        private final String developerId, developerKey;

        public DabKickBuilder(String developerId, String developerKey, final DabKickVideoProvider nestedDabKickVideoProvider) {
            this.developerId = developerId;
            this.developerKey = developerKey;
            this.nestedDabKickVideoProvider = nestedDabKickVideoProvider;
        }

        public DabKickSession build() {
            return new DabKickSession(developerId, developerKey, nestedDabKickVideoProvider);
        }

    }

    public interface DabKickVideoProvider {
        /**
         * Called by the SDK when it needs more videos
         * @param offset the last index which was loaded
         * @param category the category to load from
         * @return list of new videos
         */
        ArrayList<DabKickVideoInfo> provideVideos(String category, int offset);

        /**
         * Called when more categories are needed
         * @param offset the last loaded index
         * @return list of new categories
         */
        ArrayList<String> provideCategories(int offset);

        /**
         * Starts a DabKick session with videos
         * @return a list of videos for the session
         */
        ArrayList<DabKickVideoInfo> startDabKickWithVideos();
    }

}

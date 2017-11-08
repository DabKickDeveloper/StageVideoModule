package com.dabkick.videosdk;


import java.util.ArrayList;

public class DabKickSession {


    private final DabKickVideoProvider dabKickVideoProvider;

    private DabKickSession(final DabKickVideoProvider dabKickVideoProvider) {
        this.dabKickVideoProvider = dabKickVideoProvider;
    }

    // public getter methods here

    public DabKickVideoProvider getDabKickVideoProvider() {
        return dabKickVideoProvider;
    }

    // builder pattern
    public static class DabKickBuilder {

        private final DabKickVideoProvider nestedDabKickVideoProvider;

        public DabKickBuilder(String developerKey, final DabKickVideoProvider nestedDabKickVideoProvider) {
            this.nestedDabKickVideoProvider = nestedDabKickVideoProvider;
        }

        public DabKickSession build() {
            return new DabKickSession(
                    nestedDabKickVideoProvider);
        }


    }

    public interface DabKickVideoProvider {
        ArrayList<DabKickVideoInfo> provideVideos(int offset);
        ArrayList<String> provideCategories(int offset);
        ArrayList<DabKickVideoInfo> startDabKickWithVideos();
    }

}

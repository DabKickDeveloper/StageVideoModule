package com.dabkick.videosdk.livesession.stage;


public class StageVideo {

    private String url;

    private String state;

    public enum State {

        PLAYING("playing"), PAUSED("paused");

        private final String stateStr;

        State(String stateStr) {
            this.stateStr = stateStr;
        }


        @Override
        public String toString() {
            return stateStr;
        }
    }

    public StageVideo() {
        // firebase req'd empty constructor
    }

    public StageVideo(String url) {
        this.url = url;
        this.state = State.PAUSED.toString();
    }

    public String getState() {
        return state;
    }

    public String getUrl() {
        return url;
    }

}
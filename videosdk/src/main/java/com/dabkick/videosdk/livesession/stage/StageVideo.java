package com.dabkick.videosdk.livesession.stage;


public class StageVideo {

    private String url;
    private String state;
    private String key;

    private int playedSeconds;

    static final String PLAYING = "playing", PAUSED = "paused";

    public StageVideo() {
        // firebase req'd empty constructor
    }

    public StageVideo(String url) {
        this.url = url;
        this.state = PAUSED;
        this.playedSeconds = 0;
    }

    public int getPlayedSeconds() {
        return playedSeconds;
    }

    public void setPlayedSeconds(int playedSeconds) {
        this.playedSeconds = playedSeconds;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public String getUrl() {
        return url;
    }

}
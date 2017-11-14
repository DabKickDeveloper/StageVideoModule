package com.dabkick.videosdk.livesession.stage;


import com.google.firebase.database.Exclude;

import timber.log.Timber;

public class StageModel {

    private String url;
    private String state;
    private String key;

    private long playedMillis;

    static final String PLAYING = "playing", PAUSED = "paused";

    public StageModel() {
        // firebase req'd empty constructor
    }

    public StageModel(String url) {
        this.url = url;
        this.state = PAUSED;
        this.playedMillis = 0;
    }

    @Exclude
    public boolean isPlaying() {
        if (state == null) {
            Timber.w("cannot check state before initialization");
            return false;
        }
        return state.equals(PLAYING);
    }

    public long getPlayedMillis() {
        return playedMillis;
    }

    public void setPlayedMillis(long playedMillis) {
        this.playedMillis = playedMillis;
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

    @Override
    public int hashCode() {
        int num = 17;
        num *= 31 + Integer.parseInt(key);
        return num;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof StageModel)) return false;
        final StageModel other = (StageModel) obj;

        return key.equals(other.getKey());
    }
}
package com.dabkick.videosdk;


public class DabKickVideoInfo {

    private String videoUrl;

    private String authorName;

    public DabKickVideoInfo(String videoUrl, String authorName) {
        this.videoUrl = videoUrl;
        this.authorName = authorName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getAuthorName() {
        return authorName;
    }


}
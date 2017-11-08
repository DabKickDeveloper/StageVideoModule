package com.dabkick.videosdk;


public class DabKickVideoInfo {

    private String videoUrl;
    private String thumbnailUrl;
    private String authorName;
    private String duration;
    private String title;

    public DabKickVideoInfo(String author, String title, String duration, String thumbnailUrl, String Url) {
        this.videoUrl = Url;
        this.authorName = author;
        this.title = title;
        this.duration = duration;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getDuration() {
        return duration;
    }

    public String getTitle() {
        return title;
    }

}
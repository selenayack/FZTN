package com.app.fztn;

public class VideoItem {
    private String textBilgi;
    private String videoUrl;
    private String agriBolgesi;

    public VideoItem(String textBilgi, String videoUrl, String agriBolgesi) {
        this.textBilgi = textBilgi;
        this.videoUrl = videoUrl;
        this.agriBolgesi = agriBolgesi;
    }

    public String getTextBilgi() {
        return textBilgi;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getAgriBolgesi() {
        return agriBolgesi;
    }
}

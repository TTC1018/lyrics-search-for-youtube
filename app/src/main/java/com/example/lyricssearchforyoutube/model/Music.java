package com.example.lyricssearchforyoutube.model;

public class Music {
    private String title;
    private String artist;
    private String lyricsLink;

    public Music(){
        this.title = "";
        this.artist = "";
        this.lyricsLink = "";
    }

    public Music(String title, String artist, String lyricsLink) {
        this.title = title;
        this.artist = artist;
        this.lyricsLink = lyricsLink;
    }


    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getLyricsLink() {
        return lyricsLink;
    }
}

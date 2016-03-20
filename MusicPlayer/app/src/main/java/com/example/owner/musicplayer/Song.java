package com.example.owner.musicplayer;

/**
 * Created by Owner on 2016-03-18.
 */
public class Song {

    private long id;
    private String title;
    private String artist;

    public Song(long songID, String songTitle, String songArtist) {
        id = songID;
        title = songTitle;
        artist = songArtist;
    }

    public String getArtist() {
        return artist;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}

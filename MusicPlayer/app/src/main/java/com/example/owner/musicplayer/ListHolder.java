package com.example.owner.musicplayer;

import java.io.File;
import java.util.LinkedList;

/**
 * ListHolder is a singleton class that saves the play list of the user.
 * @author Andrew Jeong
 */
public class ListHolder {
    private LinkedList<File> songList;
    public ListHolder(){
        songList = new LinkedList<File>();
    }

    // Getter method for the play list
    public LinkedList<File> getSongList() {
        return songList;
    }
    
    private static ListHolder holder = new ListHolder();
    public static ListHolder getInstance() {
        return holder;
    }
}
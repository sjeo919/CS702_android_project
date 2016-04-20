package com.example.owner.musicplayer;

import java.io.File;
import java.util.LinkedList;

public class ListHolder {
    private LinkedList<File> songList;
    public ListHolder(){
        songList = new LinkedList<File>();
    }

    public LinkedList<File> getSongList() {
        return songList;
    }

    public void setSongList(LinkedList<File> sl) {
        songList = sl;
    }

    private static ListHolder holder = new ListHolder();
    public static ListHolder getInstance() {
        return holder;
    }
}

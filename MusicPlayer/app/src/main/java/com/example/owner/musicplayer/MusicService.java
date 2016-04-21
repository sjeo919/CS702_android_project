package com.example.owner.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * This Service class is responsible for dealing the media files in the background.
 * The class does not directly interact with the UI components
 * @author Andrew Jeong
 */

public class MusicService extends Service implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {

    private MediaPlayer mp;
    private File curSong;
    private int SONG_POS;
    private List<File> PLAYLIST;

    private final IBinder serviceBinder = new MyLocalBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        // start a new MediaPlayer instance
        mp = new MediaPlayer();
        // get the singleton instance of the playlist
        PLAYLIST = (List) ListHolder.getInstance().getSongList();
        SONG_POS = 0;
        mp.setOnErrorListener(this);
        mp.setOnPreparedListener(this);
    }

    /**
     * This method gets the media file as input and outputs the Uri of the file
     * @param song
     * @return Uri of the input media file
     */
    public Uri getmSongUri (File song) {
        curSong = song;
        return Uri.parse(curSong.toString());
    }

    /**
     * Procedures to be taken when Play/Pause button is pressed and change the button text
     * @return String of button text to be written on the button
     */
    public String playButton() {
        if (mp.isPlaying()) {
            mp.pause();
            return ">";
        } else {
            mp.start();
            return "||";
        }
    }

    /**
     * fast forward on the current media file. Jump to the next file if the end is reached.
     * @return true or false depending on whether the next media file is started or not
     */
    public boolean fastForward() {
        if (mp.getCurrentPosition()+5000 < mp.getDuration()) {
            mp.seekTo(mp.getCurrentPosition() + 5000);
            return false;
        } else {
            playNext();
            return true;

        }
    }

    /**
     * rewind on the current media file. Jump to the previous file if the end is reached.
     * @return true or false depending on whether the next media file is started or not
     */
    public boolean rewind() {
        if (mp.getCurrentPosition()-5000 > 0) {
            mp.seekTo(mp.getCurrentPosition() - 5000);
            return false;
        } else {
            playPrev();
            return true;
        }
    }

    /**
     * Primarily release the media player instance and find the position of the previous media file in the list.
     * Make the MediaPlayer object to point to the new media file then start.
     */
    public void playPrev() {
        mp.stop();
        mp.release();
        SONG_POS = (SONG_POS-1 < 0) ? PLAYLIST.size()-1 : SONG_POS-1;
        curSong = PLAYLIST.get(SONG_POS);
        mp = MediaPlayer.create(getApplicationContext(),getmSongUri(curSong));
        mp.start();
    }

    /**
     * Primarily release the media player instance and find the position of the next media file in the list.
     * Make the MediaPlayer object to point to the new media file then start.
     */
    public void playNext() {
        mp.stop();
        mp.release();
        SONG_POS = (SONG_POS + 1)%PLAYLIST.size();
        curSong = PLAYLIST.get(SONG_POS);
        mp = MediaPlayer.create(getApplicationContext(), getmSongUri(curSong));
        mp.start();
    }

    /**
     * This method finds and returns the bitmap image that is embedded in the media file.
     * @param uri of the media file
     * @param data is always null
     * @return Bitmap image to be used as the album art
     */
    public Bitmap findAlbumArt (Uri uri, byte[] data) {
        FFmpegMediaMetadataRetriever retriever = new FFmpegMediaMetadataRetriever();
        retriever.setDataSource(uri.toString());

        data = retriever.getEmbeddedPicture();

        if (data != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            return bitmap;
        } else {
            Bitmap bImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.albumdefault);
            return bImage;
        }
    }

    // Getter method for current song being played
    public File getCurSong() {
        return curSong;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    // Getter method for the media duration
    public int getMusicDuration() {
        return mp.getDuration();
    }

    // Getter method for current position in the media file
    public int getCurPosition() {
        return mp.getCurrentPosition();
    }

    /**
     * This method checks if the MediaPlayer is already playing, and if it is, toast a message then
     * stop and release the media player object
     */
    public void terminatePlayer() {
        if (mp != null) {
            if (mp.isPlaying()) {
                Toast.makeText(getApplicationContext(), "STOPPING CURRENTLY PLAYING SONGS", Toast.LENGTH_SHORT).show();
            }
            mp.stop();
            mp.release();
        }
    }

    /**
     * This method restarts the playlist when a media is already being played, and set the song
     * position to 0
     */
    public void restartPlaylist() {
        if (mp.isPlaying()) {
            mp.stop();
            mp.release();
            SONG_POS = 0;
        }
    }

    /**
     * This method points the current media player object to a new playlist and start playing
     */
    public void startPlayer() {
        mp = MediaPlayer.create(getApplicationContext(), getmSongUri(PLAYLIST.get(SONG_POS)));
        mp.start();
    }

    // This method jumps to input position of the media file
    public void jumpTo(int progress) {
        mp.seekTo(progress);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {    }

    /**
     * This declares MyLocalBinder that extends Binder class to implement Bound Service
     */
    public class MyLocalBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

}
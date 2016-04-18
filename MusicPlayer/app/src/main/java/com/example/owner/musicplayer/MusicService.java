package com.example.owner.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import java.io.File;
import java.util.List;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class MusicService extends Service implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {

    private MediaPlayer mp;
    private File curSong;
    private int SONG_POS;
    private List<File> PLAYLIST;
    private static final String TAG = "LogMessage";

    private final IBinder serviceBinder = new MyLocalBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        mp = new MediaPlayer();
        PLAYLIST = (List) ListHolder.getInstance().getSongList();
        SONG_POS = 0;
        mp.setOnErrorListener(this);
        mp.setOnPreparedListener(this);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //Stop the mediaplayer
        mp.stop();
        mp.release();
        return false;
    }

    public Uri getmSongUri (File song) {
        curSong = song;
        return Uri.parse(curSong.toString());
    }

    public String playButton() {
        if (mp.isPlaying()) {
            mp.pause();
            return ">";
        } else {
            mp.start();
            return "||";
        }
    }

    public boolean fastForward() {
        if (mp.getCurrentPosition()+5000 < mp.getDuration()) {
            mp.seekTo(mp.getCurrentPosition() + 5000);
            return false;
        } else {
            playNext();
            return true;

        }
    }

    public boolean rewind() {
        if (mp.getCurrentPosition()-5000 > 0) {
            mp.seekTo(mp.getCurrentPosition() - 5000);
            return false;
        } else {
            playPrev();
            return true;
        }
    }

    public void playPrev() {
        mp.stop();
        mp.release();
        SONG_POS = (SONG_POS-1 < 0) ? PLAYLIST.size()-1 : SONG_POS-1;
        curSong = PLAYLIST.get(SONG_POS);
        mp = MediaPlayer.create(getApplicationContext(),getmSongUri(curSong));
        mp.start();
    }

    public void playNext() {
        mp.stop();
        mp.release();
        SONG_POS = (SONG_POS + 1)%PLAYLIST.size();
        curSong = PLAYLIST.get(SONG_POS);
        mp = MediaPlayer.create(getApplicationContext(), getmSongUri(curSong));
        mp.start();
    }

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

    public int getPosition() {
        return SONG_POS;
    }

    public File getCurSong() {
        return curSong;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    public int getDuration() {
        return mp.getDuration();
    }

    public int getCurPosition() {
        return mp.getCurrentPosition();
    }

    public void init() {
        if (mp != null) {
            mp.stop();
            mp.release();
        }
    }

    public void startPlayer() {
        mp = MediaPlayer.create(getApplicationContext(), getmSongUri(PLAYLIST.get(SONG_POS)));
        mp.start();
    }

    public void seekTo(int progress) {
        mp.seekTo(progress);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    public class MyLocalBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }
}

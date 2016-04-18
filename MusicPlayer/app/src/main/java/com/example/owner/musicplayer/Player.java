package com.example.owner.musicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import com.example.owner.musicplayer.MusicService.MyLocalBinder;

public class Player extends AppCompatActivity implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private static final String TAG = "LogMessage";
    ArrayList<File> mySongs;
    int position;
    Thread updateSeekBar;

    SeekBar sb;
    Button btPlay, btFf, btRw, btNext, btPrev;
    TextView songName;
    ImageView albumArt;

    boolean isBound = false;
    MusicService musicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent i = new Intent(this, MusicService.class);
        bindService(i, musicConnection, Context.BIND_AUTO_CREATE);

        btPlay = (Button) findViewById(R.id.btPlay);
        btFf = (Button) findViewById(R.id.btFf);
        btRw = (Button) findViewById(R.id.btRw);
        btNext = (Button) findViewById(R.id.btNext);
        btPrev = (Button) findViewById(R.id.btPrev);
        songName = (TextView) findViewById(R.id.songName);

        btPlay.setOnClickListener(this);
        btFf.setOnClickListener(this);
        btRw.setOnClickListener(this);
        btNext.setOnClickListener(this);
        btPrev.setOnClickListener(this);

        albumArt = (ImageView) findViewById(R.id.albumView);

        sb = (SeekBar) findViewById(R.id.seekBar);
        updateSeekBar = new Thread() {
            @Override
            public void run() {
                int totalDuration = musicService.getDuration();
                int currentPosition = 0;
                sb.setMax(totalDuration);

                while (currentPosition < totalDuration) {
                    try {
                        Thread.sleep(500);
                        currentPosition = musicService.getCurPosition();
                        sb.setProgress(currentPosition);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        };

        Intent in = getIntent();
        Bundle b = in.getExtras();
        mySongs = (ArrayList) b.getParcelableArrayList("songlist");
        position = b.getInt("pos", 0);
        sb.setOnSeekBarChangeListener(this);
    }


    public void init() {
        musicService.startPlayer();
        songName.setText(musicService.getCurSong().getName().replace(".mp3", "").replace(".wav", ""));
        changeAlbumArt(musicService.getmSongUri(musicService.getCurSong()));
        sb.setMax(musicService.getDuration());
        updateSeekBar.start();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        musicService.seekTo(seekBar.getProgress());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    public void changeAlbumArt (Uri uri) {
        albumArt.setImageBitmap(musicService.findAlbumArt(uri, null));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btPlay:
                btPlay.setText(musicService.playButton());
                break;
            case R.id.btFf:
                if (musicService.fastForward()) {
                    changeAlbumArt(musicService.getmSongUri(musicService.getCurSong()));
                    songName.setText(musicService.getCurSong().getName().replace(".mp3", "").replace(".wav", ""));
                }
                break;
            case R.id.btRw:
                if (musicService.rewind()) {
                    changeAlbumArt(musicService.getmSongUri(musicService.getCurSong()));
                    songName.setText(musicService.getCurSong().getName().replace(".mp3", "").replace(".wav", ""));
                }
                break;
            case R.id.btNext:
                musicService.playNext();
                changeAlbumArt(musicService.getmSongUri(musicService.getCurSong()));
                songName.setText(musicService.getCurSong().getName().replace(".mp3", "").replace(".wav", ""));
//                sb.setProgress(0);
                sb.setMax(musicService.getDuration());
//                updateSeekBar.start();
                break;
            case R.id.btPrev:
                musicService.playPrev();
                changeAlbumArt(musicService.getmSongUri(musicService.getCurSong()));
                songName.setText(musicService.getCurSong().getName().replace(".mp3", "").replace(".wav", ""));
//                sb.setProgress(0);
                sb.setMax(musicService.getDuration());
//                updateSeekBar.start();
                break;
        }
    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyLocalBinder binder = (MyLocalBinder) service;
            musicService = binder.getService();
            isBound = true;
            init();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i(TAG, "OnCompletion method entered!");
    }
}

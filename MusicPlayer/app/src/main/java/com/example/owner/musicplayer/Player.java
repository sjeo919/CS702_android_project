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

/**
 * Player.java is responsible for the UI and the direct interactions that take place in
 * Player activity (such as playback functions)
 * @author Andrew Jeong
 */

public class Player extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private static final String TAG = "LogMessage";
    private ArrayList<File> mySongs;
    private int position;
    private Thread updateSeekBar;

    private SeekBar sb;
    private Button btPlay, btFf, btRw, btNext, btPrev;
    private TextView songName;
    private ImageView albumArt;

    private boolean isBound = false;
    private MusicService musicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // Player class binds to the MusicService class
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
        sb.setOnSeekBarChangeListener(this);

        // a thread updates the seekbar while the media is being played
        updateSeekBar = new Thread() {
            @Override
            public void run() {
                int totalDuration = musicService.getMusicDuration();
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

        // retrieve the bundle that was sent from Playlist class
        Intent in = getIntent();
        Bundle b = in.getExtras();
        mySongs = (ArrayList) b.getParcelableArrayList("songlist");
        position = b.getInt("pos", 0);
    }

    /**
     * initialises the player. Gets the mediaplayer ready to run and loads the name and the album
     * art of the media file.
     */
    public void init() {
        musicService.startPlayer();
        songName.setText(musicService.getCurSong().getName().replace(".mp3", "").replace(".wav", ""));
        changeAlbumArt(musicService.getmSongUri(musicService.getCurSong()));
        sb.setMax(musicService.getMusicDuration());
        // thread starts running to update the seekbar
        updateSeekBar.start();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        musicService.jumpTo(seekBar.getProgress());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {    }

    /**
     * sets the ImageView to show the album art embedded in the media file
     * @param uri
     */
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

    /**
     * This method reads which button was interacted with the user and carries out appropriate tasks.
      * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btPlay:
                // clicking play button either plays or pauses the media and changes the button text
                btPlay.setText(musicService.playButton());
                break;
            case R.id.btFf:
                // fast forward the currently playing media by 500ms
                if (musicService.fastForward()) {
                    // when fast forwarded at the end of the file, jump to the next media file in the playlist
                    // and change the album art.
                    changeAlbumArt(musicService.getmSongUri(musicService.getCurSong()));
                    songName.setText(musicService.getCurSong().getName().replace(".mp3", "").replace(".wav", ""));
                    sb.setMax(musicService.getMusicDuration());
                    // in case the media was paused then fast forwarded, change button text when the next media starts
                    btPlay.setText("||");
                }
                break;
            case R.id.btRw:
                // rewind the currently playing media by 500ms
                if (musicService.rewind()) {
                    // when rewound at the start of the file, jump to the next media file in the playlist
                    // and change the album art and the song name.
                    changeAlbumArt(musicService.getmSongUri(musicService.getCurSong()));
                    songName.setText(musicService.getCurSong().getName().replace(".mp3", "").replace(".wav", ""));
                    sb.setMax(musicService.getMusicDuration());
                    // in case the media was paused then rewound, change button text when the next media starts
                    btPlay.setText("||");
                }
                break;
            case R.id.btNext:
                // play the next media file then change the album art and the song name
                musicService.playNext();
                changeAlbumArt(musicService.getmSongUri(musicService.getCurSong()));
                songName.setText(musicService.getCurSong().getName().replace(".mp3", "").replace(".wav", ""));
                // reset the seekbar max value then change the button text
                sb.setMax(musicService.getMusicDuration());
                btPlay.setText("||");
                break;
            case R.id.btPrev:
                // play the previous media file then change the album art and the song name.
                musicService.playPrev();
                changeAlbumArt(musicService.getmSongUri(musicService.getCurSong()));
                songName.setText(musicService.getCurSong().getName().replace(".mp3", "").replace(".wav", ""));
                // reset the seekbar max value then change the button text
                sb.setMax(musicService.getMusicDuration());
                btPlay.setText("||");
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
}
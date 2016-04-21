package com.example.owner.musicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.LinkedList;

/**
 * Playlist.java is reponsible for displaying the list items in the playlist. The user can either
 * start playing the songs in the playlist or clear the current list.
 * @author Andrew Jeong
 */
public class Playlist extends AppCompatActivity implements View.OnClickListener {

    boolean isBound = false;
    private MusicService musicService;
    private Runnable run;
    private LinkedList<File> playList;
    private ArrayAdapter<String> adp;
    private String[] items;
    private Button playButton, clearButton, playerButton;
    private ListView pl;
    private static final String TAG = "LogMessage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        // Playlist class binds to the MusicService class
        Intent i = new Intent(this, MusicService.class);
        bindService(i, musicConnection, Context.BIND_AUTO_CREATE);

        playButton = (Button) findViewById(R.id.playButton);
        clearButton = (Button) findViewById(R.id.clearButton);

        pl = (ListView) findViewById(R.id.lvPlaylist);

        // A runnable that refreshes the current activity
        run = new Runnable() {
            @Override
            public void run() {
                pl.setAdapter(adp);
                adp.notifyDataSetChanged();
                pl.invalidateViews();
                pl.refreshDrawableState();

            }
        };

        clearButton.setOnClickListener(this);
        playButton.setOnClickListener(this);

        // get the singleton instance of the playlist
        playList = ListHolder.getInstance().getSongList();

        items = extractNames(playList);

        // instantiate array adapter for each list item in the ListView, and register it to the ListView
        adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.song_layout, R.id.textView, items);
        pl.setAdapter(adp);
    }

    /**
     * This method extracts the file names without file extensions from the each file
     * @param list of songs
     * @return String array of the song names
     */
    public String[] extractNames(LinkedList<File> list) {
        items = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            items[i] = list.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
        }
        return items;
    }

    /**
     * When Playlist activity is revisited from Player activity, run this method
     */
    @Override
    protected void onResume() {
        super.onResume();
        playList = ListHolder.getInstance().getSongList();
        Log.i(TAG, "Resume is working");
        refreshContent();
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
     * This method runs the Runnable that was created in onCreate method and runs it on EDT
     */
    public void refreshContent() {  runOnUiThread(run); }

    /**
     * This method reads which button was interacted with the user and carries out appropriate tasks.
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.playButton:
                // start playing the playlist if it is not empty
                if (playList.size() != 0) {
                    // if media player is already running, terminate(release) it and start a new media player
                    musicService.restartPlaylist();
                    startActivity(new Intent(getApplicationContext(), Player.class).putExtra("pos", 0).putExtra("songlist", playList));
                } else {
                    // if the list is empty, toast a message
                    Toast.makeText(getApplicationContext(), "NO SONG IN THE PLAYLIST, CANNOT START PLAYING.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.clearButton:
                // clear the play list
                if (playList.size() != 0) {
                    // if the play list is not empty, stop whatever is being played and clear the play list
                    // then update the string array of song names and re-instantiate the adapter for the ListView
                    musicService.terminatePlayer();
                    playList.clear();
                    items = extractNames(playList);
                    adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.song_layout, R.id.textView, items);
                    // refresh the activity
                    refreshContent();
                } else {
                    // if the play list is already empty, toast a message
                    Toast.makeText(getApplicationContext(), "THE PLAYLIST IS ALREADY EMPTY", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    // Make a ServiceConnection instance that connects to the MusicService class
    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MyLocalBinder binder = (MusicService.MyLocalBinder) service;
            musicService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
}
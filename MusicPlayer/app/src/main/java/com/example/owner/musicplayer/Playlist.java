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

public class Playlist extends AppCompatActivity implements View.OnClickListener {

    boolean isBound = false;
    MusicService musicService;
    private Runnable run;
    private LinkedList<File> playList;
    private ArrayAdapter<String> adp;
    String[] items;
    Button playButton, clearButton, playerButton;
    ListView pl;
    private static final String TAG = "LogMessage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        Intent i = new Intent(this, MusicService.class);
        bindService(i, musicConnection, Context.BIND_AUTO_CREATE);

        playButton = (Button) findViewById(R.id.playButton);
        clearButton = (Button) findViewById(R.id.clearButton);
//        playerButton = (Button) findViewById(R.id.btPlayer);
        pl = (ListView) findViewById(R.id.lvPlaylist);

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
//        playerButton.setOnClickListener(this);

        playList = ListHolder.getInstance().getSongList();

        items = extractNames(playList);

        adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.song_layout, R.id.textView, items);
        pl.setAdapter(adp);
    }

    public String[] extractNames(LinkedList<File> list) {
        items = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            items[i] = list.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
        }
        return items;
    }

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

    public void refreshContent() {  runOnUiThread(run); }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.playButton:
                if (playList.size() != 0) {
                    musicService.restartPlaylist();
                    startActivity(new Intent(getApplicationContext(), Player.class).putExtra("pos", 0).putExtra("songlist", playList));
                } else {
                    Toast.makeText(getApplicationContext(), "No song in the playlist, Cannot start playing.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.clearButton:
                if (playList.size() != 0) {
                    musicService.terminatePlayer();
                    playList.clear();
                    items = extractNames(playList);
                    adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.song_layout, R.id.textView, items);
                    refreshContent();
                } else {
                    Toast.makeText(getApplicationContext(), "The playlist is already empty", Toast.LENGTH_SHORT).show();
                }
                break;
//            case R.id.btPlayer:
//                Intent i = new Intent(getApplicationContext(), Player.class);
//                startActivityForResult(i, 0);
        }
    }

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
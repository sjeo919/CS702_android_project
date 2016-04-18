package com.example.owner.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.LinkedList;

public class Playlist extends AppCompatActivity implements View.OnClickListener {

    private Thread runOnUiThread;
    private Runnable run;
    private LinkedList<File> playList;
    private ArrayAdapter<String> adp;
    String[] items;
    Button playButton;
    ListView pl;
    private static final String TAG = "LogMessage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        runOnUiThread = new Thread();
        playButton = (Button) findViewById(R.id.playButton);
        pl = (ListView) findViewById(R.id.lvPlaylist);

        run = new Runnable() {
            @Override
            public void run() {
                adp.notifyDataSetChanged();
                pl.invalidateViews();
                pl.refreshDrawableState();

            }
        };

        playButton.setOnClickListener(this);
        playList = ListHolder.getInstance().getSongList();
        Log.i(TAG, "22The size of the list is: " + playList.size());

        items = new String[playList.size()];
        for (int i = 0; i < playList.size(); i++) {
            items[i] = playList.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
        }

        adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.song_layout, R.id.textView, items);
        pl.setAdapter(adp);
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

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), Player.class).putExtra("pos", 0).putExtra("songlist", playList));
    }

    public void refreshContent() {
        runOnUiThread(run);
    }
}

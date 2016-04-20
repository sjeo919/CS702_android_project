package com.example.owner.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * MainActivity.java iterates through the directory tree of the device and lists all the mp3 and wav
 * files on the ListView. Songs can be added to the play list by clicking on them
 * @author Andrew Jeong
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LogMessage";

    private Button bt_playlist;
    private LinkedList<File> playList;
    private ArrayAdapter<String> adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final ListView lv;
        final String[] items;
        String a = "";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.allSongs);
        bt_playlist = (Button) findViewById(R.id.btPlayer);

        // When Playlist>> button is pressed, open the Playlist activity
        bt_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Playlist.class);
                startActivity(i);
            }
        });

        // get the instance of the play list
        playList = ListHolder.getInstance().getSongList();
        // find all the mp3 and wav media files on the device
        final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());
        // extract song names from the file names
        items = new String[mySongs.size()];
        for (int i = 0; i < mySongs.size(); i++) {
            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
        }

        // instantiate array adapter for each list item in the ListView, and register it to the ListView
        adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.song_layout, R.id.textView, items);
        lv.setAdapter(adp);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // for each clicked list item, add the item to the end of the play list
                File selectedItem = mySongs.get(position);
                playList.addLast(selectedItem);
                Log.i(TAG, "The size of the list is: " + playList.size());
                // toast a message to indicate that the song was added successfully
                Toast.makeText(getApplicationContext(), items[position] + " IS ADDED TO THE QUEUE", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method iterates through the directory tree on the device to find all the files that
     * ends with .mp3 or .wav
     * @param root directory of the device
     * @return ArrayList<File> of all the songs found
     */
    public ArrayList<File> findSongs(File root) {
        ArrayList<File> al = new ArrayList<File>();
        File[] files = root.listFiles();
        if(files != null){
            for (File singleFile : files) {
                if (singleFile.isDirectory() && !singleFile.isHidden()) {
                    al.addAll(findSongs(singleFile));
                } else {
                    if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {
                        al.add(singleFile);
                    }
                }
            }
        }
        return al;
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
}
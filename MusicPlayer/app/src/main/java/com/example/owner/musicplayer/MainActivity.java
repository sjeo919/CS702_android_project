package com.example.owner.musicplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import android.util.Log;

import com.example.owner.musicplayer.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
        bt_playlist = (Button) findViewById(R.id.btPlaylist);

        bt_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Playlist.class);
                startActivity(i);
            }
        });

        playList = ListHolder.getInstance().getSongList();
        final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];
        for (int i = 0; i < mySongs.size(); i++) {
            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
        }


        adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.song_layout, R.id.textView, items);
        lv.setAdapter(adp);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File selectedItem = mySongs.get(position);
                playList.addLast(selectedItem);
                Log.i(TAG, "The size of the list is: " + playList.size());
                Toast.makeText(getApplicationContext(), items[position] + " is added to the queue", Toast.LENGTH_SHORT).show();
            }
        });
    }
//
//    class MyTask extends AsyncTask<Void, String, String> {
//
//        ArrayAdapter<String> adapter;
//
//        @Override
//        protected void onPreExecute() {
//            adapter = (ArrayAdapter<String>) fav.getAdapter();
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            int count = adapter.getCount();
//            if (count < playList.size()) {
//                publishProgress(playList.getLast().getName().toString());
//            }
//            return playList.getLast().getName().toString() + "is added to the queue";
//        }
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//            try {
//                adapter.add(values[0]);
//            } catch (UnsupportedOperationException e) {
//                e.printStackTrace();
//            }
//            adapter.notifyDataSetChanged();
//            fav.invalidateViews();
//            fav.refreshDrawableState();
//        }
//
//            @Override
//        protected void onPostExecute(String s) {
//                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
//        }
//    }

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

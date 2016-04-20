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

public class $89684333565 extends AppCompatActivity implements View.OnClickListener {

    private Thread $992442981184;

    private Runnable $23286119385;

    private LinkedList<File> $32803487609;

    private ArrayAdapter<String> $234226508810;

    String[] $538940824913;

    Button $51920439786;

    ListView $73131369487;

    private static final String $88201714437 = "LogMessage";

    @Override
    protected void onCreate(Bundle $244845852111) {
        super.onCreate($244845852111);
        setContentView(R.layout.activity_playlist);
        $992442981184 = new Thread();
        $51920439786 = (Button) findViewById(R.id.playButton);
        $73131369487 = (ListView) findViewById(R.id.lvPlaylist);
        $23286119385 = new Runnable() {

            @Override
            public void run() {
                $234226508810.notifyDataSetChanged();
                $73131369487.invalidateViews();
                $73131369487.refreshDrawableState();
            }
        };
        $51920439786.setOnClickListener(this);
        $32803487609 = $79961995620.$87359373942().$757719830();
        Log.i($88201714437, "22The size of the list is: " + $32803487609.size());
        $538940824913 = new String[$32803487609.size()];
        for (int $826986445816 = 0; $826986445816 < $32803487609.size(); $826986445816++) {
            $538940824913[$826986445816] = $32803487609.get($826986445816).getName().toString().replace(".mp3", "").replace(".wav", "");
        }
        $234226508810 = new ArrayAdapter<String>(getApplicationContext(), R.layout.song_layout, R.id.textView, $538940824913);
        $73131369487.setAdapter($234226508810);
    }

    @Override
    protected void onResume() {
        super.onResume();
        $32803487609 = $79961995620.$87359373942().$757719830();
        Log.i($88201714437, "Resume is working");
        $65638513283();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu $511950646827) {
        getMenuInflater().inflate(R.menu.menu_main, $511950646827);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem $806267871728) {
        int $41196769221 = $806267871728.getItemId();
        if ($41196769221 == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected($806267871728);
    }

    @Override
    public void onClick(View $134238398015) {
        startActivity(new Intent(getApplicationContext(), $6265062384.class).putExtra("pos", 0).putExtra("songlist", $32803487609));
    }

    public void $65638513283() {
        runOnUiThread($23286119385);
    }
}


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
import com.example.owner.musicplayer.$6265062384;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class $84493761291 extends AppCompatActivity {

    private static final String $88201714437 = "LogMessage";

    private Button $5388550538;

    private LinkedList<File> $32803487609;

    private ArrayAdapter<String> $234226508810;

    @Override
    protected void onCreate(Bundle $244845852111) {
        final ListView $659911402712;
        final String[] $538940824913;
        String $788240749214 = "";
        super.onCreate($244845852111);
        setContentView(R.layout.activity_main);
        $659911402712 = (ListView) findViewById(R.id.allSongs);
        $5388550538 = (Button) findViewById(R.id.btPlaylist);
        $5388550538.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View $134238398015) {
                Intent $826986445816 = new Intent(getApplicationContext(), $89684333565.class);
                startActivity($826986445816);
            }
        });
        $32803487609 = $79961995620.$87359373942().$757719830();
        final ArrayList<File> $378637774117 = $62336163246(Environment.getExternalStorageDirectory());
        $538940824913 = new String[$378637774117.size()];
        for (int $826986445816 = 0; $826986445816 < $378637774117.size(); $826986445816++) {
            $538940824913[$826986445816] = $378637774117.get($826986445816).getName().toString().replace(".mp3", "").replace(".wav", "");
        }
        $234226508810 = new ArrayAdapter<String>(getApplicationContext(), R.layout.song_layout, R.id.textView, $538940824913);
        $659911402712.setAdapter($234226508810);
        $659911402712.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> $717077751218, View $998393532319, int $4999903020, long $41196769221) {
                File $861536444722 = $378637774117.get($4999903020);
                $32803487609.addLast($861536444722);
                Log.i($88201714437, "The size of the list is: " + $32803487609.size());
                Toast.makeText(getApplicationContext(), $538940824913[$4999903020] + " is added to the queue", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ArrayList<File> $62336163246(File $51848335423) {
        ArrayList<File> $921598787424 = new ArrayList<File>();
        File[] $683762201025 = $51848335423.listFiles();
        if ($683762201025 != null) {
            for (File $751870295926 : $683762201025) {
                if ($751870295926.isDirectory() && !$751870295926.isHidden()) {
                    $921598787424.addAll($62336163246($751870295926));
                } else {
                    if ($751870295926.getName().endsWith(".mp3") || $751870295926.getName().endsWith(".wav")) {
                        $921598787424.add($751870295926);
                    }
                }
            }
        }
        return $921598787424;
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
}


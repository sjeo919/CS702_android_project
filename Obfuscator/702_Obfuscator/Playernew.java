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
import com.example.owner.musicplayer.$6391464523.$7971815252;

public class $6265062384 extends AppCompatActivity implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private static final String $88201714437 = "LogMessage";

    ArrayList<File> $378637774117;

    int $4999903020;

    Thread $28393701861;

    SeekBar $476636285662;

    Button $827597346463, $862492651164, $689912214565, $672799537466, $67213538567;

    TextView $862058421168;

    ImageView $339240129069;

    boolean $89877868770 = false;

    $6391464523 $62906903971;

    @Override
    protected void onCreate(Bundle $244845852111) {
        super.onCreate($244845852111);
        setContentView(R.layout.activity_player);
        Intent $826986445816 = new Intent(this, $6391464523.class);
        bindService($826986445816, $26689507479, Context.BIND_AUTO_CREATE);
        $827597346463 = (Button) findViewById(R.id.btPlay);
        $862492651164 = (Button) findViewById(R.id.btFf);
        $689912214565 = (Button) findViewById(R.id.btRw);
        $672799537466 = (Button) findViewById(R.id.btNext);
        $67213538567 = (Button) findViewById(R.id.btPrev);
        $862058421168 = (TextView) findViewById(R.id.songName);
        $827597346463.setOnClickListener(this);
        $862492651164.setOnClickListener(this);
        $689912214565.setOnClickListener(this);
        $672799537466.setOnClickListener(this);
        $67213538567.setOnClickListener(this);
        $339240129069 = (ImageView) findViewById(R.id.albumView);
        $476636285662 = (SeekBar) findViewById(R.id.seekBar);
        $28393701861 = new Thread() {

            @Override
            public void run() {
                int $907298663072 = $62906903971.$34623473639();
                int $897532918673 = 0;
                $476636285662.setMax($907298663072);
                while ($897532918673 < $907298663072) {
                    try {
                        Thread.sleep(500);
                        $897532918673 = $62906903971.$3096450240();
                        $476636285662.setProgress($897532918673);
                    } catch (Exception $375867608174) {
                        $375867608174.printStackTrace();
                    }
                }
            }
        };
        Intent $814349929075 = getIntent();
        Bundle $713079646276 = $814349929075.getExtras();
        $378637774117 = (ArrayList) $713079646276.getParcelableArrayList("songlist");
        $4999903020 = $713079646276.getInt("pos", 0);
        $476636285662.setOnSeekBarChangeListener(this);
    }

    public void $07547692959() {
        $62906903971.$876510471642();
        $862058421168.setText($62906903971.$23725352138().getName().replace(".mp3", "").replace(".wav", ""));
        $788166892260($62906903971.$744858512930($62906903971.$23725352138()));
        $476636285662.setMax($62906903971.$34623473639());
        $28393701861.start();
    }

    @Override
    public void onStopTrackingTouch(SeekBar $499734636577) {
        $62906903971.$761123987643($499734636577.getProgress());
    }

    @Override
    public void onProgressChanged(SeekBar $499734636577, int $12205837456, boolean $32715833278) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar $499734636577) {
    }

    public void $788166892260(Uri $2723925951) {
        $339240129069.setImageBitmap($62906903971.$85982550336($2723925951, null));
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
        int $41196769221 = $134238398015.getId();
        switch($41196769221) {
            case R.id.btPlay:
                $827597346463.setText($62906903971.$616992377231());
                break;
            case R.id.btFf:
                if ($62906903971.$98971152332()) {
                    $788166892260($62906903971.$744858512930($62906903971.$23725352138()));
                    $862058421168.setText($62906903971.$23725352138().getName().replace(".mp3", "").replace(".wav", ""));
                }
                break;
            case R.id.btRw:
                if ($62906903971.$965615482233()) {
                    $788166892260($62906903971.$744858512930($62906903971.$23725352138()));
                    $862058421168.setText($62906903971.$23725352138().getName().replace(".mp3", "").replace(".wav", ""));
                }
                break;
            case R.id.btNext:
                $62906903971.$82557734035();
                $788166892260($62906903971.$744858512930($62906903971.$23725352138()));
                $862058421168.setText($62906903971.$23725352138().getName().replace(".mp3", "").replace(".wav", ""));
                $476636285662.setMax($62906903971.$34623473639());
                break;
            case R.id.btPrev:
                $62906903971.$961918409034();
                $788166892260($62906903971.$744858512930($62906903971.$23725352138()));
                $862058421168.setText($62906903971.$23725352138().getName().replace(".mp3", "").replace(".wav", ""));
                $476636285662.setMax($62906903971.$34623473639());
                break;
        }
    }

    private ServiceConnection $26689507479 = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName $879978613480, IBinder $53745544681) {
            $7971815252 $5000406582 = ($7971815252) $53745544681;
            $62906903971 = $5000406582.$26140484829();
            $89877868770 = true;
            $07547692959();
        }

        @Override
        public void onServiceDisconnected(ComponentName $879978613480) {
            $89877868770 = false;
        }
    };

    @Override
    public void onCompletion(MediaPlayer $38996095044) {
        Log.i($88201714437, "OnCompletion method entered!");
    }
}


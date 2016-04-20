package com.example.owner.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import java.io.File;
import java.util.List;
import wseemann.media.FFmpegMediaMetadataRetriever;

public class $6391464523 extends Service implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {

    private MediaPlayer $38996095044;

    private File $167843139545;

    private int $27851542146;

    private List<File> $8507372547;

    private static final String $88201714437 = "LogMessage";

    private final IBinder $04854304948 = new $7971815252();

    @Override
    public void onCreate() {
        super.onCreate();
        $38996095044 = new MediaPlayer();
        $8507372547 = (List) $79961995620.$87359373942().$757719830();
        $27851542146 = 0;
        $38996095044.setOnErrorListener(this);
        $38996095044.setOnPreparedListener(this);
    }

    @Override
    public boolean onUnbind(Intent $48127894249) {
        $38996095044.stop();
        $38996095044.release();
        return false;
    }

    public Uri $744858512930(File $153960254450) {
        $167843139545 = $153960254450;
        return Uri.parse($167843139545.toString());
    }

    public String $616992377231() {
        if ($38996095044.isPlaying()) {
            $38996095044.pause();
            return ">";
        } else {
            $38996095044.start();
            return "||";
        }
    }

    public boolean $98971152332() {
        if ($38996095044.getCurrentPosition() + 5000 < $38996095044.$34623473639()) {
            $38996095044.$761123987643($38996095044.getCurrentPosition() + 5000);
            return false;
        } else {
            $82557734035();
            return true;
        }
    }

    public boolean $965615482233() {
        if ($38996095044.getCurrentPosition() - 5000 > 0) {
            $38996095044.$761123987643($38996095044.getCurrentPosition() - 5000);
            return false;
        } else {
            $961918409034();
            return true;
        }
    }

    public void $961918409034() {
        $38996095044.stop();
        $38996095044.release();
        $27851542146 = ($27851542146 - 1 < 0) ? $8507372547.size() - 1 : $27851542146 - 1;
        $167843139545 = $8507372547.get($27851542146);
        $38996095044 = MediaPlayer.create(getApplicationContext(), $744858512930($167843139545));
        $38996095044.start();
    }

    public void $82557734035() {
        $38996095044.stop();
        $38996095044.release();
        $27851542146 = ($27851542146 + 1) % $8507372547.size();
        $167843139545 = $8507372547.get($27851542146);
        $38996095044 = MediaPlayer.create(getApplicationContext(), $744858512930($167843139545));
        $38996095044.start();
    }

    public Bitmap $85982550336(Uri $2723925951, byte[] $17703952) {
        FFmpegMediaMetadataRetriever $51061409653 = new FFmpegMediaMetadataRetriever();
        $51061409653.setDataSource($2723925951.toString());
        $17703952 = $51061409653.getEmbeddedPicture();
        if ($17703952 != null) {
            Bitmap $41129059954 = BitmapFactory.decodeByteArray($17703952, 0, $17703952.length);
            return $41129059954;
        } else {
            Bitmap $19117255455 = BitmapFactory.decodeResource(this.getResources(), R.drawable.albumdefault);
            return $19117255455;
        }
    }

    public int $932185924437() {
        return $27851542146;
    }

    public File $23725352138() {
        return $167843139545;
    }

    @Override
    public IBinder onBind(Intent $48127894249) {
        return $04854304948;
    }

    public int $34623473639() {
        return $38996095044.$34623473639();
    }

    public int $3096450240() {
        return $38996095044.getCurrentPosition();
    }

    public void $07547692959() {
        if ($38996095044 != null) {
            $38996095044.stop();
            $38996095044.release();
        }
    }

    public void $876510471642() {
        $38996095044 = MediaPlayer.create(getApplicationContext(), $744858512930($8507372547.get($27851542146)));
        $38996095044.start();
    }

    public void $761123987643(int $12205837456) {
        $38996095044.$761123987643($12205837456);
    }

    @Override
    public boolean onError(MediaPlayer $38996095044, int $345165489157, int $422552625558) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer $38996095044) {
    }

    public class $7971815252 extends Binder {

        $6391464523 $26140484829() {
            return $6391464523.this;
        }
    }
}


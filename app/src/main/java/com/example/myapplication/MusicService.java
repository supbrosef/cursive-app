package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.example.nick.cursiveapp.R;

public class MusicService extends Service {
    public static boolean isPlaying;
    MediaPlayer player;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this, R.raw.happyclappy);
        player.setLooping(true); // Set looping
        player.setVolume(100,100);
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();
        isPlaying = true;
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isPlaying = false;
        player.stop();
        player.release();
        player = null;
    }

    @Override
    public void onLowMemory() {
    }
}
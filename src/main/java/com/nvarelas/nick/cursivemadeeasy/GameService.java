package com.nvarelas.nick.cursivemadeeasy;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class GameService extends Service {
    MediaPlayer player;
    public static boolean isPlaying;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this, R.raw.westinafrica);
        player.setLooping(true); // Set looping
        player.setVolume(100,100);
        //player.prepareAsync();
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
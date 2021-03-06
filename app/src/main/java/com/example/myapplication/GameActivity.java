package com.example.myapplication;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.nick.cursiveapp.R;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private TextView letterSet, timerValue, mistakeValue;
    private TextView txt[] = new TextView[26];
    private int position, mistake = 0;
    private char[] letterArray = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private ArrayList<Character> list = new ArrayList<Character>();
    public int myRecord,value;
    Random rand = new Random();
    Handler customHandler = new Handler();
    Animation animShake;
    Button okButton;
    public String tValue;
    public boolean isBetter = false;
    FragmentManager fm = getSupportFragmentManager();
    ArrayList<Character> letterlist = new ArrayList<>();
    LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
    long startTime = 0L, timeInMilliseconds = 0L, timeSwapBuff = 0L, updateTime = 0L;
    MediaPlayer mp;


    Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updateTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updateTime / 1000);
            int mins = secs / 60;
            secs %= 60;
            int milliseconds = (int) (updateTime % 100);
            tValue = "" + String.format("%02d", mins) + ":" + String.format("%02d", secs) + ":"
                    + String.format("%02d", milliseconds);
            timerValue.setText(tValue);
            customHandler.postDelayed(this, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        letterSet = findViewById(R.id.textView29);
        timerValue = findViewById(R.id.timerValue);
        mistakeValue = findViewById(R.id.mistakeValue);
        animShake = AnimationUtils.loadAnimation(this, R.anim.shake);

        mp = MediaPlayer.create(GameActivity.this, R.raw.ding);

        //Turn off HW Accel on textview to avoid openGLRenderer error with large fonts
        letterSet.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        //set up buttons
        for (int i = 0; i < 26; i++) {
            txt[i] = new TextView(this);
            String idName = "textView" + Character.toString(letterArray[i]);
            txt[i] = findViewById(getResources().getIdentifier(idName, "id", getPackageName()));
            txt[i].setOnClickListener(onClickListener);
        }

        //add 52 letters to list
        for(int i=0;i<52;i++){
            list.add(letterArray[i]);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.fragment_gamestart, null);
        builder.setView(customLayout);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        OnStartFragment dialogFragment = new OnStartFragment();
        dialogFragment.setCancelable(false);
        dialogFragment.show(fm,"ww");
        myRecord= getValue("record");

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.ding);
    }

    public void startGame(){
        changeLetter();
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread,0);
    }

    private void changeLetter() {
        list.remove(position);
        if(list.isEmpty()){
            customHandler.removeCallbacks(updateTimerThread);
            letterSet.setText("");
            value = numValue(tValue);
            if(value < myRecord || myRecord == 0){
                isBetter = true;
            }
            OnCompleteFragment completeDialog = new OnCompleteFragment();
            completeDialog.setCancelable(false);
            completeDialog.show(fm,"");
        }
        else{
            position = rand.nextInt(list.size());
            String letter = Character.toString(list.get(position));
            letterSet.setText(letter);
        }

    }

    private int numValue(String val){
        int retVal  = Integer.valueOf(val.replaceAll("[^0-9]",""));
        return retVal;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String viewId = view.getResources().getResourceEntryName(view.getId());
            if (viewId.charAt(viewId.length() - 1) == Character.toLowerCase(list.get(position))) {
                changeLetter();
                if(mp.isPlaying()){
                    //mp.stop();
                    mp.seekTo(0);
                }
                else
                    mp.start();
            } else {
                mistake++;
                mistakeValue.setText(Integer.toString(mistake));
                letterSet.startAnimation(animShake);
            }
        }
    };


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
    }

    public int getValue(String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getInt(value, 0);
    }

    public void setValue(String value, int newValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(value, newValue);
        editor.apply();
    }


}


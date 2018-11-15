package com.example.nick.cursiveapp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;

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

    Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updateTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updateTime / 1000);
            int mins = secs / 60;
            secs %= 60;
            int milliseconds = (int) (updateTime % 1000);
            tValue = "" + String.format("%02d", mins) + ":" + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds);
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

        for (int i = 0; i < 26; i++) {
            txt[i] = new TextView(this);
            String idName = "textView" + Character.toString(letterArray[i]);
            txt[i] = findViewById(getResources().getIdentifier(idName, "id", getPackageName()));
            txt[i].setOnClickListener(onClickListener);
        }

        for(int i=0;i<5;i++){
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
        dialogFragment.show(fm,"");
        myRecord= getValue("record");
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


package com.example.nick.cursiveapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


//https://code.tutsplus.com/tutorials/android-sdk-create-a-drawing-app-touch-interaction--mobile-19202

public class MainActivity extends AppCompatActivity {

    private int myRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView recordText = (TextView) findViewById(R.id.recordtext);
        loadAnimation();
        myRecord = getValue("record");
        if (myRecord > 0){
            String rec = String.format("%07d", myRecord);
            rec = rec.substring(0,2) + ":" + rec.substring(2,4) + ":" + rec.substring(4,7);
            recordText.setText(rec);
        }
    }

    public void openTrace(View v){
        Intent tracePage = new Intent (this, TraceActivity.class);
        this.startActivity(tracePage);
    }

    public void openGame(View v){
        Intent gamePage = new Intent(this, GameActivity.class);
        this.startActivity(gamePage);
    }

    public void loadAnimation(){
        String cursive = getString(R.string.cursive);
        String made = getString(R.string.made);
        String easy = getString(R.string.easy);

        SpannableStringBuilder spanMade = new SpannableStringBuilder(made);
        SpannableStringBuilder spanEasy = new SpannableStringBuilder(easy);

        spanMade.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 4, 0);
        spanEasy.setSpan(new ForegroundColorSpan(Color.RED), 0, 4, 0);

        TextView textViewcursive = (TextView) findViewById(R.id.textViewcursive);
        TextView textViewmade = (TextView) findViewById(R.id.textViewmade);
        TextView textVieweasy = (TextView) findViewById(R.id.textVieweasy);

        textViewmade.setText(spanMade);
        textVieweasy.setText(spanEasy);

        Animation fadeCursive = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in);
        Animation fadeMade = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in);
        Animation fadeEasy = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in);
        fadeCursive.setStartOffset(1000);
        fadeMade.setStartOffset(2000);
        fadeEasy.setStartOffset(3000);
        textViewcursive.startAnimation(fadeCursive);
        textViewmade.startAnimation(fadeMade);
        textVieweasy.startAnimation(fadeEasy);
    }

    public int getValue(String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getInt(value, 0);
    }

}

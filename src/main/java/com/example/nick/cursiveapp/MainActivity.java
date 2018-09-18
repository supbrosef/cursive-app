package com.example.nick.cursiveapp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;


//https://code.tutsplus.com/tutorials/android-sdk-create-a-drawing-app-touch-interaction--mobile-19202

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadAnimation();
    }

    public void openTrace(View v){
        Intent tracePage = new Intent (this, Trace.class);
        this.startActivity(tracePage);
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

}

package com.example.nick.cursiveapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;


//https://code.tutsplus.com/tutorials/android-sdk-create-a-drawing-app-touch-interaction--mobile-19202

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView txtView = (TextView) findViewById(R.id.textview1);
        Animation fadeIn = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in);
        fadeIn.setStartOffset(1000);
        txtView.startAnimation(fadeIn);
    }


    public void openTrace(View v){
        Intent tracePage = new Intent (this, Trace.class);
        this.startActivity(tracePage);
    }
}

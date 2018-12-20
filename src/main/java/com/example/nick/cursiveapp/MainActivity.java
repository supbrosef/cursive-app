package com.example.nick.cursiveapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;


//https://code.tutsplus.com/tutorials/android-sdk-create-a-drawing-app-touch-interaction--mobile-19202

public class MainActivity extends AppCompatActivity {

    private int myRecord;
    LottieAnimationView animLottieLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView recordText = (TextView) findViewById(R.id.recordtext);
        Button aboutButton = findViewById(R.id.about);
        animLottieLogo = findViewById(R.id.logo);
        //animLottieLogo.setAnimation("logo.json");
        animLottieLogo.playAnimation();
        myRecord = getValue("record");
        if (myRecord > 0){
            String rec = String.format("%06d", myRecord);
            rec = rec.substring(0,2) + ":" + rec.substring(2,4) + ":" + rec.substring(4,6);
            recordText.setText("Current Record:\n" + rec);
        }

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonShowPopupWindowClick(view);
            }
        });
    }

    public void openTrace(View v){
        Intent tracePage = new Intent (this, TraceActivity.class);
        this.startActivity(tracePage);
    }

    public void openGame(View v){
        Intent gamePage = new Intent(this, GameActivity.class);
        this.startActivity(gamePage);
    }

    public void openEnd(View v){
        Intent gameEnd = new Intent(this, FinishActivity.class);
        this.startActivity(gameEnd);
    }
    public int getValue(String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getInt(value, 0);
    }

    public void onButtonShowPopupWindowClick(View view) {

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popupwindow, null);

        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 10, 10);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

}

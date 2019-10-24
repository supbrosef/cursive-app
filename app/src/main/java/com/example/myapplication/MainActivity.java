package com.example.myapplication;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.nick.cursiveapp.R;


public class MainActivity extends AppCompatActivity {

    Context mContext;
    private int myRecord;
    LottieAnimationView animLottieLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView recordText = (TextView) findViewById(R.id.recordtext);
        ImageButton aboutButton = findViewById(R.id.about);
        animLottieLogo = findViewById(R.id.logo);
        animLottieLogo.playAnimation();
        myRecord = getValue("record");
        if (myRecord > 0){
            String rec = String.format("%06d", myRecord);
            rec = rec.substring(0,2) + ":" + rec.substring(2,4) + ":" + rec.substring(4,6);
            recordText.setText("Current Record:\n" + rec);
        }

        Intent svc = new Intent(this, MusicService.class);
        startService(svc);

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonShowPopupWindowClick(view);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            int newRecord = data.getIntExtra("result",0);
            TextView recordText = (TextView) findViewById(R.id.recordtext);
            if (newRecord > 0){
                String rec = String.format("%06d", newRecord);
                rec = rec.substring(0,2) + ":" + rec.substring(2,4) + ":" + rec.substring(4,6);
                recordText.setText("Current Record:\n" + rec);
            }
        }
    }

    public void openTrace(View v){
        Intent tracePage = new Intent(this, TraceActivity.class);
        tracePage.putExtra("isRandom", false);
        this.startActivity(tracePage);
    }

    public void openGame(View v){
        Intent gamePage = new Intent(this, GameActivity.class);
        this.startActivityForResult(gamePage, myRecord);
    }

    public void openEnd(View v){
        Intent gameEnd = new Intent(this, FinishActivity.class);
        this.startActivity(gameEnd);
    }

    public void openRandomTrace(View v){
        Intent randomPage = new Intent(this, TraceActivity.class);
        randomPage.putExtra("isRandom", true);
        this.startActivity(randomPage);
    }

    public void openRandomUpper(View v){
        Intent randomUpper = new Intent(this, TraceActivity.class);
        randomUpper.putExtra("isUpper", true);
        this.startActivity(randomUpper);
    }

    public int getValue(String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getInt(value, 0);
    }

    public void toggleSound(View v){
        ImageButton sound = (ImageButton) findViewById(R.id.buttonSound);
        if (MusicService.isPlaying == true){
            stopService(new Intent(this, MusicService.class));
            sound.setImageResource(R.drawable.nosound);
        }
        else {
            startService(new Intent(this, MusicService.class));
            sound.setImageResource(R.drawable.sound);
        }
    }

    public void onButtonShowPopupWindowClick(View view) {

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popupwindow, null);

        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 10, 10);

        Button buttonRate = popupView.findViewById(R.id.buttonRate);
        buttonRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                }
                catch (ActivityNotFoundException e){
                    Toast.makeText(getApplicationContext(), "Error Opening Google Play Store...", Toast.LENGTH_LONG).show();
                }
            }
        });

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

//    @Override
//    protected void onStop(){
//        mContext.stopService(new Intent(mContext, MusicService.class));
//        super.onStop();
//    }
//
//    @Override
//    protected void onPause(){
//        mContext.stopService(new Intent(this, MusicService.class));
//        super.onPause();
//    }
//
//    @Override
//    protected void onResume(){
//        mContext.startService(new Intent(mContext, MusicService.class));
//        super.onResume();
//    }

}

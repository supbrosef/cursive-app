package com.nvarelas.nick.cursivemadeeasy;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
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
import com.airbnb.lottie.LottieAnimationView;
import com.flurry.android.FlurryAgent;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Context mContext;
    private int myRecord;
    LottieAnimationView animLottieLogo;
    public static boolean isPlaying = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView recordText = findViewById(R.id.recordtext);
        ImageButton aboutButton = findViewById(R.id.about);
        animLottieLogo = findViewById(R.id.logo);
        animLottieLogo.playAnimation();
        myRecord = getValue("record");
        if (myRecord > 0){
            String rec = String.format(Locale.US, "%06d", myRecord);
            rec = rec.substring(0,2) + ":" + rec.substring(2,4) + ":" + rec.substring(4,6);
            recordText.setText(getString(R.string.main_tv_setrecord, rec));
        }

        Intent svc = new Intent(this, MusicService.class);
        svc.putExtra("test", true);
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
            TextView recordText = findViewById(R.id.recordtext);
            if (newRecord > 0){
                String rec = String.format(Locale.US,"%06d", newRecord);
                rec = rec.substring(0,2) + ":" + rec.substring(2,4) + ":" + rec.substring(4,6);
                recordText.setText(getString(R.string.main_tv_setrecord, rec));
            }
        }
    }

    public void openTrace(View v){
        Intent tracePage = new Intent (this, TraceActivity.class);
        tracePage.putExtra("isRandom", false);
        this.startActivity(tracePage);
    }

    public void openGame(View v){
        Intent gamePage = new Intent(this, GameActivity.class);
        this.startActivityForResult(gamePage, myRecord);
    }


    public void openRandomTrace(View v){
        Intent randomPage = new Intent (this, TraceActivity.class);
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
        ImageButton sound = findViewById(R.id.buttonSound);
        if (MusicService.isPlaying){
            stopService(new Intent(this, MusicService.class));
            sound.setImageResource(R.drawable.nosound);
            isPlaying = false;
        }
        else {
            startService(new Intent(this, MusicService.class));
            sound.setImageResource(R.drawable.sound);
            isPlaying = true;
        }
    }

    @SuppressWarnings({"InflateParams", "ClickableViewAccessibility"})
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
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("amzn://apps/android?p=" + getPackageName())));
                }
                catch (ActivityNotFoundException e){
                    Toast.makeText(getApplicationContext(), "Error Opening Amazon App Store...", Toast.LENGTH_LONG).show();
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
//    public void onResume(){
//        super.onResume();
//        startService(new Intent(this, MusicService.class));
//    }

//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//        stopService(new Intent(this, MusicService.class));
//    }

    @Override
    public void onResume(){
        super.onResume();
        ImageButton sound = findViewById(R.id.buttonSound);
        if (MainActivity.isPlaying){
            sound.setImageResource(R.drawable.sound);
            startService(new Intent(this, MusicService.class));
        }
        else {
            sound.setImageResource(R.drawable.nosound);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        stopService(new Intent(this, MusicService.class));
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        stopService(new Intent(this, MusicService.class));
    }
}

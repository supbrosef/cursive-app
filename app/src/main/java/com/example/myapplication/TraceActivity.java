package com.example.myapplication;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.nick.cursiveapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class TraceActivity extends AppCompatActivity {

    private char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private String[] alpha = new String[52];
    public char currentLetter;
    public String currLett;
    public int myInt = 0, spot = 0;
    public String uri;
    String myColor;
    Context mContext;
    ImageButton buttonErase, buttonReplay;
    Spinner colorSpinner;
    RecyclerView recyclerView;
    public boolean isLarge, isRandom, isUpper;
    private TraceView traceView;
    private Map<String, String> colorMap = new HashMap<String, String>();
    private ArrayList<ColorItem> mColorList;
    public ArrayList<Character> charList = new ArrayList<>();
    public ArrayList<Integer> intList = new ArrayList<>();
    private ColorAdapter mAdapter;
    private LinearLayoutManager layoutManager = new MyCustomLayoutManager(this);
    Animation animScaleDown, animRotate;
    FragmentManager fm = getSupportFragmentManager();
    LottieAnimationView animLottieView, animLottieCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace);

        for(int i = 0; i < 26;i++){
            alpha[i] = "lower" + alphabet[i] + ".json";
        }
        for(int i = 26; i < 52;i++){
            alpha[i] = "upper" + alphabet[i-26] + ".json";
        }


        colorList();
        mContext = this;
        myInt = getValue("currPosition");
        myColor = getStringValue("currColor");
        isRandom = getIntent().getExtras().getBoolean("isRandom");
        isUpper = getIntent().getExtras().getBoolean("isUpper");

        if(isRandom){
            for(int i=0;i<26;i++){
                intList.add(i);
            }
            Collections.shuffle(intList);
            myInt = intList.get(spot);
            intList.remove(spot);
        }
        if(isUpper){
            for(int i=26;i<52;i++){
                intList.add(i);
            }
            Collections.shuffle(intList);
            myInt = intList.get(spot);
            intList.remove(spot);
        }

        Log.d("color", myColor);

        currLett = alpha[myInt];

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.ding);
        final MediaPlayer mpno = MediaPlayer.create(this, R.raw.buzzer);

        traceView = findViewById(R.id.drawing);
        buttonErase = findViewById(R.id.buttonErase);
        colorSpinner = findViewById(R.id.colorSpinner);
        mAdapter = new ColorAdapter(this, mColorList);
        recyclerView = findViewById(R.id.rvLetter);
        animLottieView = findViewById(R.id.animation_view);
        animLottieCheck = findViewById(R.id.exout);
        buttonReplay = findViewById(R.id.buttonReplay);
        ImageButton sound = (ImageButton) findViewById(R.id.buttonSound);

        if (MusicService.isPlaying == true){
            sound.setImageResource(R.drawable.sound);
        }
        else {
            sound.setImageResource(R.drawable.nosound);
        }

        if(getResources().getConfiguration().smallestScreenWidthDp >= 600 ){
            isLarge = true;
        }

        //Color Spinner
        colorSpinner.setAdapter(mAdapter);
        colorSpinner.setSelection(0,false);
        traceView.setColorValue(myColor);

        //RecyclerView
        charList = Letter.createArrayList();
        LetterAdapter letterAdapter = new LetterAdapter(charList);
        recyclerView.setAdapter(letterAdapter);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(myInt);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.smoothScrollToPosition(myInt+1);

        //Animations
        animScaleDown = new ScaleAnimation(1.0f, 0, 1.0f, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animScaleDown.setDuration(500);
        animScaleDown.setInterpolator(new AccelerateDecelerateInterpolator());
        animRotate = AnimationUtils.loadAnimation(TraceActivity.this, R.anim.rotate);
        animRotate.setFillAfter(true);
        final AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(animScaleDown);
        animationSet.addAnimation(animRotate);

        uri = currLett;

        animLottieView.setAnimation(uri);
        animLottieView.playAnimation();
        animLottieCheck.setEnabled(false);

        animLottieCheck.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (traceView.isCorrect == false) {
                    traceView.changeBit();
                    traceView.invalidate();
                    animLottieCheck.setEnabled(false);
                    animLottieCheck.setVisibility(View.INVISIBLE);
                    traceView.isTouchable = true;
                }
                else {
                    animLottieView.cancelAnimation();
                    animLottieView.setEnabled(false);
                    animLottieView.setVisibility(View.INVISIBLE);
                    animLottieCheck.setEnabled(false);
                    animLottieCheck.setVisibility(View.INVISIBLE);
                    if(myInt == 51){
                        Intent finAct = new Intent(TraceActivity.this, FinishActivity.class);
                        TraceActivity.this.startActivity(finAct);
                        myInt = -1;
                        finish();
                    }
                    if(isRandom || isUpper){
                        if (intList.isEmpty()){
                            Intent finAct = new Intent(TraceActivity.this, FinishActivity.class);
                            TraceActivity.this.startActivity(finAct);
                            finish();
                        }
                        myInt = intList.get(spot);
                        intList.remove(spot);
                    }
                    else {
                        myInt++;
                        setValue("currPosition", myInt);
                    }

                    currLett = alpha[myInt];

                    recyclerView.smoothScrollToPosition(myInt+1);
                    recyclerView.setLayoutManager(layoutManager);

                    //Animates view smaller and turns it to new view when done
                    traceView.startAnimation(animationSet);
                    traceView.isTouchable = false;
                }
            }
        });


        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                // true: consume touch event
                // false: dispatch touch event
                return true;
            }
        });

        buttonReplay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                animLottieView.setVisibility(View.VISIBLE);
                animLottieView.setAnimation(uri);
                animLottieView.playAnimation();
                traceView.isTouchable = false;
            }
        });

        buttonErase.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                animLottieCheck.setVisibility(View.INVISIBLE);
                animLottieCheck.setEnabled(false);
                animLottieCheck.setVisibility(View.INVISIBLE);
                animLottieCheck.setEnabled(false);
                traceView.isTouchable = true;
                traceView.changeBit();
                traceView.invalidate();
                traceView.actionDown = 0;
            }
        });

        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ColorItem clickedItem = (ColorItem) parent.getItemAtPosition(position);
                myColor = clickedItem.getColorName();
                traceView.setColorValue(myColor);
                setString("currColor", myColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        animLottieView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                buttonReplay.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animLottieView.setVisibility(View.INVISIBLE);
                traceView.isTouchable = true;
                buttonReplay.setEnabled(true);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        //View Smaller and rotate, calls new view and view fade in
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(final Animation animation) {
                traceView.resultBitmap.recycle();
                traceView.changeBit();
                traceView.postInvalidate();
                traceView.setAlpha(0f);
                traceView.animate().alpha(1).setDuration(1000).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        animLottieView.setVisibility(View.VISIBLE);
                        uri = "lower"+ currentLetter + ".json";
                        uri = currLett;
                        animLottieView.setAnimation(uri);
                        animLottieView.playAnimation();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
            }
            public void onAnimationRepeat(Animation animation) {
            }
            public void onAnimationStart(Animation animation) {
            }
        });

        animLottieCheck.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if(traceView.isCorrect == true) {
                    mp.start();
                }
                else
                    mpno.start();
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

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

    public String getStringValue(String value){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(value, "#00FF00");
    }
    public void setString(String value, String newValue){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(value, newValue);
        editor.apply();
    }

    public void openQuestion(View v){
        OnQuestionFragment fragment = new OnQuestionFragment();
        fragment.show(fm,"");
    }

    public void colorList(){
        mColorList = new ArrayList<>();
        mColorList.add(new ColorItem("#0000FF", R.drawable.color_blue));
        mColorList.add(new ColorItem("#ff6680", R.drawable.color_pink));
        mColorList.add(new ColorItem("#00FF00", R.drawable.color_green));
        mColorList.add(new ColorItem("#551A8B", R.drawable.color_purple));
        mColorList.add(new ColorItem("#ffa500", R.drawable.color_orange));
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
}

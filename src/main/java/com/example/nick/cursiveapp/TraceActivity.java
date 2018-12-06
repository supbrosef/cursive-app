package com.example.nick.cursiveapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.content.Intent;
import android.widget.Spinner;
import 	android.view.animation.AccelerateDecelerateInterpolator;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TraceActivity extends AppCompatActivity  {

    private char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    public char currentLetter;
    public int myInt = 0;
    public int currColor;
    public String uri;
    String myColor = null;
    Context mContext;
    Button nextButton;
    Spinner colorSpinner;
    RecyclerView recyclerView;
    public boolean isLarge;
    private TraceView traceView;
    private Map<String, String> colorMap = new HashMap<String, String>();
    private ArrayList<ColorItem> mColorList;
    public ArrayList<Character> charList = new ArrayList<>();
    private ColorAdapter mAdapter;
    private LinearLayoutManager layoutManager = new MyCustomLayoutManager(this);
    Animation animScaleDown, animRotate;
    LottieAnimationView animLottieView, animLottieCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //loadColorMap();
        colorList();
        mContext = this;
        myInt = getValue("currPosition");
        myColor = getStringValue("currColor");
        setContentView(R.layout.activity_trace);
        Log.d("color", myColor);
        currentLetter = alphabet[myInt];

        traceView = findViewById(R.id.drawing);
        nextButton = findViewById(R.id.next);
        Button resetButton = findViewById(R.id.reset);
        colorSpinner = findViewById(R.id.colorSpinner);
        mAdapter = new ColorAdapter(this, mColorList);
        recyclerView = findViewById(R.id.rvLetter);
        animLottieView = findViewById(R.id.animation_view);
        animLottieCheck = findViewById(R.id.check);

        if(getResources().getConfiguration().smallestScreenWidthDp >= 600 ){
            isLarge = true;
        }

        //Color Spinner
        colorSpinner.setAdapter(mAdapter);
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

        uri = "lower"+ currentLetter + ".json";
        animLottieView.setAnimation(uri);
        animLottieView.playAnimation();

        animLottieCheck.setAnimation("check_animation.json");
        animLottieCheck.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view){
                animLottieCheck.setVisibility(View.INVISIBLE);
                if(myInt == 6){
                    Intent finAct = new Intent(TraceActivity.this, FinishActivity.class);
                    TraceActivity.this.startActivity(finAct);
                    myInt = -1;
                    finish();
                }
                myInt++;
                currentLetter = alphabet[myInt];

                recyclerView.smoothScrollToPosition(myInt+1);
                recyclerView.setLayoutManager(layoutManager);

                setValue("currPosition", myInt);
                //Animates view smaller and turns it to new view when done
                traceView.startAnimation(animationSet);
                traceView.isTouchable = false;
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

        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                nextButton.setEnabled(false);
                if(myInt == 6){
                    Intent finAct = new Intent(TraceActivity.this, FinishActivity.class);
                    TraceActivity.this.startActivity(finAct);
                    myInt = -1;
                    finish();
                }
                myInt++;
                currentLetter = alphabet[myInt];

                recyclerView.smoothScrollToPosition(myInt+1);
                recyclerView.setLayoutManager(layoutManager);

                setValue("currPosition", myInt);
                //Animates view smaller and turns it to new view when done
                traceView.startAnimation(animationSet);
                traceView.isTouchable = false;
            }
        });

        resetButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v){
                myInt = 0;
                currentLetter = alphabet[myInt];
                setValue("currPosition", myInt);
                traceView.resultBitmap.recycle();
                traceView.changeBit();
                traceView.postInvalidate();
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

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animLottieView.setVisibility(View.INVISIBLE);
                traceView.isTouchable = true;
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
                //nextButton.setEnabled(true);
            }
            public void onAnimationRepeat(Animation animation) {
            }
            public void onAnimationStart(Animation animation) {
            }
        });
        nextButton.setEnabled(false);
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

    public void colorList(){
        mColorList = new ArrayList<>();
        mColorList.add(new ColorItem("#FFC0CB",R.drawable.pink));
        mColorList.add(new ColorItem("#FFFF00",R.drawable.yellow));
        mColorList.add(new ColorItem("#00FF00",R.drawable.green));
        mColorList.add(new ColorItem("#0000FF", R.drawable.blue));
        mColorList.add(new ColorItem("#551A8B",R.drawable.purple));
    }
}

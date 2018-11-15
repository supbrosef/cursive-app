package com.example.nick.cursiveapp;

import android.animation.AnimatorSet;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Spinner;
import 	android.view.animation.AccelerateDecelerateInterpolator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TraceActivity extends AppCompatActivity  {

    private char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    public char currentLetter;
    public int myInt = 0;
    String myColor = null;
    Context mContext;
    Button nextButton;
    Spinner colorSpinner;
    RecyclerView recyclerView;
    ImageView gifView;
    public boolean isLarge;
    private TraceView drawView;
    private Map<String, String> colorMap = new HashMap<String, String>();
    private ArrayList<ColorItem> mColorList;
    public ArrayList<Character> charList = new ArrayList<>();
    private ColorAdapter mAdapter;
    private LinearLayoutManager layoutManager = new MyCustomLayoutManager(this);
    Animation scaleDown, rotateAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadColorMap();
        colorList();
        mContext = this;
        myInt = getValue("int");
        myColor = getString("color");
        setContentView(R.layout.activity_trace);
        if (savedInstanceState != null){
            myInt = savedInstanceState.getInt("MyInt");
            myColor = savedInstanceState.getString("myColor");
        }
        currentLetter = alphabet[myInt];
        if(getResources().getConfiguration().smallestScreenWidthDp >= 600 ){
            isLarge = true;
        }

        drawView = findViewById(R.id.drawing);
        nextButton = findViewById(R.id.next);
        Button resetButton = findViewById(R.id.reset);
        colorSpinner = findViewById(R.id.colorSpinner);
        mAdapter = new ColorAdapter(this, mColorList);
        recyclerView = findViewById(R.id.rvLetter);
        gifView = findViewById(R.id.gifView);

        Glide.with(this).asGif().load(R.drawable.lettera).listener(new RequestListener<GifDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(final GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                if (resource instanceof GifDrawable){
                    ((GifDrawable)resource).setLoopCount(1);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(true) {
                                if(!resource.isRunning()) {
                                    gifView.setVisibility(View.INVISIBLE);
                                    break;
                                }
                            }
                        }
                    }).start();
                    return false;
                }
                return false;
            }
        }).into(gifView);

        //Color Spinner
        colorSpinner.setAdapter(mAdapter);
        drawView.setColor(colorMap.get(myColor));

        //RecyclerView
        charList = Letter.createArrayList();
        LetterAdapter letterAdapter = new LetterAdapter(charList);
        recyclerView.setAdapter(letterAdapter);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(myInt);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.smoothScrollToPosition(myInt+1);

        //Animations
        scaleDown = new ScaleAnimation(1.0f, 0, 1.0f, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleDown.setDuration(500);
        scaleDown.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnim = AnimationUtils.loadAnimation(TraceActivity.this, R.anim.rotate);
        rotateAnim.setFillAfter(true);
        final AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(scaleDown);
        animationSet.addAnimation(rotateAnim);

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
                //Disabled for testing purposes
                //nextButton.setEnabled(false);
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

                setValue("int", myInt);
                drawView.startAnimation(animationSet);
            }
        });

        resetButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v){
                myInt = 0;
                currentLetter = alphabet[myInt];
                setValue("int", myInt);
                drawView.resultBitmap.recycle();
                drawView.changeBit();
                drawView.postInvalidate();
            }
        });

        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ColorItem clickedItem = (ColorItem) parent.getItemAtPosition(position);
                drawView.setColor(colorMap.get(clickedItem.getColorName()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                drawView.resultBitmap.recycle();
                drawView.changeBit();
                drawView.postInvalidate();
                //Fade in new letter
                drawView.setAlpha(0f);
                drawView.animate().alpha(1).setDuration(1000);
                //nextButton.setEnabled(true);
            }
            public void onAnimationRepeat(Animation animation) {
            }
            public void onAnimationStart(Animation animation) {
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

    public String getString(String value){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(value, "blue");
    }
    public void setString(String value, String newValue){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(value, newValue);
        editor.apply();
    }

    public void loadColorMap(){
        colorMap.put("green","#00FF00");
        colorMap.put("pink","#FFC0CB");
        colorMap.put("blue","#0000FF");
        colorMap.put("cyan", "#00FFFF");
        colorMap.put("yellow","#FFFF00");
        colorMap.put("purple","#551A8B");
    }

    public void colorList(){
        mColorList = new ArrayList<>();
        mColorList.add(new ColorItem("blue", R.drawable.blue));
        mColorList.add(new ColorItem("green",R.drawable.green));
        mColorList.add(new ColorItem("yellow",R.drawable.yellow));
        mColorList.add(new ColorItem("pink",R.drawable.pink));
        mColorList.add(new ColorItem("purple",R.drawable.purple));
    }


}

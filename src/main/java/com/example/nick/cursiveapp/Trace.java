package com.example.nick.cursiveapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.LinearLayout;


public class Trace extends AppCompatActivity{

    private char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    public char currentLetter;
    public int myInt = 0;
    public String color;
    Context mContext;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        myInt = getValue();
        setContentView(R.layout.activity_trace);
        if (savedInstanceState != null){
            myInt = savedInstanceState.getInt("MyInt");
        }
        currentLetter = alphabet[myInt];

        nextButton = (Button) findViewById(R.id.next);
        //nextButton.setVisibility(View.INVISIBLE);
        nextButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v){
                if(myInt == 4){
                    Intent finAct = new Intent(Trace.this, FinishActivity.class);
                    Trace.this.startActivity(finAct);
                    myInt = -1;
                    finish();
                }
                myInt++;
                setValue(myInt);
                recreate();
            }
        });

        Button resetButton = (Button) findViewById(R.id.reset);
        resetButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v){
                myInt = 0;
                setValue(myInt);
                recreate();
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("MyInt",myInt);
    }

    public int getValue() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getInt("value_key", 0);
    }

    public void setValue(int newValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("value_key", newValue);
        editor.commit();
    }

    public void setVisible(){
        nextButton.setVisibility(View.VISIBLE);
    }

}

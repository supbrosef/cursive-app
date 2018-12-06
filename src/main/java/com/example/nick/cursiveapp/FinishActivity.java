package com.example.nick.cursiveapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

public class FinishActivity extends AppCompatActivity {

    public Button menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        menu = (Button) findViewById(R.id.buttonMenu);

        menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                Intent mainMenu  = new Intent(FinishActivity.this, MainActivity.class);
                FinishActivity.this.startActivity(mainMenu);
            }
        });
    }



}

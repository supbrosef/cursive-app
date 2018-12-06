package com.example.nick.cursiveapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.fragment.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class OnCompleteFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_gameend, container, false);
        GameActivity gameActivity = (GameActivity) getActivity();
        Button ok = (Button) rootView.findViewById(R.id.button);
        TextView result = rootView.findViewById(R.id.textView5);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if(gameActivity.isBetter == true){
            result.setText("Congrats!. New Record: \n" + gameActivity.tValue);
            gameActivity.setValue("record", gameActivity.value);
        }
        else{
            result.setText("Your time was: " + gameActivity.tValue + ".\n Try again to beat your previous record!");
        }
        ok.setOnClickListener(OnClickListener);
        return rootView;
    }

    public View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent mainAct = new Intent(getActivity(), MainActivity.class);
            startActivity(mainAct);
        }
    };
}

package com.example.nick.cursiveapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class OnStartFragment extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gamestart, container, false);
        Button dismiss = (Button) rootView.findViewById(R.id.button);
        //getDialog().setTitle(getResources().getString(R.string.app_name));
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dismiss.setOnClickListener(new View.OnClickListener() {
        GameActivity gameActivity = (GameActivity) getActivity();
            @Override
            public void onClick(View v) {
                gameActivity.startGame();
                dismiss();
            }
        });
        return rootView;
    }
}
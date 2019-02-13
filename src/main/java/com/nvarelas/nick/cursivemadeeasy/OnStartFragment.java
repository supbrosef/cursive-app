package com.nvarelas.nick.cursivemadeeasy;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

public class OnStartFragment extends DialogFragment {
    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gamestart, container, false);
        Button dismiss = rootView.findViewById(R.id.button);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
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

    @SuppressWarnings("ConstantConditions")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {
                getActivity().finish();
             }
        };
    }
}
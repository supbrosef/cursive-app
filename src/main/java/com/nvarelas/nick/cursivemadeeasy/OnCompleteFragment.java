package com.nvarelas.nick.cursivemadeeasy;

import android.content.Intent;
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
import android.widget.TextView;
import static android.app.Activity.RESULT_OK;

public class OnCompleteFragment extends DialogFragment {

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_gameend, container, false);
        GameActivity gameActivity = (GameActivity) getActivity();
        Button ok = rootView.findViewById(R.id.button);
        TextView result = rootView.findViewById(R.id.textView5);
        if(getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        if(gameActivity != null && gameActivity.isBetter){
            Intent prevScreen = new Intent();
            prevScreen.putExtra("result", gameActivity.value);
            getActivity().setResult(RESULT_OK, prevScreen);
            result.setText(getString(R.string.complete_tv_newrecord, gameActivity.tValue));
            gameActivity.setValue("record", gameActivity.value);
        }
        else{
            result.setText(getString(R.string.complete_tv_samerecord, gameActivity.tValue));
        }
        ok.setOnClickListener(OnClickListener);
        return rootView;
    }

    @SuppressWarnings("ConstantConditions")
    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getActivity().finish();
        }
    };
}

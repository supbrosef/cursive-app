package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nick.cursiveapp.R;

import java.util.ArrayList;

public class ColorAdapter extends ArrayAdapter<ColorItem> {
    public ColorAdapter(Context context, ArrayList<ColorItem> colorList){
        super(context, 0 , colorList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = initView(position, convertView, parent);
        view.setVisibility(View.GONE);
        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.color_spinner, parent, false
            );
        }

        ImageView imageViewFlag = convertView.findViewById(R.id.image_view_color);
        ColorItem currentItem = getItem(position);

        if (currentItem != null) {
            imageViewFlag.setImageResource(currentItem.getColorImage());
        }

        return convertView;
    }

}

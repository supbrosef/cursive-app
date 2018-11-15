package com.example.nick.cursiveapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Trace;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

public class LetterAdapter extends RecyclerView.Adapter<LetterAdapter.MyViewHolder>  {

    public List<Character> mLetters;


    public LetterAdapter(List<Character> letters){
        mLetters = letters;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View letterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.letter_recycler, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(letterView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        char letter = mLetters.get(position);
        holder.letterTextView.setText(Character.toString(letter));
    }

    @Override
    public int getItemCount(){
        return mLetters.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView letterTextView;

        public MyViewHolder(View itemView){
            super(itemView);
            letterTextView = itemView.findViewById(R.id.current_letter);
        }

    }


}

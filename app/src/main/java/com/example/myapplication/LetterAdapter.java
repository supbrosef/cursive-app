package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nick.cursiveapp.R;

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

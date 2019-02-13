package com.nvarelas.nick.cursivemadeeasy;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

public class LetterAdapter extends RecyclerView.Adapter<LetterAdapter.MyViewHolder>  {

    private List<Character> mLetters;


    LetterAdapter(List<Character> letters){
        mLetters = letters;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View letterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.letter_recycler, parent, false);
        return new MyViewHolder(letterView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        char letter = mLetters.get(position);
        holder.letterTextView.setText(Character.toString(letter));
    }

    @Override
    public int getItemCount(){
        return mLetters.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView letterTextView;

        MyViewHolder(View itemView){
            super(itemView);
            letterTextView = itemView.findViewById(R.id.current_letter);
        }

    }


}

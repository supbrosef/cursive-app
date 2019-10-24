package com.example.myapplication;

import java.util.ArrayList;

public class Letter {
    private char mLetter;

    public char getLetter(){
        return mLetter;
    }

    public static ArrayList<Character> createArrayList(){
        ArrayList<Character> letters = new ArrayList<>();
        char[] charArray = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ".toCharArray();
        for (int i = 0; i<charArray.length;i++){
            letters.add(charArray[i]);
        }
        return letters;
    }
}

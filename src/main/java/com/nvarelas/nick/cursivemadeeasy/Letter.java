package com.nvarelas.nick.cursivemadeeasy;

import java.util.ArrayList;

class Letter {

    static ArrayList<Character> createArrayList(){
        ArrayList<Character> letters = new ArrayList<>();
        char[] charArray = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ".toCharArray();
        for(Character c : charArray){
            letters.add(c);
        }
        return letters;
    }
}

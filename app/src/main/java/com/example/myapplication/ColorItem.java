package com.example.myapplication;

public class ColorItem {
    private String mColorName;
    private int mColorImage;

    public ColorItem(String colorName, int colorImage){
        mColorName = colorName;
        mColorImage = colorImage;
    }

    public String getColorName(){
        return mColorName;
    }

    public int getColorImage(){
        return mColorImage;
    }
}

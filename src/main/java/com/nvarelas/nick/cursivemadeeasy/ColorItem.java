package com.nvarelas.nick.cursivemadeeasy;

class ColorItem {
    private String mColorName;
    private int mColorImage;

    ColorItem(String colorName, int colorImage){
        mColorName = colorName;
        mColorImage = colorImage;
    }

    String getColorName(){
        return mColorName;
    }

    int getColorImage(){
        return mColorImage;
    }
}

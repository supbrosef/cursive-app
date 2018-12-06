package com.example.nick.cursiveapp;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;


public class MyCustomLayoutManager extends LinearLayoutManager {

    private static final float MILLISECONDS_PER_INCH = 800f;
    Context mContext;

    public MyCustomLayoutManager(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        RecyclerView.SmoothScroller smoothScroller = new CenterSmoothScroller(recyclerView.getContext());
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    private static class CenterSmoothScroller extends LinearSmoothScroller {

        CenterSmoothScroller(Context context) {
            super(context);
        }

        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
            return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2);
        }

        @Override
        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            return MILLISECONDS_PER_INCH/displayMetrics.densityDpi;
        }
    }
}

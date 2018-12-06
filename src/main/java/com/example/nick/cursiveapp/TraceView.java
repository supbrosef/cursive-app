package com.example.nick.cursiveapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.util.AttributeSet;
import android.graphics.Color;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class TraceView extends View {

    private Path drawPath;
    private Paint drawPaint;
    private Canvas drawCanvas;
    public Bitmap canvasBitmap, backgroundBitmap, resultBitmap;
    private int mWidth;
    private int mHeight;
    private int oldPix;
    private int oldX, oldY, actualBlackPixels, initialBlackPixels;
    private int strokeWidth;
    public boolean isTouchable = false;
    private Rect rect;
    public Map<Path, Paint> pathMap = new HashMap<>();
    public ArrayList<Integer> pixelsArrayList = new ArrayList<Integer>();
    TraceActivity traceActivity = (TraceActivity)getContext();
    private int paintColor = Color.BLUE;
    public int pixelAmount;
    public TraceView(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    float wPixel;


    private void setupDrawing(int color){
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(color);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(strokeWidth);
        if (traceActivity.isLarge == true){
            drawPaint.setStrokeWidth(strokeWidth);
        }
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!rect.contains(this.getLeft() + (int) event.getX(), this.getTop() + (int) event.getY())){
            return false;
        }
        if(!isTouchable){
            return false;
        }
        int touchX = (int) event.getX();
        int touchY = (int) event.getY();
        int currentPixel = resultBitmap.getPixel(touchX,touchY);
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                if (currentPixel == Color.TRANSPARENT) {
                    setupDrawing(Color.RED);
                }
                else {
                    setupDrawing(paintColor);
                }
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                if (currentPixel == Color.TRANSPARENT){
                    if(oldPix != Color.TRANSPARENT) {
                        drawPath.moveTo(oldX, oldY);
                        drawPath.lineTo(touchX, touchY);
                        setupDrawing(Color.RED);
                        drawPath.moveTo(touchX, touchY);
                    }
                    drawPaint.setColor(Color.RED);
                }
                else {
                    if (oldPix == Color.TRANSPARENT) {
                        drawPath.moveTo(oldX, oldY);
                        drawPath.lineTo(touchX, touchY);
                        setupDrawing(paintColor);
                        drawPath.moveTo(touchX, touchY);

                    }
                    drawPaint.setColor(paintColor);
                }
                drawPath.lineTo(touchX, touchY);
                break;

            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                int[] pixels = new int[resultBitmap.getHeight() * resultBitmap.getWidth()];
                resultBitmap.getPixels(pixels, 0, resultBitmap.getWidth(), 0, 0, resultBitmap.getWidth(), resultBitmap.getHeight());
                for(int pixel:pixels) {
                    if(pixel == Color.BLACK){
                        actualBlackPixels++;
                    }
                }
                if(actualBlackPixels < (initialBlackPixels/2)){
                    traceActivity.nextButton.setEnabled(true);
                    traceActivity.animLottieCheck.setVisibility(View.VISIBLE);
                    traceActivity.animLottieCheck.playAnimation();
                }
                Log.d("actual pixels", Integer.toString(actualBlackPixels));
                actualBlackPixels = 0;
                break;

            default:
                return false;
        }
        pathMap.put(drawPath, drawPaint);
        oldPix = currentPixel;
        oldX = touchX;
        oldY = touchY;
        invalidate(); //calls onDraw
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        mWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        mHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rect = new Rect(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
        changeBit();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(resultBitmap,0,0,drawPaint);
        for (Path p : pathMap.keySet()) {
            canvas.drawPath(p, pathMap.get(p)); //draws visual path as drawing
        }
    }

    //change letter bitmap
    public void changeBit(){
        wPixel = (float)mWidth * .1f;
        char currLetter = traceActivity.currentLetter;
        String uri = "lower" + currLetter;
        int currImage = getResources().getIdentifier(uri, "drawable", traceActivity.getPackageName());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        canvasBitmap = BitmapFactory.decodeResource(getResources(), currImage, options);
        canvasBitmap = resizeBitmap(canvasBitmap, mWidth, mHeight); //letter bitmap

        backgroundBitmap = Bitmap.createBitmap(500,500, Bitmap.Config.ARGB_8888);
        backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, mWidth, mHeight, false);

        resultBitmap = mergeToPin(backgroundBitmap, canvasBitmap, mWidth, mHeight);

        Log.d("pixel", Integer.toString(pixelAmount));
        drawCanvas = new Canvas(resultBitmap);
        int[] pixels = new int[resultBitmap.getHeight() * resultBitmap.getWidth()];
        resultBitmap.getPixels(pixels, 0, resultBitmap.getWidth(), 0, 0, resultBitmap.getWidth(), resultBitmap.getHeight());
        strokeWidth = canvasBitmap.getWidth() / 18;
        initialBlackPixels = 0;
        actualBlackPixels = 0;
        for(int pixel:pixels) {
            if(pixel == Color.BLACK){
                initialBlackPixels++;
            }
        }
        Log.d("initial pixels", Integer.toString(initialBlackPixels));
        pathMap.clear();
    }

    //resize letter bitmap
    private Bitmap resizeBitmap(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap );
            }
            if(traceActivity.isLarge == true) {
                image = Bitmap.createScaledBitmap(image, (int) ((float) finalWidth * .8f), (int) ((float) finalHeight * .8f), true);
            }
            else{
                image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            }
            return image;
        } else {
            return image;
        }
    }

    //merge background image and letter to one bitmap
    public static Bitmap mergeToPin(Bitmap back, Bitmap front,int width, int height) {
        Bitmap result = Bitmap.createBitmap(back.getWidth(), back.getHeight(), back.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(back, 0f, 0f, null);
        canvas.drawBitmap(front, ((width-front.getWidth())/2), ((height-front.getHeight())/2), null);
        return result;
    }

    //change color of correct paths
    public void setColorValue(String color){
        paintColor = Color.parseColor(color);
        for(Paint p : pathMap.values()){
            if(p.getColor() != Color.RED){
                p.setColor(paintColor);
            }
        }
        invalidate();
    }
}

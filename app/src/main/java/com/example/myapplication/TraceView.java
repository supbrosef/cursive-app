package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TraceView extends View {

    public Path drawPath;
    private Paint drawPaint;
    private Canvas drawCanvas;
    public Bitmap canvasBitmap, backgroundBitmap, resultBitmap;
    private int mWidth;
    private int mHeight;
    private int oldPix;
    private int oldX, oldY, actualBlackPixels, actualRedPixels, initialBlackPixels;
    private int strokeWidth;
    public int actionDown = 0;
    private Set<Integer> specialLetters = new HashSet<Integer>(Arrays.asList(8,9,19,23,31,33,36,45,49));
    public boolean isTouchable = false, isCheck = false, isCorrect = false;
    private Rect rect;
    public Map<Path, Paint> pathMap = new HashMap<>();
    public ArrayList<Integer> pixelsArrayList = new ArrayList<Integer>();
    TraceActivity traceActivity = (TraceActivity)getContext();
    private int paintColor = Color.BLUE;
    public int pixelAmount;
    float wPixel;

    public TraceView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

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
                actionDown++;
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
                for (Path p : pathMap.keySet()) {
                    drawCanvas.drawPath(p, pathMap.get(p)); //draws visual path as drawing
                }
                int[] pixels = new int[resultBitmap.getHeight() * resultBitmap.getWidth()];
                resultBitmap.getPixels(pixels, 0, resultBitmap.getWidth(), 0, 0, resultBitmap.getWidth(), resultBitmap.getHeight());
                for(int pixel:pixels) {
                    if(pixel != Color.TRANSPARENT) {
                        if (pixel == Color.BLACK) {
                            actualBlackPixels++;
                        }
                        if (pixel == Color.RED) {
                            actualRedPixels++;
                        }
                    }
                }
                if(specialLetters.contains(traceActivity.myInt)){
                    if (actualRedPixels > initialBlackPixels / 5 || pathMap.size() > 5) {
                        setIncorrect();
                    }
                    else if (actualBlackPixels < (initialBlackPixels / 2) && actionDown > 1) {
                        setCorrect();
                    }
                }
                else {
                    if (actualRedPixels > initialBlackPixels / 5 || pathMap.size() > 5 || actionDown > 1) {
                        setIncorrect();
                    }
                    else if (actualBlackPixels < (initialBlackPixels / 2)) {
                        setCorrect();
                    }
                }
                Log.d("Black pixels", Integer.toString(actualBlackPixels));
                Log.d("Blue pixels", Integer.toString(actualRedPixels));
                actualBlackPixels = 0;
                actualRedPixels=0;
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

    public void setIncorrect(){
            isCorrect = false;
            traceActivity.animLottieCheck.setAnimation("exout.json");
            traceActivity.animLottieCheck.setVisibility(View.VISIBLE);
            traceActivity.animLottieCheck.setEnabled(true);
            traceActivity.animLottieCheck.playAnimation();
            isTouchable = false;
        }

    public void setCorrect(){
         isCorrect = true;
         traceActivity.animLottieCheck.setAnimation("check_animation.json");
         traceActivity.animLottieCheck.setVisibility(View.VISIBLE);
         traceActivity.animLottieCheck.setEnabled(true);
         traceActivity.animLottieCheck.playAnimation();
         isTouchable = false;
    }

    //change letter bitmap
    public void changeBit(){
        wPixel = (float)mWidth * .1f;
        String uri = traceActivity.currLett;
        uri = uri.substring(0,6);
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
        strokeWidth = canvasBitmap.getWidth() / 14;
        initialBlackPixels = 0;
        actualBlackPixels = 0;
        actionDown = 0;
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
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    //merge background image and letter to one bitmap
    public static Bitmap mergeToPin(Bitmap back, Bitmap front, int width, int height) {
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

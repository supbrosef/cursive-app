package com.example.nick.cursiveapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.util.AttributeSet;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import android.widget.ImageButton;
import android.graphics.PathMeasure;
import android.widget.LinearLayout;

public class TraceView extends View {

    private Path drawPath, wrongPath, selectPath;
    private Paint drawPaint, canvasPaint;
    private int paintColor = Color.BLUE;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap, resized;
    private ImageButton currPaint;
    private TraceView traceview;
    private int mWidth;
    private int mHeight;
    private int oldPix;
    private Rect rect;
    private float pathLength = 0;
    private Map<Path, Paint> pathMap = new HashMap<Path, Paint>();
    private ArrayList<Path> paths = new ArrayList<Path>();

    public TraceView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing(paintColor);
    }

    private void setupDrawing(int color){
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(color);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(40);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        //canvasPaint = new Paint(Paint.DITHER_FLAG); //??
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        rect = new Rect(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
        if(!rect.contains(this.getLeft() + (int) event.getX(), this.getTop() + (int) event.getY())){
            return false;
        }
        int touchX = (int) event.getX();
        int touchY = (int) event.getY();
        int pixel = resized.getPixel(touchX,touchY);
        if (pixel == Color.TRANSPARENT){
            if(oldPix != Color.TRANSPARENT) {
                setupDrawing(Color.RED);
                drawPath.moveTo(touchX, touchY);
                drawPath.lineTo(touchX, touchY);
            }
            drawPaint.setColor(Color.RED);
        }
        else {
            if (oldPix == Color.TRANSPARENT) {
                setupDrawing(Color.BLUE);
                drawPath.moveTo(touchX, touchY);
                drawPath.lineTo(touchX, touchY);
            }
            drawPaint.setColor(Color.BLUE);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                break;
            default:
                return false;
        }
        pathMap.put(drawPath, drawPaint);
        oldPix = pixel;
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
        Trace trace = (Trace)getContext();
        char currLetter = trace.currentLetter;
        String uri = "lower" + currLetter;
        int currImage = getResources().getIdentifier(uri, "drawable", trace.getPackageName());
        super.onSizeChanged(w, h, oldw, oldh);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        canvasBitmap = BitmapFactory.decodeResource(getResources(), currImage, options);

        resized = Bitmap.createScaledBitmap(canvasBitmap, w, h, false);
        drawCanvas = new Canvas(resized);

        Paint paint = new Paint();
        Typeface typeBold = Typeface.createFromAsset(getResources().getAssets(), "fonts/learning_curve_bold_ot_tt.ttf");
        paint.setColor(Color.BLACK);
        paint.setTextSize(1000);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(typeBold);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(resized, 0, 0, canvasPaint);
        for (Path p : pathMap.keySet()) {
            canvas.drawPath(p, pathMap.get(p)); //draws visual path as drawing
        }
    }

}

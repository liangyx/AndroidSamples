package com.liangyx.undoredo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Deque;
import java.util.LinkedList;

public class FingerPaintView extends View {

    private Bitmap mBitmap;
    //private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;

    private Paint mPaint;

    private float mX, mY;

    private static final float TOUCH_TOLERANCE = 4;

    private Deque<Path> allPath = new LinkedList<Path>();
    private Deque<Path> removedPath = new LinkedList<Path>();

    public FingerPaintView(Context context) {
        super(context);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);

        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        //mCanvas = new Canvas(mBitmap);
        Log.e("test", "size: " + allPath.size());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xFFAAAAAA);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        for (Path path : allPath) {
            canvas.drawPath(path, mPaint);
        }
        if (mPath != null) {
            canvas.drawPath(mPath, mPaint);
        }
    }

    private void touch_start(float x, float y) {
        mPath = new Path();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        allPath.offerFirst(mPath);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    public void undo() {
        mPath = null;
        Path path = allPath.pollFirst();
        if (path != null) {
            removedPath.offerFirst(path);
        }
        invalidate();
    }

    public void redo() {
        Path path = removedPath.pollFirst();
        if (path != null) {
            allPath.offerFirst(path);
        }
        invalidate();
    }

    public void clear() {
        allPath.clear();
        removedPath.clear();
        mPath = null;
        invalidate();
    }
}

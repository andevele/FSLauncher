package com.fengsong.launcher.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class InputSourceRecyclerView extends RecyclerView {
    private Matrix matrix;

    public InputSourceRecyclerView(Context context) {
        super(context);
        init();
    }

    public InputSourceRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InputSourceRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        matrix = new Matrix();
        //make the shadow reverse of Y
        matrix.preScale(1, -1);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        Bitmap origin = loadBitmapFromView(this);
//        int height = origin.getHeight();
//        int width = origin.getWidth();
//        Log.d("zhulf", "===width: " + width + " height: " + height);
//        Bitmap reflectionImage = Bitmap.createBitmap(origin, 0,
//                height / 2, width, height / 2, matrix, false);
//        Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
//                (height + height / 2), Bitmap.Config.ARGB_8888);
////        Canvas canvas1 = new Canvas(bitmapWithReflection);
//        canvas.drawBitmap(reflectionImage, 0, 8 * height / 12, null);
    }

    public static Bitmap loadBitmapFromView(View v) {
        if (v == null) {
            return null;
        }
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(screenshot);
        canvas.translate(-v.getScrollX(), -v.getScrollY());//我们在用滑动View获得它的Bitmap时候，获得的是整个View的区域（包括隐藏的），如果想得到当前区域，需要重新定位到当前可显示的区域
        v.draw(canvas);// 将 view 画到画布上
        return screenshot;
    }
}

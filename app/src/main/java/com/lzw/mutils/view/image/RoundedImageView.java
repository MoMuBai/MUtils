package com.lzw.mutils.view.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.lzw.mutils.tool.ScreenUtils;

/**
 * Author: lzw
 * Date: 2019/7/11
 * Description: This is RoundedImageView
 */
public class RoundedImageView<T> extends android.support.v7.widget.AppCompatImageView {

    private int roundWidth;


    private int roundHeight;

    private Paint paint;

    private Paint paint2;

    public RoundedImageView(Context context) {
        this(context, null);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        roundWidth = ScreenUtils.dp2px(4);
        roundHeight = ScreenUtils.dp2px(4);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        paint2 = new Paint();
        paint2.setXfermode(null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bitmap);
        super.onDraw(canvas);
        drawLiftUp(canvas2);
        drawRightUp(canvas2);
        drawRightBottom(canvas2);
        drawLeftBottom(canvas2);
        canvas.drawBitmap(bitmap, 0, 0, paint2);
        bitmap.recycle();
    }


    private void drawLiftUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, roundHeight);
        path.lineTo(0, 0);
        path.lineTo(roundWidth, 0);
        path.arcTo(new RectF(0, 0, roundWidth * 2, roundHeight * 2), -90, -90);
        path.close();
        canvas.drawPath(path, paint);
    }


    private void drawLeftBottom(Canvas canvas) {
        Path path = new Path();
        path.moveTo(roundWidth, getHeight());
        path.lineTo(0, getHeight());
        path.lineTo(0, getHeight() - roundHeight);
        path.arcTo(new RectF(0, getHeight() - roundHeight * 2, roundWidth * 2, getHeight()), -180, -90);
        path.close();
        canvas.drawPath(path, paint);
    }


    private void drawRightUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getWidth(), roundHeight);
        path.lineTo(getWidth(), 0);
        path.lineTo(getWidth() - roundWidth, 0);
        path.arcTo(new RectF(getWidth() - roundWidth * 2, 0, getWidth(), roundHeight * 2), -90, 90);
        path.close();
        canvas.drawPath(path, paint);
    }


    /**
     * @param canvas
     */
    private void drawRightBottom(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getWidth() - roundWidth, getHeight());
        path.lineTo(getWidth(), getHeight());
        path.lineTo(getWidth(), getHeight() - roundHeight);
        path.arcTo(new RectF(getWidth() - roundWidth * 2, getHeight() - roundHeight * 2, getWidth(), getHeight()), 0, 90);
        path.close();
        canvas.drawPath(path, paint);
    }
}

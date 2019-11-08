package com.lzw.mutils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lzw.mutils.tool.ScreenUtils;

/**
 * Author: lzw
 * Date: 2019-11-07
 * Description: 圆形进度条可跟手指位置变化
 */
public class HandProgressView extends View {

    /**
     * 进度条Paint
     */
    private Paint mProgressPaint;

    /**
     * 进度条背景Paint
     */
    private Paint mBackPaint;

    /**
     * 圆形Paint
     */
    private Paint mCirclePaint;

    /**
     * 文字Paint
     */
    private Paint mTextPaint;

    /**
     * 圆心点坐标
     */
    private int mCenterX, mCenterY;

    /**
     * 圆弧矩形
     */
    private RectF mRectF;

    /**
     * 当前进度
     */
    private float mCurrentprogress = 45;

    /**
     * 上一次进度
     */
    private float mLastProgress;

    /**
     * 上一次点所在象限
     */
    private int mLastQuadrant = 1;


    /**
     * 是否按在圆弧
     */
    private boolean downOnProgress;

    /**
     * 半径
     */
    private int mRadius;

    public HandProgressView(Context context) {
        this(context, null);
    }

    public HandProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HandProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mTextPaint = new Paint();
        mTextPaint.setStrokeWidth(0);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setTextSize(40f);
        mTextPaint.setStrokeCap(Paint.Cap.ROUND);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setDither(true);

        mBackPaint = new Paint();
        mBackPaint.setStrokeWidth(25f);
        mBackPaint.setStyle(Paint.Style.STROKE);
        mBackPaint.setStrokeCap(Paint.Cap.ROUND);
        mBackPaint.setAntiAlias(true);
        mBackPaint.setColor(Color.GRAY);
        mBackPaint.setDither(true);

        mProgressPaint = new Paint();
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStrokeWidth(25f);
        mProgressPaint.setColor(Color.BLUE);
        mProgressPaint.setDither(true);

        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setDither(true);
        mCirclePaint.setStrokeWidth(10f);
        mCirclePaint.setColor(Color.BLUE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        mRadius = (int) ((width > height ? height : width) - mBackPaint.getStrokeWidth());
        int left = (width - mRadius) / 2;
        int top = (height - mRadius) / 2;
        mRectF = new RectF(left, top, left + mRadius, top + mRadius);
        mCenterX = width / 2;
        mCenterY = height / 2;
    }

    private int measure(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {//match或者具体高度
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {//wrap
            result = ScreenUtils.dp2px(200);
//            result = Math.min(result, specSize);
        } else {//其他
            result = ScreenUtils.dp2px(200);
        }
        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String text = (int) ((mCurrentprogress / 360) * 100) + "%";
        float textWidth = mTextPaint.measureText(text);
        canvas.drawText(text, mCenterX - textWidth / 2, mCenterY, mTextPaint);
        canvas.drawArc(mRectF, 0, 360, false, mBackPaint);
        canvas.drawArc(mRectF, -90, mCurrentprogress, false, mProgressPaint);
        canvas.save();
        canvas.rotate(mCurrentprogress, mCenterX, mCenterY);
        canvas.drawCircle(mCenterX, mBackPaint.getStrokeWidth() / 2, mBackPaint.getStrokeWidth() / 2 + 5, mCirclePaint);
        canvas.restore();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isTouchProgress(x, y)) {
                    downOnProgress = true;
                    updateProgress(x, y);
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (downOnProgress) {
                    updateProgress(x, y);
                }
                break;
            case MotionEvent.ACTION_UP:
                downOnProgress = false;
                break;
        }
        invalidate();
        return true;
    }

    /**
     * 更改圆弧进度
     *
     * @param x
     * @param y
     */
    private void updateProgress(int x, int y) {
        int a = x - mCenterX;
        int b = y - mCenterY;

        // 计算角度，（-180°->180°）
        double angle = Math.atan2(b, a) / Math.PI;
        // 将角度转换成（0-360）之间的值，然后加上90°的偏移量
        angle = ((2 + angle) % 2 + (90 / 180f)) % 2;
        float progress = (float) (angle * 180);

        //第一象限
        if (a >= 0 && b <= 0) {
            if (mLastProgress >= 270f) {//转了一圈后，不能继续转了
                progress = 360f;
                mLastQuadrant = 2;
            } else {
                mLastQuadrant = 1;
            }
            mCurrentprogress = progress;
        }
        //第二象限
        if (a <= 0 && b <= 0) {
            if (mLastProgress < 90f) {//从第一象限往后转，不能转了
                progress = 0f;
                mLastQuadrant = 1;
            } else {
                mLastQuadrant = 2;
            }
            mCurrentprogress = progress;
        }
        //第三象限
        if (a <= 0 && b >= 0 && mLastQuadrant != 1 && mLastProgress < 359.f) {
            mLastQuadrant = 3;
            mCurrentprogress = progress;
        }
        //第四象限
        if (a >= 0 && b >= 0 && mLastQuadrant != 2 && mLastProgress > 0.f) {
            mLastQuadrant = 4;
            mCurrentprogress = progress;
        }

        mLastProgress = progress;

//        int finalAngle = 0;
//        double angle = Math.toDegrees(Math.atan2(b, a));//得到角度
//        if (angle < 0) {
//            angle = Math.abs(angle);
//            if (angle < 90) {
//                angle = 90 - Math.abs(angle);
//                finalAngle = (int) angle;
//            } else {
//                finalAngle = (int) ((float) (180 + (180 - angle)) + 90);
//            }
//        } else {
//            finalAngle = (int) (angle + 90);
//        }
//        mCurrentprogress = finalAngle;

    }

    /**
     * 是否按在圆弧上
     *
     * @param x
     * @param y
     * @return
     */
    private boolean isTouchProgress(int x, int y) {
        int a = x - mCenterX;
        int b = y - mCenterY;
        double c = Math.hypot(a, b);//算出手指触摸点到圆心点的距离
        if (c >= mRadius / 2 - 50f && c <= mRadius / 2 + 50f) {
            return true;
        } else {
            return false;
        }
    }

}

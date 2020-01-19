package com.lzw.mutils.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Author: lzw
 * Date: 2019-11-14
 * Description: This is SplashView
 */
public class SplashView extends View {

    /**
     * 属性动画
     */
    private ValueAnimator mAnimator;

    /**
     * 大圆的半径
     */
    private float mRotationRadius = 90;

    /**
     * 每一个小圆的半径
     */
    private float mCircleRadius = 18;

    /**
     * 小圆的颜色列表
     */
    private int[] mCircleColors;


    /**
     * 背景颜色
     */
    private int mSplashBgColor = Color.parseColor("#FFFFFF");

    /**
     * 圆的画笔
     */
    private Paint mCirclePaint = new Paint();
    /**
     * 背景的画笔
     */
    private Paint mBackgroundPaint = new Paint();

    /**
     * 中心点坐标
     */
    private float mCenterX, mCenterY;

    /**
     * 对角线一半
     */
    private float mDiagonalDist;


    /**
     * 宽、高
     */
    private int mWidth, mHeight;


    /**
     * 策略模式
     */
    private SplashState mState = null;


    /**
     * 空心圆初始半径，空心圆用来实现扩散效果
     */
    private float mHoleRadius = 0F;

    /**
     * 当前大圆旋转角度(弧度)
     */
    private float mCurrentRotationAngle = 0F;


    /**
     * 当前大圆的半径
     */
    private float mCurrentRotationRadius = mRotationRadius;


    /**
     * 旋转的时间
     */
    private long mRotationDuration = 1500;


    public SplashView(Context context) {
        this(context, null);
    }

    public SplashView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SplashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        float density = context.getResources().getDisplayMetrics().density;
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Log.d("SplashView", "density:" + density);
        Log.d("SplashView", "widthPixels:" + widthPixels);
        Log.d("SplashView", "heightPixels:" + heightPixels);
    }

    /**
     * 初始化
     */
    private void init() {
        mCircleColors = new int[]{Color.BLUE, Color.RED, Color.GREEN, Color.GRAY, Color.YELLOW, Color.BLACK};
        mCirclePaint.setAntiAlias(true);
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.STROKE);
        mBackgroundPaint.setColor(mSplashBgColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;
        mDiagonalDist = (float) Math.sqrt((w * w + h * h)) / 2f;//对角线的一半
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mState == null) {
            mState = new RotateState();
        }
        mState.drawState(canvas);
    }


    /**
     * 旋转
     */
    private class RotateState extends SplashState {

        public RotateState() {
            mAnimator = ValueAnimator.ofFloat(0, (float) (Math.PI * 2));//从0到360度变化
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotationAngle = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mAnimator.setDuration(mRotationDuration);
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mAnimator.start();
        }

        @Override
        public void drawState(Canvas canvas) {

            drawBackground(canvas);

            drawCircles(canvas);
        }
    }

    /**
     * 画小圆
     *
     * @param canvas
     */
    private void drawCircles(Canvas canvas) {
        float rotationAngle = (float) (2 * Math.PI / mCircleColors.length);
        for (int i = 0; i < mCircleColors.length; i++) {
            /**
             * x = r*cos(a) +centerX
             * y=  r*sin(a) + centerY
             *
             * 每个小圆*间隔角度 + 旋转的角度 = 当前小圆的角度
             */
            double angle = i * rotationAngle + mCurrentRotationAngle;
            float cx = (float) (mCurrentRotationRadius * Math.cos(angle) + mCenterX);
            float cy = (float) (mCurrentRotationRadius * Math.sin(angle) + mCenterY);
            mCirclePaint.setColor(mCircleColors[i]);
            canvas.drawCircle(cx, cy, mCircleRadius, mCirclePaint);
        }
    }

    /**
     * 画白色背景
     *
     * @param canvas
     */
    private void drawBackground(Canvas canvas) {
        if (mHoleRadius > 0) {//如果空心圆的半径大于0，代表要开始扩散效果
            //得到画笔的宽度 = 对角线/2 - 空心圆的半径
            float strokeWidth = mDiagonalDist - mHoleRadius;
            mBackgroundPaint.setStrokeWidth(strokeWidth);
            //画圆的半径 = 空心圆的半径 + 画笔的宽度/2
            float radius = mHoleRadius + strokeWidth / 2;
            canvas.drawCircle(mCenterX, mCenterY, radius, mBackgroundPaint);
        } else {//小于0，只需画个白色背景
            canvas.drawColor(mSplashBgColor);
        }
    }


    /**
     * 聚合
     */
    private class MergingState extends SplashState {

        public MergingState() {
            mAnimator = ValueAnimator.ofFloat(0, mRotationRadius);
            mAnimator.setInterpolator(new OvershootInterpolator(10f));//弹性差值器
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotationRadius = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mState = new ExpandState();
                }
            });
            //反转下
            mAnimator.reverse();
        }

        @Override
        public void drawState(Canvas canvas) {
            drawBackground(canvas);
            drawCircles(canvas);
        }
    }

    /**
     * 开始聚合
     */
    public void startMerging() {
        if (mState != null && mState instanceof RotateState) {
            //结束旋转动画
            SplashView.RotateState rotateState = (SplashView.RotateState) mState;
            rotateState.cancel();
            post(new Runnable() {
                @Override
                public void run() {
                    mState = new SplashView.MergingState();
                }
            });
        }
    }


    /**
     * 扩散
     */
    private class ExpandState extends SplashState {

        public ExpandState() {
            mAnimator = ValueAnimator.ofFloat(mCircleRadius, mDiagonalDist);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    /**
                     * 空心圆的半径
                     */
                    mHoleRadius = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mAnimator.setDuration(mRotationDuration);
            mAnimator.start();
        }

        @Override
        public void drawState(Canvas canvas) {
            drawBackground(canvas);
        }
    }

    private abstract class SplashState {
        public abstract void drawState(Canvas canvas);

        public void cancel() {
            mAnimator.cancel();
        }
    }


}

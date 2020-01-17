package com.lzw.mutils.view.banner;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lzw.mutils.R;
import com.lzw.mutils.tool.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author :lzw
 * date   :2020-01-15-16:15
 * desc   :
 */
public class LBanner extends FrameLayout {

    private boolean mAutoPlay = true;//是否自动播放，默认为true
    private int mDelayTime = 3000;//自动播放的间隔时间，默认3000毫秒
    private boolean mLoop = true;//是否可以无限制滑动，默认为true
    private boolean mShowIndicator = true;//是否显示指示器
    private LBannerPageChangeListener mLBannerPageChangeListener;
    private LBannerViewPager mViewPager;
    private LinearLayout mIndicatorGroup;
    private List<ImageView> mIndicatorView;
    private LBannerHeadFootAdapter mLBannerHeadFootAdapter;
    private LBannerMaxAdapter mLBannerMaxAdapter;
    private Context mContext;
    private int mCurrentPos, mRealCurrentPos;
    private List mData;
    private LayoutInflater mLayoutInflater;
    private LBannerImageLoader mLBannerImageLoader;
    private LBannerStyle mBannerStyle = LBannerStyle.ViewPagerStyle;//默认是ViewPager的加头加尾显示
    private int mLayout;
    private Drawable mSelectIndicator, mUnSelectIndicator;
    private int mIndicatorSize = ScreenUtils.dp2px(8);

    private int mIndicatorLeftMargin = 0;
    private int mIndicatorRightMargin = ScreenUtils.dp2px(8);
    private int mIndicatorTopMargin = 0;
    private int mIndicatorBottomMargin = ScreenUtils.dp2px(8);

    private int mIndicatorGroupLeftMargin = 0;
    private int mIndicatorGroupRightMargin = 0;
    private int mIndicatorGroupTopMargin = 0;
    private int mIndicatorGroupBottomMargin = 0;

    private int mIndicatorGravity = Gravity.CENTER_HORIZONTAL;//默认是居中显示
    private LBannerListener mLBannerListener;


    public LBanner(Context context) {
        this(context, null);
    }

    public LBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public LBanner setLBannerListener(LBannerListener lBannerListener) {
        this.mLBannerListener = lBannerListener;
        return this;
    }


    public LBanner setLBannerPageChangeListener(LBannerPageChangeListener lBannerPageChangeListener) {
        this.mLBannerPageChangeListener = lBannerPageChangeListener;
        return this;
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        mData = new ArrayList();
        mIndicatorView = new ArrayList();
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayout = R.layout.layout_viewpager_banner;
    }


    /**
     * 设置指示器
     *
     * @param selectDrawable
     * @param unSelectDrawable
     */
    private void setDrawableIndicator(Drawable selectDrawable, Drawable unSelectDrawable) {
        mSelectIndicator = selectDrawable;
        mUnSelectIndicator = unSelectDrawable;
    }


    /**
     * 处理数据
     *
     * @param data
     */
    private void buildRecyclerData(List data) {
        mData.clear();
        mData.addAll(data);
    }

    /**
     * 处理数据
     *
     * @param data
     */
    private void buildMaxViewPagerData(List data) {
        mData.clear();
        mData.addAll(data);
    }

    /**
     * 处理数据
     *
     * @param data
     */
    private void buildViewPagerData(List data) {
        mData.clear();
        if (mLoop) {
            mData.add(0, data.get(data.size() - 1));
            for (int i = 0; i < data.size(); i++) {
                mData.add(i + 1, data.get(i));
            }
            mData.add(data.size() + 1, data.get(0));
        } else {
            mData.addAll(data);
        }
    }

    /**
     * 处理ViewPager相关
     */
    private void buildViewPager() {
        View inflate = mLayoutInflater.inflate(mLayout, this);
        mViewPager = inflate.findViewById(R.id.view_pager);
        mIndicatorGroup = inflate.findViewById(R.id.layout_indicator);
        mLBannerHeadFootAdapter = new LBannerHeadFootAdapter(mContext, mData, mLBannerImageLoader, mLBannerListener, mLoop);
        mViewPager.setAdapter(mLBannerHeadFootAdapter);
        int realSize = 0;
        if (mLoop) {
            mCurrentPos = 1;
            realSize = mData.size() - 2;
        } else {
            mCurrentPos = 0;
            realSize = mData.size();
        }
        mViewPager.setCurrentItem(mCurrentPos);
        if (mShowIndicator) {
            buildIndicator(realSize);
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPos = position;
                if (mLoop) {
                    if (mCurrentPos == 0) {
                        mCurrentPos = mData.size() - 2;
                    } else if (mCurrentPos == mData.size() - 1) {
                        mCurrentPos = 1;
                    }
                    if (mCurrentPos >= 1) {
                        mRealCurrentPos = mCurrentPos - 1;
                        for (int i = 0; i < mIndicatorView.size(); i++) {
                            if (mCurrentPos - 1 == i) {
                                mIndicatorView.get(i).setImageDrawable(mSelectIndicator);
                            } else {
                                mIndicatorView.get(i).setImageDrawable(mUnSelectIndicator);
                            }
                        }
                        mLBannerPageChangeListener.onPageChange(mRealCurrentPos);
                    }
                } else {
                    mRealCurrentPos = mCurrentPos;
                    for (int i = 0; i < mIndicatorView.size(); i++) {
                        if (mCurrentPos == i) {
                            mIndicatorView.get(i).setImageDrawable(mSelectIndicator);
                        } else {
                            mIndicatorView.get(i).setImageDrawable(mUnSelectIndicator);
                        }
                    }
                    mLBannerPageChangeListener.onPageChange(mRealCurrentPos);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mLoop) {
                    //验证当前的滑动是否结束
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        mViewPager.setCurrentItem(mCurrentPos, false);//切换，不要动画效果
                    }
                }
            }
        });
    }


    /**
     * 处理MaxViewPager相关
     */
    private void buildMaxViewPager() {
        View inflate = mLayoutInflater.inflate(mLayout, this);
        mViewPager = inflate.findViewById(R.id.view_pager);
        mIndicatorGroup = inflate.findViewById(R.id.layout_indicator);
        if (mShowIndicator) {
            buildIndicator(mData.size());
        }
        mLBannerMaxAdapter = new LBannerMaxAdapter(mContext, mData, mLBannerImageLoader, mLBannerListener, mLoop);
        mViewPager.setAdapter(mLBannerMaxAdapter);
        if (mLoop) {
            mViewPager.setCurrentItem(mData.size() * 10000);//取一个较大值
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mLoop) {
                    mCurrentPos = position % mData.size();
                } else {
                    mCurrentPos = position;
                }
                mRealCurrentPos = mCurrentPos;
                mLBannerMaxAdapter.setOnClickPos(mRealCurrentPos);
                for (int i = 0; i < mIndicatorView.size(); i++) {
                    if (mRealCurrentPos == i) {
                        mIndicatorView.get(i).setImageDrawable(mSelectIndicator);
                    } else {
                        mIndicatorView.get(i).setImageDrawable(mUnSelectIndicator);
                    }
                }
                mLBannerPageChangeListener.onPageChange(mRealCurrentPos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 生成指示器
     *
     * @param size
     */
    private void buildIndicator(int size) {
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mIndicatorSize, mIndicatorSize);
            imageView.setLayoutParams(layoutParams);
            layoutParams.bottomMargin = mIndicatorBottomMargin;
            layoutParams.topMargin = mIndicatorTopMargin;
            layoutParams.rightMargin = mIndicatorRightMargin;
            layoutParams.leftMargin = mIndicatorLeftMargin;
            if (i == 0) {
                imageView.setImageDrawable(mSelectIndicator);
            } else {
                imageView.setImageDrawable(mUnSelectIndicator);
            }
            mIndicatorGroup.setGravity(mIndicatorGravity);
            mIndicatorView.add(imageView);
            mIndicatorGroup.addView(imageView);
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mIndicatorGroup.getLayoutParams();
        layoutParams.leftMargin = mIndicatorGroupLeftMargin;
        layoutParams.rightMargin = mIndicatorGroupRightMargin;
        layoutParams.topMargin = mIndicatorGroupTopMargin;
        layoutParams.bottomMargin = mIndicatorGroupBottomMargin;
    }

    /**
     * 处理RecyclerView相关
     */
    private void buildRecyclerView() {

    }


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });
    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (mLoop && mAutoPlay) {
                mViewPager.setCurrentItem(mRealCurrentPos);
                handler.postDelayed(task, mDelayTime);
            }
        }
    };


    //******************************对面暴露的方法************************************

    /**
     * 设置是否自动播放
     *
     * @param autoPlay
     * @return
     */
    public LBanner setAutoPlay(boolean autoPlay) {
        this.mAutoPlay = autoPlay;
        return this;
    }

    /**
     * 设置自动轮播的间隔时间
     *
     * @param delayTime
     * @return
     */
    public LBanner setDelayTime(int delayTime) {
        this.mDelayTime = delayTime;
        return this;
    }

    /**
     * 设置自动播放
     */
    public void startAutoPlay() {
        handler.removeCallbacks(task);
        handler.postDelayed(task, mDelayTime);
    }

    /**
     * 设置停止播放
     */
    public void stopAutoPlay() {
        handler.removeCallbacks(task);
    }

    /**
     * 设置是否可以无限滑动
     *
     * @param mLoop
     * @return
     */
    public LBanner setLoop(boolean mLoop) {
        this.mLoop = mLoop;
        return this;
    }


    /**
     * 设置是否显示指示器
     *
     * @param showIndicator
     * @return
     */
    public LBanner setShowIndicator(boolean showIndicator) {
        this.mShowIndicator = showIndicator;
        return this;
    }

    /**
     * 设置指示器的Size
     *
     * @param size
     * @return
     */
    public LBanner setIndicatorSize(int size) {
        this.mIndicatorSize = ScreenUtils.dp2px(size);
        return this;
    }


    /**
     * 设置指示器之间的的margin
     *
     * @param leftMargin
     * @param topMargin
     * @param rightMargin
     * @param bottomMargin
     * @return
     */
    public LBanner setIndicatorMargin(int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        this.mIndicatorLeftMargin = ScreenUtils.dp2px(leftMargin);
        this.mIndicatorRightMargin = ScreenUtils.dp2px(rightMargin);
        this.mIndicatorTopMargin = ScreenUtils.dp2px(topMargin);
        this.mIndicatorBottomMargin = ScreenUtils.dp2px(bottomMargin);
        return this;
    }


    /**
     * 设置指示器组的的margin
     *
     * @param leftMargin
     * @param topMargin
     * @param rightMargin
     * @param bottomMargin
     * @return
     */
    public LBanner setIndicatorGroupMargin(int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        this.mIndicatorGroupLeftMargin = ScreenUtils.dp2px(leftMargin);
        this.mIndicatorGroupRightMargin = ScreenUtils.dp2px(rightMargin);
        this.mIndicatorGroupTopMargin = ScreenUtils.dp2px(topMargin);
        this.mIndicatorGroupBottomMargin = ScreenUtils.dp2px(bottomMargin);
        return this;
    }

    /**
     * 设置是RecyclerView还是ViewPager来支持的
     *
     * @param style
     * @return
     */
    public LBanner setStyle(LBannerStyle style) {
        this.mBannerStyle = style;
        return this;
    }

    /**
     * 设置指示器
     *
     * @param selectIndicator
     * @param unSelectIndicator
     * @return
     */
    public LBanner setIndicator(int selectIndicator, int unSelectIndicator) {
        Resources resources = mContext.getResources();
        Drawable selectDrawable = resources.getDrawable(selectIndicator);
        Drawable unSelectDrawable = resources.getDrawable(unSelectIndicator);
        setDrawableIndicator(selectDrawable, unSelectDrawable);
        return this;
    }

    /**
     * 设置指示器的位置，默认居中显示
     *
     * @param gravity
     * @return
     */
    public LBanner setIndicatorGravity(int gravity) {
        if (gravity == Gravity.CENTER || gravity == Gravity.CENTER_VERTICAL) {
            gravity = Gravity.CENTER_HORIZONTAL;
        }
        mIndicatorGravity = gravity;
        return this;
    }

    /**
     * 设置指示器
     *
     * @param selectIndicator
     * @param unSelectIndicator
     * @return
     */
    public LBanner setIndicator(Drawable selectIndicator, Drawable unSelectIndicator) {
        setDrawableIndicator(selectIndicator, unSelectIndicator);
        return this;
    }


    /**
     * 设置自己的图片加载方式，如果不设置默认采用ViewPager的最大值方式
     *
     * @param loader
     * @return
     */
    public LBanner setImageLoader(LBannerImageLoader loader) {
        this.mLBannerImageLoader = loader;
        return this;
    }

    /**
     * 设置数据源
     *
     * @param data
     * @return
     */
    public LBanner setImgData(List data) {
        if (null != data && data.size() > 0) {
            if (mBannerStyle == LBannerStyle.RecyclerViewStyle) {//RecyclerView实现方式
                buildRecyclerData(data);
            } else if (mBannerStyle == LBannerStyle.ViewPagerMaxStyle) {//ViewPager最大值实现方式
                buildMaxViewPagerData(data);
            } else {//默认是ViewPager加头加尾实现方式
                buildViewPagerData(data);
            }
        }
        return this;
    }

    /**
     * 开始构建
     *
     * @return
     */
    public LBanner build() {
        if (mBannerStyle == LBannerStyle.RecyclerViewStyle) {//RecyclerView实现方式
            buildRecyclerView();
        } else if (mBannerStyle == LBannerStyle.ViewPagerMaxStyle) {//ViewPager最大值的实现方式
            buildMaxViewPager();
        } else {//这边默认如果不写的话会去使用ViewPager加头加尾的实现方式
            buildViewPager();
        }
        return this;
    }
}

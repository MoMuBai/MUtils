package com.lzw.mutils.view.banner;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.lzw.mutils.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author :lzw
 * date   :2020-01-15-16:15
 * desc   :
 */
public class LBanner extends FrameLayout {

    private ViewPager mViewPager;
    private LBannerHeadFootAdapter mLBannerHeadFootAdapter;
    private LBannerMaxAdapter mLBannerMaxAdapter;
    private Context mContext;
    private int mCurrentPos;
    private List mData;
    private LayoutInflater mLayoutInflater;
    private LBannerImageLoader mLBannerImageLoader;
    private LBannerStyle mBannerStyle = LBannerStyle.ViewPagerHeadFootStyle;
    private int mLayout;

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

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        mData = new ArrayList();
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayout = R.layout.layout_viewpager_banner;
    }


    /**
     * 设置布局
     *
     * @param layout
     * @return
     */
    public LBanner setLayout(int layout) {
        if (layout != 0) {
            mLayout = layout;
        }
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
     * 设置自己的图片加载样式
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
    public LBanner setImgData(List<?> data) {
        if (null != data && data.size() > 0) {
            if (mBannerStyle == LBannerStyle.ViewPagerHeadFootStyle) {
                mData.clear();
                mData.add(0, data.get(data.size() - 1));
                for (int i = 0; i < data.size(); i++) {
                    mData.add(i + 1, data.get(i));
                }
                mData.add(data.size() + 1, data.get(0));
            } else if (mBannerStyle == LBannerStyle.ViewPagerMaxStyle) {
                mData.clear();
                mData.addAll(data);
            } else if (mBannerStyle == LBannerStyle.RecyclerViewStyle) {

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
        if (mBannerStyle == LBannerStyle.ViewPagerHeadFootStyle) {
            View inflate = mLayoutInflater.inflate(mLayout, this);
            mViewPager = inflate.findViewById(R.id.view_pager);
            mLBannerHeadFootAdapter = new LBannerHeadFootAdapter(mContext, mLBannerImageLoader, mData);
            mViewPager.setAdapter(mLBannerHeadFootAdapter);
            mCurrentPos = 1;
            mViewPager.setCurrentItem(mCurrentPos);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mCurrentPos = position;
                    if (mCurrentPos == 0) {
                        mCurrentPos = mData.size() - 2;
                    } else if (mCurrentPos == mData.size() - 1) {
                        mCurrentPos = 1;
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    //验证当前的滑动是否结束
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        mViewPager.setCurrentItem(mCurrentPos, false);//切换，不要动画效果
                    }
                }
            });
        } else if (mBannerStyle == LBannerStyle.ViewPagerMaxStyle) {
            View inflate = mLayoutInflater.inflate(mLayout, this);
            mViewPager = inflate.findViewById(R.id.view_pager);
            mLBannerMaxAdapter = new LBannerMaxAdapter(mContext, mLBannerImageLoader, mData);
            mViewPager.setAdapter(mLBannerMaxAdapter);
            mViewPager.setCurrentItem(mData.size() * 1000);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mCurrentPos = position % mData.size();
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else if (mBannerStyle == LBannerStyle.RecyclerViewStyle) {

        }
        return this;
    }
}

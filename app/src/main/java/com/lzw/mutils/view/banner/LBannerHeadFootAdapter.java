package com.lzw.mutils.view.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lzw.mutils.tool.ScreenUtils;

import java.util.List;

/**
 * author :lzw
 * date   :2020-01-15-16:22
 * desc   :
 */
public final class LBannerHeadFootAdapter extends PagerAdapter {

    private List mData;

    private Context mContext;

    private LBannerImageLoader mLBannerImageLoader;

    private View imageView;

    private LBannerListener mLBannerListener;

    private int mCurrentPos;

    private boolean mLoop;


    public LBannerHeadFootAdapter(Context mContext, List mData, LBannerImageLoader lBannerImageLoader, LBannerListener lBannerListener, boolean mLoop) {
        this.mContext = mContext;
        this.mLBannerListener = lBannerListener;
        this.mLBannerImageLoader = lBannerImageLoader;
        this.mData = mData;
        this.mLoop = mLoop;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        imageView = mLBannerImageLoader.createLoadView();
        if (null == imageView) {
            imageView = new ImageView(mContext);
        }
        mLBannerImageLoader.showLoadView(imageView, mData.get(position));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentPos = position;
                if (mLoop) {
                    if (mCurrentPos == 0) {
                        mCurrentPos = mData.size() - 2;
                    } else if (mCurrentPos == mData.size() - 1) {
                        mCurrentPos = 1;
                    }
                    if (mCurrentPos >= 1) {
                        mLBannerListener.itemClick(mCurrentPos - 1);
                    }
                } else {
                    mLBannerListener.itemClick(mCurrentPos);
                }
            }
        });
        container.addView(imageView);
        return imageView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}

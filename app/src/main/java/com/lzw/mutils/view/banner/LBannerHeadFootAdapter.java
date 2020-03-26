package com.lzw.mutils.view.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lzw.mutils.tool.ScreenUtils;

import java.util.ArrayList;
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

    public void setData(List mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    public LBannerHeadFootAdapter(Context mContext, LBannerImageLoader lBannerImageLoader, LBannerListener lBannerListener, boolean mLoop) {
        this.mContext = mContext;
        this.mLBannerListener = lBannerListener;
        this.mLBannerImageLoader = lBannerImageLoader;
        this.mLoop = mLoop;
        this.mData = new ArrayList();
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
    public Object instantiateItem(ViewGroup container, int position) {
        imageView = mLBannerImageLoader.createLoadView();
        if (null == imageView) {
            imageView = new ImageView(mContext);
        }
        if (mData.size() > 0) {
            mLBannerImageLoader.showLoadView(imageView, mData.get(position));
        }
        int finalPosition = position;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentPos = finalPosition;
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

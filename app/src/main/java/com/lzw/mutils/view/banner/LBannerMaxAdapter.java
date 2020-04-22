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
 * date   :2020-01-16-10:39
 * desc   :
 */
public final class LBannerMaxAdapter extends PagerAdapter {

    private List mData;

    private Context mContext;

    private int currentPosition;

    private LBannerImageLoader mLBannerImageLoader;

    private LBannerListener mLBannerListener;

    private View imageView;

    private List<View> viewList;

    private int clickPos = 0;

    private boolean mLoop;

    public void setData(List mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    public LBannerMaxAdapter(Context mContext, List mData, LBannerImageLoader lBannerImageLoader, LBannerListener lBannerListener, boolean mLoop) {
        this.mContext = mContext;
        this.mLBannerImageLoader = lBannerImageLoader;
        this.mLBannerListener = lBannerListener;
        this.mLoop = mLoop;
        this.mData = new ArrayList();
        this.mData = mData;
        this.viewList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mLoop ? Integer.MAX_VALUE : mData.size();
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
        if (mLoop) {
            currentPosition = position % mData.size();
        } else {
            currentPosition = position;
        }
        mLBannerImageLoader.showLoadView(imageView, mData.get(currentPosition));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLBannerListener.itemClick(clickPos);
            }
        });
        viewList.add(imageView);
        container.addView(imageView);
        return imageView;
    }

    /**
     * 设置点击的pos
     *
     * @param pos
     */
    public void setOnClickPos(int pos) {
        clickPos = pos;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        if (mLoop) {
            currentPosition = position % mData.size();
        } else {
            currentPosition = position;
        }
        container.removeView(viewList.get(currentPosition));
    }
}

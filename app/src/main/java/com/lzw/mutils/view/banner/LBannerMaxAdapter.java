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


    public LBannerMaxAdapter(Context mContext, List mData, LBannerImageLoader lBannerImageLoader, LBannerListener lBannerListener) {
        this.mContext = mContext;
        this.mLBannerImageLoader = lBannerImageLoader;
        this.mLBannerListener = lBannerListener;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE / 2;
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
        currentPosition = position % mData.size();
        mLBannerImageLoader.showLoadView(imageView, mData.get(currentPosition));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLBannerListener.itemClick(currentPosition);
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

package com.lzw.mutils.view.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
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
public class LBannerHeadFootAdapter extends PagerAdapter {

    private List mData;

    private Context mContext;

    private LBannerImageLoader mLBannerImageLoader;

    public LBannerHeadFootAdapter(Context mContext, LBannerImageLoader lBannerImageLoader, List mData) {
        this.mContext = mContext;
        this.mLBannerImageLoader = lBannerImageLoader;
        this.mData = mData;
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
        ImageView imageView = new ImageView(mContext);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mLBannerImageLoader.loadImg(imageView, mData.get(position));
        container.addView(imageView);
        return imageView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}

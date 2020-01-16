package com.lzw.mutils.view.banner;

import android.widget.ImageView;

/**
 * author :lzw
 * date   :2020-01-15-16:51
 * desc   :
 */
public interface LBannerImageLoader<T> {
    void loadImg(ImageView imageView, T path);
}

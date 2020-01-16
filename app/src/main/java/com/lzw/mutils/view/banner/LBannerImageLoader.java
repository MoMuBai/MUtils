package com.lzw.mutils.view.banner;

import android.view.View;
import android.widget.ImageView;

/**
 * author :lzw
 * date   :2020-01-15-16:51
 * desc   :
 */
public interface LBannerImageLoader<T, V extends View> {

    void showLoadView(V imageView, T path);

    V createLoadView();
}

package com.lzw.mutils.view.image;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;

import com.lzw.mutils.R;

/**
 * Author: lzw
 * Date: 2019-11-14
 * Description: This is ContentImageView
 */
public class ContentImageView extends AppCompatImageView {

    public ContentImageView(Context context) {
        super(context);
        setScaleType(ScaleType.CENTER_CROP);
        setImageResource(R.drawable.ic_sp);
    }
}

package com.lzw.mutils.ui.banner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzw.mutils.R;
import com.lzw.mutils.tool.To;
import com.lzw.mutils.view.banner.LBanner;
import com.lzw.mutils.view.banner.LBannerImageLoader;
import com.lzw.mutils.view.banner.LBannerImageView;
import com.lzw.mutils.view.banner.LBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * author :lzw
 * date   :2020-01-16-13:48
 * desc   :
 */
public class LBannerActivity extends AppCompatActivity {

    private LBanner lBanner;

    private List<String> data;

    private List<Integer> idList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        lBanner = findViewById(R.id.banner);

        data = new ArrayList<>();
        idList = new ArrayList<>();
        data.add("http://c.hiphotos.baidu.com/zhidao/pic/item/d009b3de9c82d1587e249850820a19d8bd3e42a9.jpg");
        data.add("http://i1.sinaimg.cn/ent/d/2008-06-04/U105P28T3D2048907F326DT20080604225106.jpg");
        data.add("http://dl.ppt123.net/pptbj/51/20181115/mzj0ghw2xo2.jpg");
        idList.add(R.drawable.zhizhen1);
        idList.add(R.drawable.ic_launcher_background);
        idList.add(R.drawable.aa);
        lBanner.setImageLoader(new MyLoader())
                .setIndicator(R.drawable.ic_select_indicator, R.drawable.ic_unselect_indicator)
                .setIndicatorGravity(Gravity.CENTER)
                .setImgData(data)
                .setLBannerListener(new LBannerListener() {
                    @Override
                    public void itemClick(int pos) {
                        Toast.makeText(LBannerActivity.this, "点击了：" + pos, Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

    }


    private class MyLoader implements LBannerImageLoader<String, ImageView> {

        @Override
        public void showLoadView(ImageView imageView, String path) {
            Glide.with(LBannerActivity.this).load(path).centerCrop().into(imageView);
        }


        @Override
        public ImageView createLoadView() {
            return null;
        }
    }
}

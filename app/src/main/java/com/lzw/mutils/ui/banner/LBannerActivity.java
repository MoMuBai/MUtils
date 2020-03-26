package com.lzw.mutils.ui.banner;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzw.mutils.R;
import com.lzw.mutils.tool.To;
import com.lzw.mutils.view.banner.LBanner;
import com.lzw.mutils.view.banner.LBannerImageLoader;
import com.lzw.mutils.view.banner.LBannerImageView;
import com.lzw.mutils.view.banner.LBannerListener;
import com.lzw.mutils.view.banner.LBannerPageChangeListener;
import com.lzw.mutils.view.banner.LBannerStyle;

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

    private LinearLayout swipeRefreshLayout;

    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        lBanner = findViewById(R.id.banner);
        swipeRefreshLayout = findViewById(R.id.swipe);

        data = new ArrayList<>();
        idList = new ArrayList<>();
        data.add("http://c.hiphotos.baidu.com/zhidao/pic/item/d009b3de9c82d1587e249850820a19d8bd3e42a9.jpg");//1
        data.add("http://i1.sinaimg.cn/ent/d/2008-06-04/U105P28T3D2048907F326DT20080604225106.jpg");//2
        data.add("http://dl.ppt123.net/pptbj/51/20181115/mzj0ghw2xo2.jpg");//3
        data.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1000551505,2077899926&fm=26&gp=0.jpg");//1
        data.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2353873632,2644143944&fm=26&gp=0.jpg");//5
        idList.add(R.drawable.zhizhen1);
        idList.add(R.drawable.ic_sp);
        idList.add(R.drawable.aa);
        idList.add(R.drawable.zhizhen1);
        idList.add(R.drawable.ic_sp);

        lBanner.setStyle(LBannerStyle.ViewPagerMaxStyle)
                .setImageLoader(new MyLoader())
                .setIndicator(R.drawable.ic_select_indicator, R.drawable.ic_unselect_indicator)
                .setIndicatorGravity(Gravity.CENTER)
                .setIndicatorSize(8)
                .setIndicatorMargin(0, 0, 8, 12)
                .setShowIndicator(true)
                .setLoop(true)
                .setAutoPlay(true)
                .setDelayTime(3000)
                .setLBannerListener(new LBannerListener() {
                    @Override
                    public void itemClick(int pos) {
                        Toast.makeText(LBannerActivity.this, "点击了：" + pos, Toast.LENGTH_SHORT).show();
                    }
                })
                .setLBannerPageChangeListener(new LBannerPageChangeListener() {
                    @Override
                    public void onPageChange(int pos) {
                    }
                }).build();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lBanner.setImgData(idList).show();
                lBanner.startAutoPlay();
            }
        }, 3000);
    }


    private class MyLoader implements LBannerImageLoader<Integer, ImageView> {

        @Override
        public void showLoadView(ImageView imageView, Integer path) {
            Glide.with(LBannerActivity.this).load(path).centerCrop().into(imageView);
        }


        @Override
        public ImageView createLoadView() {
            return null;
        }
    }
}

package com.lzw.mutils.ui.splash;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.lzw.mutils.view.SplashView;
import com.lzw.mutils.view.image.ContentImageView;


/**
 * Author: lzw
 * Date: 2019-10-25
 * Description: This is SplashActivity
 */
public class SplashActivity extends AppCompatActivity {

    private SplashView splashView;

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frameLayout = new FrameLayout(this);
        frameLayout.addView(new ContentImageView(this));
        splashView = new SplashView(this);
        frameLayout.addView(splashView);
        setContentView(frameLayout);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splashView.startMerging();
            }
        }, 3000);
    }
}

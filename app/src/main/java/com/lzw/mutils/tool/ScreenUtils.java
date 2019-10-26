package com.lzw.mutils.tool;

import android.content.Context;
import android.util.TypedValue;

/**
 * Author: lzw
 * Date: 2018/3/16
 * Description: This is ScreenUtils
 */

public class ScreenUtils {

    private static Context mContext;
    private static ScreenUtils instance;

    public static ScreenUtils getInstance() {
        if (null == instance) {
            instance = new ScreenUtils();
        }
        checkEvn();
        return instance;
    }

    private static void checkEvn() {
        if (null == mContext) {
            return;
        }
    }

    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 获取屏幕高度(px)
     */
    public static int getScreenHeight() {
        return mContext.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth() {
        return mContext.getResources().getDisplayMetrics().widthPixels;
    }

    public static int dp2px(float dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources().getDisplayMetrics()) + 0.5f);
    }

    /**
     * sp转px
     */
    public static int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, mContext.getResources().getDisplayMetrics());
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取导航栏高度
     *
     * @return
     */
    public static int getDaoHangHeight() {
        int result = 0;
        int resourceId = 0;
        int rid = mContext.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = mContext.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return mContext.getResources().getDimensionPixelSize(resourceId);
        } else
            return 0;
    }
}

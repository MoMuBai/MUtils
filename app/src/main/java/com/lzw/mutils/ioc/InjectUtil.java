package com.lzw.mutils.ioc;

import android.util.Log;

import java.lang.reflect.Method;

/**
 * author :lzw
 * date   :2020-01-19-15:32
 * desc   :
 */
public class InjectUtil {
    private static String TAG = "InjectUtil";

    public static void inject(Object context) {
        injectLayout(context);
        injectView(context);
    }


    /**
     * 布局注入
     *
     * @param context
     */
    private static void injectLayout(Object context) {
        int layoutId = 0;
        Class<?> clazz = context.getClass();//1、找到类
        LayoutInject layoutInject = clazz.getAnnotation(LayoutInject.class);
        if (null != layoutInject) {//2、如果这个类添加了LayoutInject的注解
            layoutId = layoutInject.value();//3、找到id，这个id就是我们的通过注解传入的布局文件的id
            //4、通过代理去执行我们setContentView方法，需要传入的参数是一个int
            try {
                Log.d(TAG, "injectLayout: " + layoutId);
                Method method = clazz.getMethod("setContentView", int.class);
                method.invoke(context, layoutId);//5、执行setContentView
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 变量注入
     *
     * @param context
     */
    private static void injectView(Object context) {

    }


}

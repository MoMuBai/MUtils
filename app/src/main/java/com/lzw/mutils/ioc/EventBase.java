package com.lzw.mutils.ioc;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author :lzw
 * date   :2020-01-19-15:26
 * desc   :
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface EventBase {
    //     btn.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//        }
//    });

    //1.setOnClickListener  订阅
    String listenerSetter();

    //2.new View.OnClickListener() 事件
    Class<?> listenerType();

    //3.onClick(View v)  事件处理
    String callBackMethod();

}

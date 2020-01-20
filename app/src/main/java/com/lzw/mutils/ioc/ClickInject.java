package com.lzw.mutils.ioc;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author :lzw
 * date   :2020-01-19-15:22
 * desc   :
 */

@EventBase(listenerSetter = "setOnClickListener"
        , listenerType = View.OnClickListener.class
        , callBackMethod = "onClick")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ClickInject {
    int[] value() default -1;
}

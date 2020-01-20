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
@EventBase(listenerSetter = "setOnLongClickListener"
        , listenerType = View.OnLongClickListener.class
        , callBackMethod = "setOnLongClick")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LongClickInject {
    int[] value() default -1;
}

package com.lzw.mutils.tool.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author :lzw
 * date   :2020/4/22-10:44 AM
 * desc   :
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MSubscribe {
    MThreadMode threadMode() default MThreadMode.POSITING;
}

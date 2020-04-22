package com.lzw.mutils.tool.event;

import java.lang.reflect.Method;

/**
 * author :lzw
 * date   :2020/4/22-10:47 AM
 * desc   : 订阅方法的model，存储订阅的相关信息
 */
public class MSubscribeMethod {
    private Method method;//订阅方法
    private MThreadMode mThreadMode;//订阅方法的指定线程
    private Class<?> eventType;//订阅方法的参数类型

    public MSubscribeMethod(Method method, MThreadMode mThreadMode, Class<?> eventType) {
        this.method = method;
        this.mThreadMode = mThreadMode;
        this.eventType = eventType;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public MThreadMode getmThreadMode() {
        return mThreadMode;
    }

    public void setmThreadMode(MThreadMode mThreadMode) {
        this.mThreadMode = mThreadMode;
    }

    public Class<?> getEventType() {
        return eventType;
    }

    public void setEventType(Class<?> eventType) {
        this.eventType = eventType;
    }
}

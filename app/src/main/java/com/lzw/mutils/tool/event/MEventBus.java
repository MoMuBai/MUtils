package com.lzw.mutils.tool.event;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author :lzw
 * date   :2020/4/22-10:41 AM
 * desc   :
 */
public class MEventBus {


    private static MEventBus eventBus = new MEventBus();

    /**
     * 订阅者+订阅方法的缓存
     */
    private Map<Object, List<MSubscribeMethod>> cacheMap;

    private Handler handler;

    /**
     * 线程池
     */
    private ExecutorService executorService;

    private MEventBus() {
        cacheMap = new HashMap<>();
        handler = new Handler(Looper.getMainLooper());
        executorService = Executors.newCachedThreadPool();
    }

    public static MEventBus getInstance() {
        return eventBus;
    }

    /**
     * 注册
     *
     * @param subscriber
     */
    public void register(Object subscriber) {
        Class<?> aClass = subscriber.getClass();
        List<MSubscribeMethod> mSubscribeMethods = cacheMap.get(subscriber);//获取缓存
        if (mSubscribeMethods == null) {//如果获取的值为null，则将订阅者+订阅方法加入缓存中
            mSubscribeMethods = getSubcribleMethod(subscriber);
            cacheMap.put(subscriber, mSubscribeMethods);
        }
    }

    /**
     * 遍历订阅者获取订阅方法
     *
     * @param subscriber
     * @return
     */
    private List<MSubscribeMethod> getSubcribleMethod(Object subscriber) {
        List<MSubscribeMethod> list = new ArrayList<>();
        Class<?> aClass = subscriber.getClass();
        while (aClass != null) {//这边有点疑问，我只获取当前这个类的订阅方法不就可以了吗？
            String name = aClass.getName();//如果是系统的类，我们就不需要管
            if (name.startsWith("java.") ||
                    name.startsWith("javax.") ||
                    name.startsWith("android.") ||
                    name.startsWith("androidx.")) {
                break;
            }
            Method[] declaredMethods = aClass.getDeclaredMethods();//获取这个订阅者的所有方法
            for (Method method : declaredMethods) {
                MSubscribe annotation = method.getAnnotation(MSubscribe.class);//获取我们的注解
                if (annotation == null) {
                    continue;
                }

                //获取这个方法的参数，这边的参数只能有一个
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new RuntimeException("eventBus只能接收一个参数");
                }

                MThreadMode mThreadMode = annotation.threadMode();
                //生成到我们的订阅方法对象 加入到列表中
                MSubscribeMethod subscribeMethod = new MSubscribeMethod(method, mThreadMode, parameterTypes[0]);
                list.add(subscribeMethod);
            }
            aClass = aClass.getSuperclass();
        }
        return list;
    }


    /**
     * 发送
     *
     * @param obj
     */
    public void post(Object obj) {
        Set<Object> set = cacheMap.keySet();
        Iterator<Object> iterator = set.iterator();
        while (iterator.hasNext()) {
            //拿到订阅者的类
            Object next = iterator.next();

            List<MSubscribeMethod> list = cacheMap.get(next);//拿到订阅者的订阅方法
            for (MSubscribeMethod mSubscribeMethod : list) {

                //根据订阅方法的eventtype是否是来自于我们post传入的对象， isAssignableFrom
                if (mSubscribeMethod.getEventType().isAssignableFrom(obj.getClass())) {//如果这个方法的eventtype是我们post传入的类型，我们就通过反射让这个方法去执行
                    switch (mSubscribeMethod.getmThreadMode()) {
                        case MAIN:
                        case MAIN_ORDERED:
                            if (Looper.myLooper() == Looper.getMainLooper()) {//发送方是在主线程,接收方在主线程
                                invoke(mSubscribeMethod, next, obj);
                            } else {//发送方是在子线程,接收方在主线程
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(mSubscribeMethod, next, obj);
                                    }
                                });
                            }
                            break;
                        case ASYNC:
                            if (Looper.myLooper() == Looper.getMainLooper()) {//发送方是在主线程，但是接收方在子线程
                                executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(mSubscribeMethod, next, obj);
                                    }
                                });
                            } else {//发送方是在子线程，但是接收方在子线程
                                invoke(mSubscribeMethod, next, obj);
                            }
                            break;
                        case POSITING:
                            break;
                        default:
                            break;
                    }
                    invoke(mSubscribeMethod, next, obj);
                }
            }
        }
    }

    /**
     * 反射执行 我们订阅方法，我们的通知就被响应了
     *
     * @param mSubscribeMethod
     * @param next
     * @param obj
     */
    private void invoke(MSubscribeMethod mSubscribeMethod, Object next, Object obj) {
        Method method = mSubscribeMethod.getMethod();
        try {
            method.invoke(next, obj);//反射去执行
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    /**
     * 取消注册
     *
     * @param subscriber
     */
    public void unRegister(Object subscriber) {
        Class<?> aClass = subscriber.getClass();
        List<MSubscribeMethod> list = cacheMap.get(subscriber);
        if (list != null) {//取消注册，从缓存中移除
            cacheMap.remove(subscriber);
        }
    }
}

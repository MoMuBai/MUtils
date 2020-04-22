package com.lzw.mutils.tool.plugin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;

import dalvik.system.DexClassLoader;

/**
 * author :lzw
 * date   :2020/4/1-4:25 PM
 * desc   :
 */
public class PluginHelper {
    private static final String TAG = "PluginHelper";

    private static final String CLASS_DEX_PATH_LIST = "dalvik.system.DexPathList";

    private static final String FIELD_PATH_LIST = "pathList";

    private static final String FIELD_DEX_ELEMENTS = "dexElements";


    private static void loadPluginClass(Context context, ClassLoader hostClassLoader) {
        //1.存放plugin apk的file
        File pluginFile = context.getExternalFilesDir("plugin");

        pluginFile = pluginFile.listFiles()[0];

        //2.生成插件的classLoader
        DexClassLoader pluginClassLoader = new DexClassLoader(pluginFile.getAbsolutePath(), null, null, hostClassLoader);

        //3.通过反射获取到插件中classLoader的field：pathList
        Object pluginPathList = getField(pluginClassLoader, FIELD_PATH_LIST);

        //4.通过反射获取到插件中pathList中的pathElements
        Object pluginPathElements = getField(pluginPathList, FIELD_DEX_ELEMENTS);

        //5.通过反射获取到主工程中classLoader的field：pathList
        Object hostPathList = getField(hostClassLoader, FIELD_PATH_LIST);

        //6.通过反射获取到主工程中pathList的field：pathElements
        Object hostPathElements = getField(hostClassLoader, FIELD_DEX_ELEMENTS);

        //7.合拼宿主apk和插件apk的pathElements

        Object array = combineArray(hostPathElements, pluginPathElements);


    }

    /**
     * 合并Elements
     *
     * @param hostElements
     * @param pluginElements
     * @return
     */
    private static Object combineArray(Object hostElements, Object pluginElements) {
        Class<?> componentType = hostElements.getClass().getComponentType();
        int i = Array.getLength(hostElements);
        int j = Array.getLength(pluginElements);
        int k = i + j;
        Object result = Array.newInstance(componentType, k);
        System.arraycopy(pluginElements, 0, result, 0, j);
        System.arraycopy(hostElements, 0, result, j, i);
        return result;
    }


    private static void setField() {

    }


    /**
     * 反射获取field
     *
     * @param object
     * @param fieldName
     * @return
     */
    private static Object getField(Object object, String fieldName) {
        Class clazz = object.getClass();
        Field field = null;
        try {
            field = clazz.getField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return field;
    }


    /**
     * 反射设置field
     *
     * @param object
     * @param fieldName
     * @param value
     * @return
     */
    private static Object setField(Object object, String fieldName, Object value) {
        Class clazz = object.getClass();
        Field field = null;
        try {
            field = clazz.getField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return field;
    }
}

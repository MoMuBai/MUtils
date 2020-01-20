package com.lzw.mutils

import android.app.Application
import com.lzw.mutils.ioc.InjectUtil
import com.lzw.mutils.tool.ContextInstance
import com.lzw.mutils.tool.ScreenUtils
import com.lzw.mutils.tool.To

/**
 * Author: lzw
 * Date: 2019/3/19
 * Description: This is App
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        To.init(this)
        ContextInstance.setContext(this)
        ScreenUtils.init(this)
    }
}
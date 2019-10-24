package com.lzw.mutils.tool

import android.content.Context

/**
 * Author: lzw
 * Date: 2019/3/19
 * Description: This is ContextInstance
 */
object ContextInstance {
    private lateinit var mInstance: Context

    fun getContext(): Context {
        return mInstance
    }

    fun setContext(context: Context) {
        mInstance = context
    }

}
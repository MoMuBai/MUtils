package com.lzw.mutils.bean;

import java.io.Serializable;

/**
 * Author: lzw
 * Date: 2018/9/7
 * Description: This is BaseResultData
 */

public class BaseResultData<T> implements Serializable {
    private String code;

    private T data;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

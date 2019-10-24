package com.lzw.mutils.tool.permission;

import java.util.List;

/**
 * Author: lzw
 * Date: 2018/8/13
 * Description: This is PermissionCallBack
 */

public abstract class PermissionCallBack {

    /**
     * 申请回调
     *
     * @param hasRefuse  是否有拒绝
     * @param refuseList 拒绝的权限集合
     */
    protected abstract void permitNext(boolean hasRefuse, List<String> refuseList);

    /**
     * 手动开启权限dialog自定义
     */

    public void permitDialogCustom() {

    }
}

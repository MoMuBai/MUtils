package com.lzw.mutils.tool.permission;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Author: lzw
 * Date: 2019-10-22
 * Description: This is PhotoGetUtil
 */
public class CheckPermissionUtils {

    private static final String TAG = "CheckPermissionUtils";
    private static CheckPermissionUtils instance;

    private Context mContext;

    /**
     * 需要申请权限的集合
     */
    private List<String> permissionList;
    /**
     * 申请权限被拒绝的集合
     */
    private List<String> refusePermissionList;
    private List<String> noRemindList;

    private int requestCode;

    private boolean needCustom;

    private PermissionCallBack permissionCallBack;

    private CheckPermissionUtils(Context context) {
        this.mContext = context;
        permissionList = new ArrayList<>();
        refusePermissionList = new ArrayList<>();
        noRemindList = new ArrayList<>();
    }

    public static CheckPermissionUtils getInstance(Context context) {
        if (null == instance) {
            instance = new CheckPermissionUtils(context);
        }
        return instance;
    }


    /**
     * 申请权限
     *
     * @param permissionCallBack 权限申请回调
     * @param requestCode        requestCode
     * @param permissions        需要申请的权限
     */
    public void initPermission(@NonNull PermissionCallBack permissionCallBack, @NonNull int requestCode, String[] permissions) {
        initPermission(permissionCallBack, requestCode, permissions, false);
    }

    /**
     * 申请权限
     *
     * @param permissionCallBack 权限申请回调
     * @param requestCode        requestCode
     * @param permissions        需要申请的权限
     * @param needCustom         是否需要自定义提示dialog
     */
    public void initPermission(@NonNull PermissionCallBack permissionCallBack, @NonNull int requestCode, String[] permissions, boolean needCustom) {
        this.requestCode = requestCode;
        this.permissionCallBack = permissionCallBack;
        permissionList.clear();
        this.needCustom = needCustom;
        noRemindList.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//如果api大于23，需要去申请权限
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(mContext, permissions[i]) != PackageManager.PERMISSION_GRANTED) {//判断该权限是否已经申请了
                    permissionList.add(permissions[i]);
                }
            }
            if (permissionList.size() > 0) {//如果需要申请权限的集合大于0，去申请对应的权限
                String[] permissionArray = new String[permissionList.size()];
                for (int i = 0; i < permissionList.size(); i++) {
                    permissionArray[i] = permissionList.get(i);
                }
                ActivityCompat.requestPermissions((Activity) mContext, permissionArray, requestCode);//申请对应的权限
            } else {
                permissionCallBack.permitNext(false, new ArrayList<String>());//没有需要申请的权限，则直接下一步
            }
        } else {
            permissionCallBack.permitNext(false, new ArrayList<String>());//api小于23，则直接下一步
        }
    }


    /**
     * 申请权限的回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void permissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean isRefuse = false;
        boolean dialogShow = false;
        refusePermissionList.clear();
        if (this.requestCode == requestCode) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        isRefuse = true;
                        refusePermissionList.add(permissions[i]);
                        if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, permissions[i])) {
                            if (!dialogShow) {//是否已经显示了dialog
                                dialogShow = true;
                                if (needCustom) {//需要自定义
                                    permissionCallBack.permitDialogCustom();
                                } else {
                                    new AlertDialog.Builder(mContext)
                                            .setMessage("用户选择了不在提示按钮")
                                            .setPositiveButton("去授权", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //引导用户至设置页手动授权
                                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                    Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                                                    intent.setData(uri);
                                                    mContext.startActivity(intent);
                                                }
                                            })
                                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //引导用户手动授权，权限请求失败
                                                }
                                            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialog) {
                                            //引导用户手动授权，权限请求失败
                                        }
                                    }).show();
                                }
                            }
                        } else {
                            //权限请求失败，但未选中“不再提示”选项
                        }
                    }
                }
                if (null != permissionCallBack && null != refusePermissionList) {
                    permissionCallBack.permitNext(isRefuse, refusePermissionList);
                }
            } else {
                if (null != permissionCallBack && null != permissionList) {
                    permissionCallBack.permitNext(false, permissionList);
                }
            }
        } else {
            Log.d(TAG, "e: requestCode不一致");
        }
    }
}

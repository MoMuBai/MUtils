package com.lzw.mutils.ui;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lzw.mutils.R;
import com.lzw.mutils.tool.To;
import com.lzw.mutils.tool.permission.CheckPermissionUtils;
import com.lzw.mutils.tool.permission.PermissionCallBack;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * 申请权限
     *
     * @param view
     */
    public void permit(View view) {
        CheckPermissionUtils.getInstance(this).initPermission(new PermissionCallBack() {
            @Override
            protected void permitNext(boolean hasRefuse, List<String> refuseList) {
                if (hasRefuse) {
                    To.d("有" + refuseList.size() + "个权限被拒绝");
                } else {
                    To.d("申请成功");
                }
            }
        }, 0x01, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, false);

    }

    /**
     * 申请权限 选择自定义框
     *
     * @param view
     */
    public void permitCustom(View view) {
        CheckPermissionUtils.getInstance(this).initPermission(new PermissionCallBack() {
            @Override
            protected void permitNext(boolean hasRefuse, List<String> refuseList) {
                if (hasRefuse) {
                    To.d("有" + refuseList.size() + "个权限被拒绝");
                } else {
                    To.d("申请成功");
                }
            }

            @Override
            public void permitDialogCustom() {//当用户选择了不在询问后，这边会根据弹框引导用户去授权
                super.permitDialogCustom();
                //TODO 需要自定义弹框引导用户去授权
                Log.d("MainActivity", "自定义弹框引导用户去授权");
            }
        }, 0x01, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CheckPermissionUtils.getInstance(this).permissionsResult(requestCode, permissions, grantResults);
    }
}

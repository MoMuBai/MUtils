package com.lzw.mutils.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lzw.mutils.R;
import com.lzw.mutils.tool.permission.CheckPermissionUtils;
import com.lzw.mutils.tool.permission.PermissionCallBack;

import java.util.List;

/**
 * Author: lzw
 * Date: 2019-11-06
 * Description: This is Camera2Activity
 */
public class Camera2Activity extends AppCompatActivity {

    private static final String TAG = "Camera2Activity";
    private CameraManager cameraManager;

    private CameraDevice mCameraDevice;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_camera2);
        CheckPermissionUtils.getInstance(this).initPermission(new PermissionCallBack() {
            @SuppressLint("MissingPermission")
            @Override
            protected void permitNext(boolean hasRefuse, List<String> refuseList) {
                if (!hasRefuse) {
                    cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                }
            }
        }, 123, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }

    @SuppressLint("NewApi")
    // CameraCharacteristics  可通过 CameraManager.getCameraCharacteristics() 获取
    private int isHardwareSupported(CameraCharacteristics characteristics) {
        Integer deviceLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
        if (deviceLevel == null) {
            Log.e(TAG, "can not get INFO_SUPPORTED_HARDWARE_LEVEL");
            return -1;
        }
        switch (deviceLevel) {
            case CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL:
                Log.w(TAG, "hardware supported level:LEVEL_FULL");
                break;
            case CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY:
                Log.w(TAG, "hardware supported level:LEVEL_LEGACY");
                break;
            case CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_3:
                Log.w(TAG, "hardware supported level:LEVEL_3");
                break;
            case CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED:
                Log.w(TAG, "hardware supported level:LEVEL_LIMITED");
                break;
        }
        return deviceLevel;
    }

    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCameraDevice = camera;
            // 当相机成功打开时回调该方法，接下来可以执行创建预览的操作
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            // 当相机断开连接时回调该方法，应该在此执行释放相机的操作
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            // 当相机打开失败时，应该在此执行释放相机的操作
        }
    };

    @SuppressLint("NewApi")
    public void getLevel(View view) {
        try {
            int a = isHardwareSupported(cameraManager.getCameraCharacteristics("0"));
            Log.d(TAG, "a:" + a);
            Toast.makeText(this, "等级：" + a, Toast.LENGTH_SHORT).show();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CheckPermissionUtils.getInstance(this).permissionsResult(requestCode, permissions, grantResults);//这边只需要将回调参数传递到CheckPermissionUtils就可以
    }

}

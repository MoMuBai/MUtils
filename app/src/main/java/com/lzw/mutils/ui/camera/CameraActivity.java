package com.lzw.mutils.ui.camera;

import android.Manifest;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.lzw.mutils.R;
import com.lzw.mutils.tool.camera.CameraPreview;
import com.lzw.mutils.tool.permission.CheckPermissionUtils;
import com.lzw.mutils.tool.permission.PermissionCallBack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Author: lzw
 * Date: 2019-11-01
 * Description: This is CameraActivity
 */
public class CameraActivity extends FragmentActivity {

    private Camera mCamera;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_camera);

        CheckPermissionUtils.getInstance(this).initPermission(new PermissionCallBack() {
            @Override
            protected void permitNext(boolean hasRefuse, List<String> refuseList) {
                if (!hasRefuse) {
                    mCamera = Camera.open(0);    //初始化 Camera对象
                    CameraPreview mPreview = new CameraPreview(CameraActivity.this, mCamera);
                    FrameLayout camera_preview = (FrameLayout) findViewById(R.id.camera_preview);
                    camera_preview.addView(mPreview);
                }
            }
        }, 123, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }

    private void startCamera() {
        //得到照相机的参数
        Camera.Parameters parameters = mCamera.getParameters();
        for (int i = 0; i < parameters.getSupportedPreviewSizes().size(); i++) {
            Log.d("CameraActivity", "height" + parameters.getSupportedPreviewSizes().get(i).height);
            Log.d("CameraActivity", "width:" + parameters.getSupportedPreviewSizes().get(i).width);
        }
        //图片的格式
        parameters.setPictureFormat(ImageFormat.JPEG);
        //预览的大小是多少
        parameters.setPreviewSize(1920, 960);
        //设置对焦模式，自动对焦
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        //对焦成功后，自动拍照
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    //获取照片
                    mCamera.takePicture(null, null, mPictureCallback);
                }
            }
        });
    }

    public void start(View view) {
        startCamera();
    }

    //获取照片中的接口回调
    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            FileOutputStream fos = null;
            String mFilePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "tt002.png";
            //文件
            File tempFile = new File(mFilePath);
            try {
                //
                fos = new FileOutputStream(tempFile);
                fos.write(data);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //实现连续拍多张的效果
//                mCamera.startPreview();
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }
}

package com.lzw.mutils.tool;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Author: lzw
 * Date: 2019/3/10
 * Description: This is BitmapUtil
 */

public class BitmapUtil {

    /**
     * Bitmap存储图片所占内存大小：图片长度*图片宽度*一个像素点占用的字节数（3264*1840*4=25M）
     *
     * @param resources
     * @param id
     */
    public void getBitmapSize(Resources resources, int id) {
        Bitmap bitmap = BitmapFactory.decodeResource(resources, id);
        int size = bitmap.getByteCount() / 1024 / 1024;
        Log.d("BitmapUtil", "bitmap.size:" + size + "M");
        Log.d("BitmapUtil", "bitmap.getWidth():" + bitmap.getWidth());
        Log.d("BitmapUtil", "bitmap.getHeight():" + bitmap.getHeight());
    }

    /**
     * 质量压缩
     * 质量压缩的话并不能改变所占内存的大小，但是转出的二进制图片数据会变小(bytes.length),适合微信分享的时候传入这个二进制数据
     *
     * @param resources
     * @param id
     */
    public void compressBitmapSize(Resources resources, int id) {
        Bitmap bitmap = BitmapFactory.decodeResource(resources, id);
        ByteArrayOutputStream outPutStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outPutStream);
        byte[] bytes = outPutStream.toByteArray();
        Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        Log.d("BitmapUtil", "bitmap1.size:" + (bitmap1.getByteCount() / 1024 / 1024));
        Log.d("BitmapUtil", "bitmap1.getWidth():" + bitmap1.getWidth());
        Log.d("BitmapUtil", "bitmap1.getHeight():" + bitmap1.getHeight());
        Log.d("BitmapUtil", "bytes.length/1024:" + (bytes.length / 1024));
    }

    /**
     * 采样率压缩
     *
     * @param resources
     * @param id
     */
    public void sampleSize(Resources resources, int id) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeResource(resources, id, options);
        Log.d("BitmapUtil", "bitmap.size:" + (bitmap.getByteCount() / 1024 / 1024));
        Log.d("BitmapUtil", "bitmap.getHeight():" + bitmap.getHeight());
        Log.d("BitmapUtil", "bitmap.getWidth():" + bitmap.getWidth());
    }

    /**
     * 缩放法压缩
     *
     * @param resources
     * @param id
     */
    public void scaleSize(Resources resources, int id) {
        Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 0.5f);
        Bitmap bitmap = BitmapFactory.decodeResource(resources, id);
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0
                , bitmap.getWidth(), bitmap.getHeight()
                , matrix, true);
        Log.d("BitmapUtil", "bitmap1.size:" + (bitmap1.getByteCount() / 1024 / 1024));
        Log.d("BitmapUtil", "bitmap1.getHeight():" + bitmap1.getHeight());
        Log.d("BitmapUtil", "bitmap1.getWidth():" + bitmap1.getWidth());
    }


}

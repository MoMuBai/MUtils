package com.lzw.mutils.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.lzw.mutils.R;
import com.lzw.mutils.ioc.ClickInject;
import com.lzw.mutils.ioc.InjectUtil;
import com.lzw.mutils.ioc.LayoutInject;
import com.lzw.mutils.ioc.LongClickInject;
import com.lzw.mutils.ioc.ViewInject;

/**
 * author :lzw
 * date   :2020-01-19-15:43
 * desc   :
 */
@LayoutInject(R.layout.activity_inject)
public class InjectActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectUtil.inject(this);
    }
    //    @ViewInject(R.id.btn_click)
//    Button btnClick;
//
//    @ClickInject(R.id.btn_click)
//    private void onClick(View view) {
//
//    }
//
//
//    @LongClickInject(R.id.btn_click)
//    private void onLongClick(View view) {
//
//    }
}

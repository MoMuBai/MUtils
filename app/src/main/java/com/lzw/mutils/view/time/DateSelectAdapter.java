package com.lzw.mutils.view.time;

import com.lzw.mutils.view.time.wheel.WheelView;

import java.util.List;

/**
 * Created by Hugh on 2019/6/24.
 */
public class DateSelectAdapter extends WheelView.WheelAdapter {

    private List<String> mList;

    public DateSelectAdapter(List<String> list) {
        this.mList = list;
    }

    @Override
    protected int getItemCount() {
        return mList.size();
    }

    @Override
    protected String getItem(int index) {
        return mList.get(index);
    }
}

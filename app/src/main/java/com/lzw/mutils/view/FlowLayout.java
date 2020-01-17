package com.lzw.mutils.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: lzw
 * Date: 2019-10-24
 * Description: This is FlowLayout
 */
public class FlowLayout extends ViewGroup {

    /**
     * 保存行的高度的集合
     */
    private List<Integer> lstLineHegiht = new ArrayList<>();

    /**
     * 保存每一行view的集合
     */
    private List<List<View>> lstLineView = new ArrayList<>();
    private boolean flag;


    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (flag) {
            return;
        }
        flag = true;

        /**
         * 获取父容器要求子view的宽、高
         * (这边可以理解为父容器的设置的虚拟宽、高，不是真实的宽、高，真实的宽、高需要你自己通过判断测量模式来计算获取)
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        /**
         * 表示当前控件真实的宽、高
         */
        int measureWidth = 0;
        int measureHeight = 0;


        //记录当前行宽，行高，因为存在多行，下一行数据要放到下方，行高需要保存
        int iCurLineWidth = 0;
        int iCurLineHeight = 0;

        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {//精准模式，match/50dp 指具体的大小
            measureWidth = widthSize;
            measureHeight = heightSize;
        } else {//非精准模式，需要由子view的宽、高来决定

            //子view的宽、高
            int childWidth = 0;
            int childHeight = 0;
            //获取子view个数
            int childCount = getChildCount();

            List<View> viewList = new ArrayList<>();
            for (int i = 0; i < childCount; i++) {
                View childAt = getChildAt(i);//得到子view
                measureChild(childAt, widthMeasureSpec, heightMeasureSpec);//让子view自己去测量获取宽、高
                //获取布局参数，这边特指的是xml中子view设置的margin
                MarginLayoutParams params = (MarginLayoutParams) childAt.getLayoutParams();
                //获取子view实际的宽、高 (具体宽、高+margin)
                childWidth = childAt.getMeasuredWidth() + params.leftMargin + params.rightMargin;
                childHeight = childAt.getMeasuredHeight() + params.topMargin + params.bottomMargin;

                //判断是否需要换行，如果当前view的宽大于view的宽，才需要换行
                if (childWidth + iCurLineWidth > widthSize) {//换行
                    iCurLineWidth = 0;
                    measureWidth = Math.max(measureWidth, iCurLineWidth);//view的真实宽
                    measureHeight += iCurLineHeight;//view的真实高度

                    lstLineHegiht.add(measureHeight);//每一行的高度
                    lstLineView.add(viewList);//每一行的view集合

                    //换行后 重新设置新的信息(宽、高)
                    iCurLineWidth = childWidth;
                    iCurLineHeight = childHeight;

                    viewList = new ArrayList<>();
                    viewList.add(childAt);

                } else {//不需要换行
                    iCurLineWidth += childWidth;
                    iCurLineHeight = Math.max(childHeight, iCurLineHeight);//高度
                    viewList.add(childAt);//保存当前view
                }

                //如果正好是最后一行需要将这一行加入到view集合中去，否则就会少了一行
                if (i == childCount - 1) {
                    measureWidth = Math.max(measureWidth, iCurLineWidth);
                    measureHeight += iCurLineHeight;

                    lstLineView.add(viewList);
                    lstLineHegiht.add(iCurLineHeight);
                }
            }
        }
//        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = 0, top = 0, right = 0, bottom = 0;
        int lineCount = lstLineView.size();//多少行
        int curLeft = 0;
        int curTop = 0;
        for (int i = 0; i < lineCount; i++) {//循环获取每一行
            List<View> viewsList = lstLineView.get(i);
            for (int j = 0; j < viewsList.size(); j++) {//对每一行的每个view进行布局
                View childView = viewsList.get(j);
                MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();

                left = curLeft + params.leftMargin;
                top = curTop + params.topMargin;
                right = left + childView.getMeasuredWidth();
                bottom = top + childView.getMeasuredHeight();
                childView.layout(left, top, right, bottom);
                //距离左边的距离，需要view的累加
                curLeft += childView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            }
            curLeft = 0;
            curTop += lstLineHegiht.get(i);//高度
        }
        lstLineView.clear();
        lstLineHegiht.clear();
    }
}

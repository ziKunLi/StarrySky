package com.example.newbies.starrysky.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.example.newbies.starrysky.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChayChan
 * @description: 底部页签根节点
 * @date 2017/6/23  11:02
 */
public class BottomBarLayout extends LinearLayout implements ViewPager.OnPageChangeListener {

    private static final String STATE_INSTANCE = "instance_state";
    private static final String STATE_ITEM = "state_item";


    private ViewPager viewPager;
    /**
     * 子条目个数
     */
    private int childCount;
    private List<BottomBarItem> itemViews = new ArrayList<>();
    /**
     * 当前条目的索引
     */
    private int currentItem;
    private boolean smoothScroll;

    public BottomBarLayout(Context context) {
        this(context, null);
    }

    public BottomBarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BottomBarLayout);
        smoothScroll = ta.getBoolean(R.styleable.BottomBarLayout_smoothScroll,false);
        ta.recycle();

    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(orientation);
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        init();
    }

    private void init() {
        if (viewPager == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        childCount = getChildCount();
        if (viewPager.getAdapter().getCount() != childCount) {
            throw new IllegalArgumentException("LinearLayout的子View数量必须和ViewPager条目数量一致");
        }
        for (int i = 0; i < childCount; i++) {
            if (getChildAt(i) instanceof BottomBarItem) {
                BottomBarItem bottomBarItem = (BottomBarItem) getChildAt(i);
                itemViews.add(bottomBarItem);
                //设置点击监听
                bottomBarItem.setOnClickListener(new MyOnClickListener(i));
            } else {
                throw new IllegalArgumentException("BottomBarLayout的子View必须是BottomBarItem");
            }
        }
        //设置选中项
        itemViews.get(currentItem).setStatus(true);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 页卡切换时的事件响应
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        resetState();
        itemViews.get(position).setStatus(true);
        if (onItemSelectedListener != null){
            onItemSelectedListener.onItemSelected(getBottomItem(position),position);
        }
        //记录当前位置
        currentItem = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 点击按钮的事件
     */
    private class MyOnClickListener implements OnClickListener {

        private int currentIndex;

        public MyOnClickListener(int i) {
            this.currentIndex = i;
        }

        @Override
        public void onClick(View v) {
            //回调点击的位置
            if (onItemSelectedListener != null && currentIndex == currentItem) {
                onItemSelectedListener.onItemSelected(getBottomItem(currentIndex),currentIndex);
            }

            viewPager.setCurrentItem(currentIndex, smoothScroll);
        }
    }

    /**
     * 重置当前按钮的状态
     */
    private void resetState() {
        if (currentItem < itemViews.size()){
            itemViews.get(currentItem).setStatus(false);
        }
    }

    public void setCurrentItem(int currentItem) {
        viewPager.setCurrentItem(currentItem,smoothScroll);
    }

    /**
     * 设置未读数
     * @param position 底部标签的下标
     * @param unReadNum 未读数
     */
    public void setUnRead(int position,int unReadNum){
        itemViews.get(position).setUnReadNum(unReadNum);
    }

    /**
     * 设置提示消息
     * @param position 底部标签的下标
     * @param msg 未读数
     */
    public void setMsg(int position,String msg){
        itemViews.get(position).setMessageText(msg);
    }

    /**
     * 隐藏提示消息
     * @param position 底部标签的下标
     */
    public void hideMsg(int position){
        itemViews.get(position).setNoon();
    }

    /**
     * 显示提示的小红点
     * @param position 底部标签的下标
     */
    public void showNotify(int position){
        itemViews.get(position).setPoint();
    }

    /**
     * 隐藏提示的小红点
     * @param position 底部标签的下标
     */
    public void hideNotify(int position){
        itemViews.get(position).setNoon();
    }

    public int getCurrentItem() {
        return currentItem;
    }

    public void setSmoothScroll(boolean smoothScroll) {
        this.smoothScroll = smoothScroll;
    }

    public BottomBarItem getBottomItem(int position){
        return itemViews.get(position);
    }

    /**
     * @return 当View被销毁的时候，保存数据
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(STATE_ITEM, currentItem);
        return bundle;
    }

    /**
     * @param state 用于恢复数据使用
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            currentItem = bundle.getInt(STATE_ITEM);
            //重置所有按钮状态
            resetState();
            //恢复点击的条目颜色
            itemViews.get(currentItem).setStatus(true);
            super.onRestoreInstanceState(bundle.getParcelable(STATE_INSTANCE));
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    private OnItemSelectedListener onItemSelectedListener;

    public interface OnItemSelectedListener {
        void onItemSelected(BottomBarItem bottomBarItem, int position);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }
}

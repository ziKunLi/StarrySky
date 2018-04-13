package com.example.newbies.starrysky.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 *
 * @author NewBies
 * @date 2018/4/13
 */

public class SampleViewPagerAdapter extends PagerAdapter{
    /**
     * 视图
     */
    private List<View> views;
    /**
     * 用于监听点击事件和长按事件的回调接口
     */
    private ClickCallBack clickCallBack;

    public SampleViewPagerAdapter(List<View> views){
        this.views = views;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == views.get((int)object);
    }
    /**
     * 该方法做了两件事，一，将要呈现的View加入到container中，第二，将代表该View的唯一值返回
     * 注意：这个方法很神奇，他不仅要加载当前要显示的界面进入container中，还要自动的将他认为即将
     *       使用的界面加入到container中，这是为了确保在finishUpdate返回this is be done!并且在
     *       finishUpdate方法执行后，还会自动的加载新的他认为要加载的页面加载到container中
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, final int position){
        View view = views.get(position);
        if(clickCallBack != null){
            //默认配置了点击事件和长按事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickCallBack.onClickListener(position);
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    clickCallBack.onLongClickListener(position);
                    return true;
                }
            });
        }
        container.addView(views.get(position));
        //返回代表该View的唯一值
        return position;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        //简单地对BUG处理，当同一个ViewPager重复打开时，每次打开都会调用该方法，删除上次滑动到的位置和前一个位置的视图
        //所以当新视图的数量比上次的视图少时，就会导致角标越界的错误
        if(position < views.size()){
            container.removeView(views.get(position));
        }
    }

    /**
     * 每次启动界面时，就会调用这个方法
     * @param container
     */
    @Override
    public void startUpdate(ViewGroup container){
        //如果加载的是同一个viewPager，那么当viewPager被关闭后重新打开时，position将会被记录下来
//        int position = step.getCurrentItem();
//        stepId.setText("步骤： " + position + "");
    }

    public void setClickListener(ClickCallBack clickListener){
        this.clickCallBack = clickListener;
    }
    public static interface ClickCallBack{
        void onClickListener(int position);
        void onLongClickListener(int position);
    }
}

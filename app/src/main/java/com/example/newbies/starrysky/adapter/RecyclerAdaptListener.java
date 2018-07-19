package com.example.newbies.starrysky.adapter;

import android.view.View;

/**
 *
 * @author NewBies
 * @date 2018/6/18
 */

public interface RecyclerAdaptListener {

    /**
     * 子组件被点击
     * @param view
     * @param positionX
     */
    void onItemClick(View view, int positionX, int positionY);

    /**
     * 子组件被长按
     * @param view
     * @param positionX
     */
    void onItemLongClick(View view, int positionX, int positionY);
}

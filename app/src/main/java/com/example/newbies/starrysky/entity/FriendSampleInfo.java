package com.example.newbies.starrysky.entity;

import android.graphics.Bitmap;

/**
 *
 * @author NewBies
 * @date 2018/4/14
 */
public class FriendSampleInfo {

    /**
     * 头像
     */
    private Bitmap headImage;
    /**
     * 姓名
     */
    private String name;

    public FriendSampleInfo(Bitmap headImage, String name){
        this.headImage = headImage;
        this.name = name;
    }

    public Bitmap getHeadImage() {
        return headImage;
    }

    public void setHeadImage(Bitmap headImage) {
        this.headImage = headImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.example.newbies.starrysky.entity;

import android.graphics.Bitmap;

import java.util.List;

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
     * 好友名字或群名字
     */
    private String name;
    /**
     * 群里面所有人的ID，或好友ID
     */
    private List<String> ids;

    public FriendSampleInfo(Bitmap headImage, String name, List<String> ids){
        this.headImage = headImage;
        this.name = name;
        this.ids = ids;
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

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}

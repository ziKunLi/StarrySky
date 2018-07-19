package com.example.newbies.starrysky.entity;

import android.graphics.Bitmap;

/**
 * 发送消息的封装类
 * @author NewBies
 * @date 2018/6/27
 */
public class Message {

    /**
     * 发送者用户的头像
     */
    private Bitmap headImage;
    /**
     * 发送者名字
     */
    private String senderName;
    /**
     * 发送者ID
     */
    private String senderId;
    /**
     * 发送的消息
     */
    private String message;

    public Bitmap getHeadImage() {
        return headImage;
    }

    public void setHeadImage(Bitmap headImage) {
        this.headImage = headImage;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}

package com.example.newbies.starrysky;

import com.example.newbies.starrysky.nio.Client;

/**
 * 全局静态数据池
 * @author NewBies
 * @date 2018/6/17
 */
public class StaticDataPool {

    public static Client client = Client.getInstance();
    /**
     * 普通消息接收到的广播
     */
    public static final String GENERAL_MESSAGE = "generalMessage";
}

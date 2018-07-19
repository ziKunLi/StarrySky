package com.example.newbies.starrysky;

import com.example.newbies.starrysky.nio.Client;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 全局静态数据池
 * @author NewBies
 * @date 2018/6/17
 */
public class StaticDataPool {

    public static Client client = Client.getInstance();

    /**
     * 用于登录时建立连接线程的单利线程池
     */
    public static Executor executor = Executors.newSingleThreadExecutor();

    /**
     * 上线用户账号
     */
    public static String userId = "";
    /**
     * 上线用户昵称
     */
    public static String nickName = "";
    /**
     * 普通消息接收到的广播
     */
    public static final String GENERAL_MESSAGE = "generalMessage";
    /**
     * 注册应答消息
     */
    public static final String REGIST_REPLY_MESSAGE = "registReplyMessage";
    /**
     * 登录应答消息
     */
    public static final String LOGIN_REPLY_MESSAGE = "loginReplyMessage";
    /**
     * 初始化好友列表信息
     */
    public static final String INIT_FRIEND_AND_GROUP_MESSAGE = "initFriendAndGroupMessage";
    /**
     * 好友请求信息
     */
    public static final String FRIEND_REQUEST_MESSAGE = "friendRequestMessage";
    /**
     * 添加群或好友不存在的回复消息
     */
    public static final String ADD_REPLY_MESSAGE = "addReplyMessage";
    /**
     * 添加好友的回复消息
     */
    public static final String ADD_FRIEND_REPLY_MESSAGE = "addFriendReplyMessage";
    /**
     * 初始化的好友信息
     */
    public static final String INIT_FRIEND_INFO = "initFriendInfo";
    /**
     * 是否登录成功
     */
    public static boolean isLogin = false;
}

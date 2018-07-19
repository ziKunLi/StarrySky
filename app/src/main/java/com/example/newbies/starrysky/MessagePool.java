package com.example.newbies.starrysky;

import com.example.newbies.starrysky.util.JsonUtil;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author NewBies
 * @date 2018/6/17
 */
public class MessagePool {

    /**
     * 登录信息拼装
     * @param userId
     * @param password
     * @return
     */
    public static String login(String userId, String password){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("type","2");
        hashMap.put("userId",userId);
        hashMap.put("password",password);

        return JsonUtil.ObjectToJson(hashMap);
    }

    /**
     * 普通信息拼装
     * @param senderId
     * @param receivers
     * @param message
     * @return
     */
    public static String generalMessage(String senderId, List<String> receivers, String message){
        HashMap hashMap = new HashMap<>();
        hashMap.put("type","3");
        hashMap.put("senderName",StaticDataPool.nickName);
        hashMap.put("senderId",senderId);
        hashMap.put("receivers",receivers);
        hashMap.put("message",message);

        return JsonUtil.ObjectToJson(hashMap);
    }

    /**
     * 注册
     * @param userId
     * @param nickName
     * @param password
     * @return
     */
    public static String registMessage(String userId, String nickName, String password){
        HashMap hashMap = new HashMap();
        hashMap.put("type","0");
        hashMap.put("userId",userId);
        hashMap.put("nickName",nickName);
        hashMap.put("password",password);

        return JsonUtil.ObjectToJson(hashMap);
    }

    /**
     * 请求初始化信息
     * @param userId
     * @return
     */
    public static String initMessage(String userId){
        HashMap hashMap = new HashMap();
        hashMap.put("type", "6");
        hashMap.put("userId",userId);

        return JsonUtil.ObjectToJson(hashMap);
    }

    /**
     * 添加好友
     * @param userId
     * @param friendId
     * @return
     */
    public static String addFriendMessage(String userId,String friendId){
        HashMap hashMap = new HashMap();
        hashMap.put("type","7");
        hashMap.put("userId",userId);
        hashMap.put("nickName",StaticDataPool.nickName);
        hashMap.put("friendId",friendId);
        hashMap.put("friendNode",friendId);
        return JsonUtil.ObjectToJson(hashMap);
    }

    /**
     * 添加群
     * @param userId
     * @param groupId
     * @return
     */
    public static String addGroupMessage(String userId, String groupId){
        HashMap hashMap = new HashMap();
        hashMap.put("type","8");
        hashMap.put("userId",userId);
        hashMap.put("groupId",groupId);
        return JsonUtil.ObjectToJson(hashMap);
    }

    /**
     * 回复好友请求
     * @param userId
     * @param friendId
     * @param nickName
     * @param status
     * @return
     */
    public static String friendRequestReplyMessage(String userId,String friendId,String nickName,int status, String friendName){
        HashMap hashMap = new HashMap();
        hashMap.put("type","10");
        hashMap.put("status",status + "");
        hashMap.put("userId",userId);
        hashMap.put("nickName",nickName);
        hashMap.put("friendId",friendId);
        hashMap.put("friendName",friendName);
        return JsonUtil.ObjectToJson(hashMap);
    }
}

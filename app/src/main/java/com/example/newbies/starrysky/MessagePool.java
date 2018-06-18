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
     * @param id
     * @param password
     * @return
     */
    public static String login(String id, String password){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("type","2");
        hashMap.put("id",id);
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
        hashMap.put("sender",senderId);
        hashMap.put("receivers",receivers);
        hashMap.put("message",message);

        return JsonUtil.ObjectToJson(hashMap);
    }
}

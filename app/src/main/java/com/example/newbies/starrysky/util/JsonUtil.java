package com.example.newbies.starrysky.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 用于解析JSON数据格式的工具类
 * @author NewBies
 * @date 2018/2/6
 */
public class JsonUtil {

    /**
     * 将json解析为对象
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T jsonToObject(String json, Class<T> clazz){
        return JSON.parseObject(json,clazz);
    }

    /**
     * 将json数据转换为数组
     * @param json
     * @return
     */
    public static <T> List<T> jsonToArray(String json, Class<T> clazz){
        return JSON.parseArray(json, clazz);
    }

    /**
     * 将对象转为json字符串
     * @param object
     * @return
     */
    public static String ObjectToJson(Object object){
        return JSONObject.toJSONString(object);
    }
}

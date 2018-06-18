package com.example.newbies.starrysky.util;

/**
 * 字符串工具类
 * @author NewBies
 * @date 2018/6/1
 */
public class StringUtil {
    public static boolean isNull(String string){
        if (string == null || string.trim().equals("")){
            return true;
        }
        return false;
    }
}

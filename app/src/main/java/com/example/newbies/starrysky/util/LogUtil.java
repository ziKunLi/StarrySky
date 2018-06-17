package com.example.newbies.starrysky.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author NewBies
 * @date 2018/3/14
 */
public class LogUtil {
    private static boolean isLogger =true;

    /**
     * 打印标签
     */
    private static String defaultTag = "Tag";

    /**
     * 点击跳转到类的关键字  eclipse是 at android studio是☞.(任意文字加.)
     */
    private static String jumpKeyWord = "  ☞. ";
    /**
     * logcat在实现上对于message的内存分配大概,2k左右, 所以超过的内容都直接被丢弃,设置文本长度超过LOG_MAXLENGTH分多条打印
     */
    private final static int LOG_MAX_LENGTH = 2048;


    public static void v(String message){
        if(isLogger){
            Log.v(defaultTag,logContent(message) + logLocation());
        }
    }

    public static void v(String tag, String message){
        if(isLogger){
            Log.v(tag,logContent(message) + logLocation());
        }
    }

    public static void d(String message){
        if(isLogger){
            Log.d(defaultTag,logContent(message) + logLocation());
        }
    }

    public static void d(String tag, String message){
        if(isLogger){
            Log.d(tag,logContent(message) + logLocation());
        }
    }

    /**
     * 打印文本
     *
     * @param text
     */
    public static void i(String text) {
        if (isLogger) {
            Log.i(defaultTag, logContent(text) + logLocation());
        }
    }

    public static void i(String tag, String message) {
        if (isLogger) {
            Log.i(tag, logContent(message) + logLocation());
        }
    }

    public static void w(String text) {
        if (isLogger) {
            Log.w(defaultTag, logContent(text) + logLocation());
        }
    }

    public static void w(String tag, String message) {
        if (isLogger) {
            Log.w(tag, logContent(message) + logLocation());
        }
    }

    /**
     * 打印异常文本
     * @param message
     */
    public static void e(String message) {
        if (isLogger) {
            Log.e(defaultTag, logContent(message) + logLocation());
        }
    }

    public static void e(String tag, String message){
        if(isLogger){
            Log.e(tag,logContent(message) + logLocation());
        }
    }

    /**
     * 打印异常
     * @param message
     * @param e
     */
    public static void e(String message, Exception e) {
        if (isLogger) {
            Log.e(defaultTag, logContent(message) + logLocation(), e);
        }
    }

    /**
     * 打印json格式文本
     *
     * @param json
     */
    public static void json(String json) {
        if (isLogger) {
            json("", json);
        }
    }

    /**
     * 打印json格式文本
     * @param prefix
     *            前缀文本
     * @param json
     */
    public static void json(String prefix, String json) {
        if (isLogger) {
            String message = prefix + fomatJson(json);
            Log.i(defaultTag, logContent(message) + logLocation());
        }
    }

    /**
     * 打印内容
     *
     * @param message
     * @return
     */
    private static String logContent(String message) {
        // 内容长度不超过25，前面加空格加到25
        if (message.length() < 25) {

            int minLength = 25 - message.length();
            if (minLength > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < minLength; i++) {
                    stringBuilder.append(" ");
                }
                message = message + stringBuilder.toString();
            }
        }
        // 内容超过logcat单条打印上限，分批打印
        else if (message.length() > LOG_MAX_LENGTH) {
            //应该分为多少条打印
            int logTime = message.length() / LOG_MAX_LENGTH;
            for (int i = 0; i < logTime; i++) {
                String length = message.substring(i * LOG_MAX_LENGTH, (i + 1) * LOG_MAX_LENGTH);
                // 提示
                if (i == 0) {
                    Log.i(defaultTag, "打印分" + logTime + "条显示 :" + length);
                } else {
                    Log.i(defaultTag, "接上条↑" + length);
                }
            }
            message = "接上条↑" + message.substring(logTime * LOG_MAX_LENGTH, message.length());
        }
        return message;
    }

    /**
     * 定位打印的方法
     * @return
     */
    private static StringBuilder logLocation() {
        StackTraceElement logStackTrace = getLogStackTrace();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(jumpKeyWord).append(" (").append(logStackTrace.getFileName()).append(":").append(logStackTrace.getLineNumber() + ")");
        return stringBuilder;
    }

    /**
     * json格式化
     * @param jsonStr
     * @return
     */
    private static String fomatJson(String jsonStr) {
        try {
            jsonStr = jsonStr.trim();
            if (jsonStr.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                return jsonObject.toString(2);
            }
            if (jsonStr.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(jsonStr);
                return jsonArray.toString(2);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "Json格式有误: " + jsonStr;
    }

    /**
     * 获取调用打印方法的栈 index 调用打印i/e/json的index为0
     *
     * @return
     */
    private static StackTraceElement getLogStackTrace() {
        StackTraceElement logTackTraces = null;
        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
        for (int i = 0; i < stackTraces.length; i++) {
            StackTraceElement stackTrace = stackTraces[i];
            if (stackTrace.getClassName().equals(LogUtil.class.getName())) {
                // getLogStackTrace--logLocation--i/e/json--方法栈,所以调用打印方法栈的位置是当前方法栈后的第3个
                logTackTraces = stackTraces[i + 3];
                i = stackTraces.length;
            }
        }
        return logTackTraces;
    }

    /**
     * 设置默认的标签
     * @param defaultTag
     */
    public static void setDefaultTag(String defaultTag) {
        LogUtil.defaultTag = defaultTag;
    }

    /**
     * 设置是否打印日志
     * @param isLogger
     */
    public static void setIsLog(boolean isLogger) {
        LogUtil.isLogger = isLogger;
    }
}

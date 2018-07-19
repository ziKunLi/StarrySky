package com.example.newbies.starrysky.util;


public class StringUtil {
	
	/**
	 * 判断一个字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str){
		if(str == null||str.trim().equals("")){
			return true;
		}
		return false;
	}
	
	public static boolean isRightId(String id){
		if(isNull(id)){
			return false;
		}
		//判断长度
		else if(id.length() != 11){
			return false;
		}
		else{
			//判断是否全为数字
			for(int i = 0; i < id.length(); i++){
				try {
					Integer.parseInt(id.charAt(i) + "");
				} catch (Exception e) {
					return false;
				}
			}
			return true;
		}
	}
}

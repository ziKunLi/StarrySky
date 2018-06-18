package com.example.newbies.starrysky.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 网络上的数据包粘包和拆包问题解决类
 * 但不能解决丢包问题，且如果出现丢包问题，该类可能会导致长时间接收不到数据的问题
 * TCP不会出现丢包问题，但是UDP会
 * @author NewBies
 *
 */
public class PackageUtil {
	
	/**
	 * 用于存储括号，判断是否完整的读取到了一条数据
	 */
	public Stack<String> bracket = new Stack<String>();
	/**
	 * 对于packageList中，在哪几个角标开始是完整的数据
	 */
	public List<Integer> packageIndex = new ArrayList<Integer>();
	/**
	 * 用于暂存接收
	 */
	private List<String> packageList = new ArrayList<String>();
	/**
	 * 上一个未读完的数据包
	 */
	public String prePackage = "";
	/**
	 *  如果5个数据包都没有解析出一条完整的数据，则表示出错
	 */
	private int timeToLive = 5;
	/**
	 * 已生存时间
	 */
	private int liveTime = 0;
	
	/**
	 * 传入一个数据包，对数据包进行判断，返回完整的报文
	 * @param packageString
	 * @return
	 * @throws Exception 
	 */
	public List<String> match(String packageString) throws Exception{
		
		packageIndex.clear();
		packageList.clear();
		//进行括号匹配，确定是否存在完整的数据包
		for(int i = 0; i < packageString.length(); i++){
			if(packageString.charAt(i) == '{'||packageString.charAt(i) == '['||packageString.charAt(i) == '('){
				bracket.push(packageString.charAt(i) + "");
			}
			else if(packageString.charAt(i) == '}'||packageString.charAt(i) == ']'||packageString.charAt(i) == ')'){
				bracket.pop();
				//如果bracket为空，说明括号匹配成功
				if(bracket.isEmpty()){
					packageIndex.add(i);
				}
			}
		}
		
		prePackage += packageString;
		int beginIndex = 0;
		int endIndex = 0;
		
		//如果一个数据包中包含多个报文，那么将每一个报文添加到线性表中去
		for(int i = 0; i < packageIndex.size(); i++){
			//由于当前数据包与前一个未解析的数据包相连起来了，而解析出来的index是没有相连时的index，所以当前的结束长度应该加上未连接前
			//的未解析数据包的长度，即为prePackage.length() - packageString.length() + 1
			endIndex = packageIndex.get(i) + prePackage.length() - packageString.length() + 1;
			//将解析出来的数据添加到list中去
			packageList.add(prePackage.substring(beginIndex,endIndex));
			beginIndex = endIndex;
		}
		
		//当完整的报文截取完毕后就判断该数据包是否有剩下的不完整的报文（说明至少构成了一个完整的报文）
		if(packageIndex.size() > 0 && packageIndex.get(packageIndex.size() - 1) + 1 <= packageString.length()){
			//存储上不完整的数据包
			prePackage = packageString.substring(packageIndex.get(packageIndex.size() - 1) + 1,packageString.length());
		}
		
		//如果没有解析出一条完整的数据，那么就将liveTime加一
		if(packageList.size() == 0){
			liveTime++;
		}
		else{
			//只要解析出了至少一条完整的数据，那么就将liveTime置为0
			liveTime = 0;
		}
		//如果连续几次都没解析出一条完整的数据包，那么就认为网络上丢包了
		if(liveTime == timeToLive){
			throw new Exception("网络可能出现丢包问题");
		}
		return packageList;
	}

	/**
	 * 设置最大生存时间
	 * @param ttl
	 */
	public void setTimeToLive(int ttl){
		this.timeToLive = ttl;
	}
}

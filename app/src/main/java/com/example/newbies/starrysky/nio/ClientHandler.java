package com.example.newbies.starrysky.nio;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.newbies.starrysky.MyApplication;
import com.example.newbies.starrysky.StaticDataPool;
import com.example.newbies.starrysky.util.JsonUtil;
import com.example.newbies.starrysky.util.LogUtil;
import com.example.newbies.starrysky.util.PackageUtil;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author NewBies
 */
public class ClientHandler{


	private Executor executor = Executors.newSingleThreadExecutor();

	private SocketChannel channel;
	/**
	 * 本地广播接收器发送信息的载体
	 */
	private Intent intent;
	/**
	 * 本地广播管理器
	 */
	private LocalBroadcastManager broadcastManager;
	/**
	 * 网络粘包，拆包问题解决的工具类
	 */
	private PackageUtil packageUtil = new PackageUtil();

	public ClientHandler(){
		intent = new Intent();
		//使用全局context获取到本地广播管理器
		broadcastManager = LocalBroadcastManager.getInstance(MyApplication.getContext());
	}
	
	/**
	 * 处理上线信息
	 * @param channel
	 * @param key
	 */
	public boolean handleRegister(SocketChannel channel,SelectionKey key){
		if(channel.isConnectionPending()){  
            try {
				if(channel.finishConnect()){  
					this.channel = channel;
				    //只有当连接成功后才能注册OP_READ事件
				    key.interestOps(SelectionKey.OP_READ);  
				    return true;
				}  
				else{  
				    key.cancel();  
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}  
        }  
		return false;
	}
	
	/**
	 * 发送信息
	 * @param content
	 */
	public void sendMessage(final String content){
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try{
					LogUtil.v("Thread : " + Thread.currentThread().getId());
					channel.write(CharsetHelper.encode(CharBuffer.wrap(content)));
				}catch (Exception e){

				}
			}
		});
	}
	
	/**
	 * 读取服务器发来的信息
	 */
	public void readMessage() {
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		try {
			if(channel.read(byteBuffer) == -1){
				return;
			}
			byteBuffer.flip();
			CharBuffer charBuffer = CharsetHelper.decode(byteBuffer);
			List<String> packageList = packageUtil.match(charBuffer.toString());

			LogUtil.v("package: " + packageList.toString());
			for(int i = 0 ; i < packageList.size(); i++){
				HashMap hashMap = JsonUtil.jsonToObject(packageList.get(i),HashMap.class);
				String type = (String) hashMap.get("type");
				switch (type){
					//注册应答信息
					case "1":
						intent.setAction(StaticDataPool.REGIST_REPLY_MESSAGE);
						intent.putExtra("status",(int)hashMap.get("status"));
						broadcastManager.sendBroadcast(intent);
						break;
					//普通消息
					case "3":
						intent.setAction(StaticDataPool.GENERAL_MESSAGE);
						intent.putExtra("senderId",(String)hashMap.get("senderId"));
						intent.putExtra("senderName",(String)hashMap.get("senderName"));
						intent.putExtra("message",(String)hashMap.get("message"));
						broadcastManager.sendBroadcast(intent);
						break;
					//登录应答消息
					case "4":
						intent.setAction(StaticDataPool.LOGIN_REPLY_MESSAGE);
						intent.putExtra("status",(String)hashMap.get("status"));
						intent.putExtra("nickName",(String)hashMap.get("nickName"));
						broadcastManager.sendBroadcast(intent);
						break;
					//初始化信息
					case "5":
						List friends = (List) hashMap.get("friends");
						intent.setAction(StaticDataPool.INIT_FRIEND_INFO);
						intent.putExtra("friends", String.valueOf(friends));
						broadcastManager.sendBroadcast(intent);
						break;
					//好友请求信息
					case "7":
						intent.setAction(StaticDataPool.FRIEND_REQUEST_MESSAGE);
						intent.putExtra("userId",(String) hashMap.get("userId"));
						intent.putExtra("nickName",(String)hashMap.get("nickName"));
						broadcastManager.sendBroadcast(intent);
						break;
					//添加好友的回复消息
					case "10":
						intent.setAction(StaticDataPool.ADD_FRIEND_REPLY_MESSAGE);
						intent.putExtra("userId",(String)hashMap.get("userId"));
						intent.putExtra("nickName",(String)hashMap.get("nickName"));
						broadcastManager.sendBroadcast(intent);
						break;
					//处理添加群的回复信息
					case "11":
						intent.setAction(StaticDataPool.ADD_REPLY_MESSAGE);
						intent.putExtra("addType",(String)hashMap.get("addType"));
						intent.putExtra("id",(String)hashMap.get("id"));
						intent.putExtra("status",(String)hashMap.get("status"));
						intent.putExtra("name",(String)hashMap.get("name"));
						broadcastManager.sendBroadcast(intent);
						break;
					default:
						break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

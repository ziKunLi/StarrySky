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

	/**
	 * 防止发送信息线程被重复创建
	 */
	private boolean isStart = false;

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
		ByteBuffer byteBuffer = ByteBuffer.allocate(128);
		try {
			channel.read(byteBuffer);
			byteBuffer.flip();
			CharBuffer charBuffer = CharsetHelper.decode(byteBuffer);
			List<String> packageList = packageUtil.match(charBuffer.toString());
			for(int i = 0 ; i < packageList.size(); i++){
				HashMap hashMap = JsonUtil.jsonToObject(packageList.get(i),HashMap.class);
				String type = (String) hashMap.get("type");
				switch (type){
					//注册应答信息
					case "1":
						break;
					//普通消息
					case "3":
						intent.setAction(StaticDataPool.GENERAL_MESSAGE);
						intent.putExtra("sender",(String)hashMap.get("sender"));
						intent.putExtra("message",(String)hashMap.get("message"));
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

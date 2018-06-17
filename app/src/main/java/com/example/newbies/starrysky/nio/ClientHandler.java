package com.example.newbies.starrysky.nio;

import android.content.Intent;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author NewBies
 */
public class ClientHandler{
	
	private SocketChannel channel;
	/**
	 * 本地广播接收器发送信息的载体
	 */
	private Intent intent;
	
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
	public void sendMessage(String content){
		try {
			channel.write(CharsetHelper.encode(CharBuffer.wrap(content)));
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			String answer = charBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

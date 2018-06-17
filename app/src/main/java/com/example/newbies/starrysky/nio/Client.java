package com.example.newbies.starrysky.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author NewBies
 */
public class Client {

	private SocketChannel channel;
    private Selector selector;
    private ClientHandler clientHandler;
	private static Client client = new Client();
	private boolean isOffline = false;
	
	private Client(){}
	
	public static Client getInstance(){
		return client;
	}
	
	/**
	 * 建立连接
	 * @param ip
	 * @param port
	 */
	public void connect(String ip, int port){
		
	    try {  
	        channel = SocketChannel.open();  
	        channel.configureBlocking(false);  
	        //请求连接
	        channel.connect(new InetSocketAddress(ip, port));  
	        selector = Selector.open();  
	        channel.register(selector, SelectionKey.OP_CONNECT); 
            
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }
	}
	
	/**
	 * 设置处理器，同时注册
	 * @param clientHandler
	 */
	public void online(ClientHandler clientHandler){
		this.clientHandler = clientHandler;

		try{
			while(!isOffline){  
				selector.select();
				Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();  
	            while(iterator.hasNext()){  
	                SelectionKey key =  iterator.next();
	                iterator.remove();  
	                   
	                if(key.isConnectable()){ 
	                	//如果发生错误，则下线
	                	if(!clientHandler.handleRegister(channel,key)){
	                		isOffline = true;
	                	}
	                }  
	                else if(key.isReadable()){ 
	                	clientHandler.readMessage();                        
	                }  
	            }   
	        }
		}
		catch (Exception e){
			e.printStackTrace();
		}  
		finally{  
	        if(channel != null){  
	            try {  
	                channel.close();  
	            } catch (IOException e) {                        
	                e.printStackTrace();  
	            }                    
	        }
	        if(selector != null){  
	            try {  
	                selector.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }  
	    }  
	}

	/**
	 * 发送消息
	 * @param mssage
	 */
	public void sendMessage(String mssage){
		if(clientHandler != null&&!isOffline){
			clientHandler.sendMessage(mssage);
		}
	}
}

package com.ctqh.mobile.tcp;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFactory;

import com.ctqh.mobile.tcp.msgs.GameProtocolcodecFactory;
import com.ctqh.mobile.tcp.msgs.MessageLogFilter;

public class NetworkConfig {
	public SocketAddress address;
	public IoHandler handler;
	public int acceptBacklog;
	public int threadPoolSize;
	public ProtocolCodecFactory factory;
	public boolean tcpNoDelay;
	public int readIdleTimeout;
	public int recieveBuffer;
	public int sendBuffer;
	public Map<String, IoFilter> filters = new HashMap<String, IoFilter>();
	
	public NetworkConfig(int port,IoHandler handler){
		address = new InetSocketAddress(port);
		this.handler = handler;
		acceptBacklog=1000;
		threadPoolSize = Runtime.getRuntime().availableProcessors() * 2;
		factory  = new GameProtocolcodecFactory();
		tcpNoDelay = true;
		readIdleTimeout = 10;
		recieveBuffer = 5120;
		sendBuffer = 32768;
		
		filters.put("messageLogFilter", new MessageLogFilter());
//		filters.put("messageFireWallFilter",new MessageFireWallFilter());
	}
	
}

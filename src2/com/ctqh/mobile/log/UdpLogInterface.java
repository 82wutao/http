package com.ctqh.mobile.log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.ctqh.mobile.common.threads.NamedThreadFactory;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.DatagramConnector;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

public class UdpLogInterface {
	public UdpLogInterface() {
	}

	public static UdpLogInterface instance = new UdpLogInterface();

	public static UdpLogInterface getInstance() {
		return instance;
	}

	public static void setInstance(UdpLogInterface obj) {
		instance = obj;
	}
	
	DatagramConnector connector;
	IoSession session;
	public void init(String ip ,int port){
		UdpLogClientHandler handler = new UdpLogClientHandler();
		Executor eventExecutor= Executors.newCachedThreadPool(new NamedThreadFactory("UDPioEvent",true));
		Executor processeExcetor = Executors.newCachedThreadPool(new NamedThreadFactory("UDPioProcessor",true));
         ((ThreadPoolExecutor) processeExcetor).setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		connector = new NioDatagramConnector(eventExecutor,processeExcetor);
		connector.setHandler(handler);
		
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		//chain.addLast("codec", new ProtocolCodecFilter(new UdpLogProtocolcodecFactory()));
		chain.addLast("codec", new ProtocolCodecFilter(
				new TextLineCodecFactory(Charset.forName("UTF-8"))));
		IoFuture connFuture = connector.connect(new InetSocketAddress(ip,port));
		connFuture.addListener(new IoFutureListener<IoFuture>() {
			public void operationComplete(IoFuture future) {
				ConnectFuture connFuture = (ConnectFuture) future;
				if (connFuture.isConnected()) {
					session = future.getSession();
				}
			}
		});
	}
	
	public boolean connected(){
		if (session!=null) {
			return true;
		}
		return false;
	}
	/**
	 * $系统 $行为标签 $时间 $区服 $帐号角色 $数据{a_b_c...}
	 * @throws IOException 
	 */
	public void sendLog(String sys,String tag,String datetime,String serverNum,String accountHero,String logData) throws IOException{
		byte[] bytes_sys=sys==null?new byte[]{'*'}:sys.getBytes("utf8");
		byte[] bytes_tag=tag==null?new byte[]{'*'}:tag.getBytes("utf8");
		byte[] bytes_datetime=datetime==null?new byte[]{'*'}:datetime.getBytes("utf8");
		byte[] bytes_serverNum=serverNum==null?new byte[]{'*'}:serverNum.getBytes("utf8");
		byte[] bytes_accountHero=accountHero==null?new byte[]{'*'}:accountHero.getBytes("utf8");
		byte[] bytes_logData=logData==null?new byte[]{'*'}:logData.getBytes("utf8");
		int length = bytes_sys.length+bytes_tag.length+bytes_datetime.length+bytes_serverNum.length+bytes_accountHero.length+bytes_logData.length;
		
		IoBuffer buffer=IoBuffer.allocate(length+5);
		buffer.put(bytes_sys);
		buffer.put((byte)32);
		buffer.put(bytes_tag);
		buffer.put((byte)32);
		buffer.put(bytes_datetime);
		buffer.put((byte)32);
		buffer.put(bytes_serverNum);
		buffer.put((byte)32);
		buffer.put(bytes_accountHero);
		buffer.put((byte)32);
		buffer.put(bytes_logData);
		buffer.flip();
		session.write(buffer);
	}
	public void sendLog(String info) throws IOException{
		session.write(info);
	}
	
public static void main(String[] args) {
	
}
}

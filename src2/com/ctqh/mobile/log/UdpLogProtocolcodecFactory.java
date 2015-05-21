package com.ctqh.mobile.log;



import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import com.ctqh.mobile.tcp.msgs.GameMsgDecoder;


/**
 * 一个对象工厂，提供了获取编码器和解码器的函数接口。
 * 
 * @author wutao
 * 
 */
public class UdpLogProtocolcodecFactory implements ProtocolCodecFactory {
	private final UdpLogEncoder encoder;


	public UdpLogProtocolcodecFactory() {
		encoder = new UdpLogEncoder();

	}

	public ProtocolEncoder getEncoder(IoSession session) {
		return encoder;
	}

	public ProtocolDecoder getDecoder(IoSession session) {
		return new GameMsgDecoder();
	}
}

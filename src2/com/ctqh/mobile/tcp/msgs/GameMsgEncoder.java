package com.ctqh.mobile.tcp.msgs;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.ctqh.mobile.common.protocol.MessageOutput;

/**
 * 消息编码器。将ResponseMsg对象编码成为连续的字节。
 * 
 * @author wutao
 * 
 */
public class GameMsgEncoder extends ProtocolEncoderAdapter {

	public GameMsgEncoder() {
	}

	/** 在此处实现对ResponseMsg的编码工作，并把它写入输出流中 */
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		MessageOutput value = (MessageOutput) message;
		IoBuffer buffer = value.toMinaBuffer();
		out.write(buffer);
	}

	public void dispose() throws Exception {
	}
}

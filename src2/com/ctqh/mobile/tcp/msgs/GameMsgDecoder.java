package com.ctqh.mobile.tcp.msgs;


import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctqh.mobile.common.protocol.MessageInput;

/**
 * 消息解码器。将连续的字节按照协议规范分割成完整的消息包，并包装成RequestMsg。
 * 
 * @author wutao
 */
public class GameMsgDecoder extends CumulativeProtocolDecoder {

	private static Logger logger = LoggerFactory.getLogger(GameMsgDecoder.class);

//	private static final short BEGINTAG_MSG = 721;
	private int maxPackLength = 1024 * 5;
	private int packHeadLength = 7;
//	private String xml = null;
//	private byte[] xmlcontent;

	public GameMsgDecoder() {
//		StringBuilder policeFile = new StringBuilder();
//		policeFile.append("<cross-domain-policy>\n").append("<site-control permitted-cross-domain-policies=\"all\"/>\n");
//		policeFile.append("<allow-access-from domain=\"*\" to-ports=\"*\" />\n").append("</cross-domain-policy>\0");
//		xml = policeFile.toString();
//		try {
//			xmlcontent = xml.getBytes("UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			logger.error(e.getMessage(), e);
//		}
	}

	public int getMaxLineLength() {
		return maxPackLength;
	}

	public void setMaxLineLength(int maxLineLength) {
		if (maxLineLength <= 0) {
			throw new IllegalArgumentException("maxLineLength: " + maxLineLength);
		}
		this.maxPackLength = maxLineLength;
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer iobuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

			if (iobuffer.remaining() < packHeadLength) {
				return false;
			}
			
			iobuffer.mark();
			
			// 读取消息头部分{short:业务指令,byte:加密魔数,int:包体长度}
			short businessCode = iobuffer.getShort();
			byte magicNum=iobuffer.get();
			int length = iobuffer.getInt();
			
			// 检查读取的包头是否正常，不正常的话清空buffer
			if (length < 0 || length > maxPackLength) {
				logger.error("connection's msg leng < 0 || length > maxPackLength ,{} ",length);
				session.close(true);
				return false;
			}
			
			// 读取正常的消息包，并写入输出流中，以便IoHandler进行处理
			else if (length <= iobuffer.remaining()) {
				// 整个缓冲区的限制指针
				int oldLimit2 = iobuffer.limit();
				// 在一个完整的消息结尾处重设限制指针
				iobuffer.limit(iobuffer.position() + length);
//				int bodyLen = iobuffer.remaining();
				byte[] body = new byte[length];
				iobuffer.get(body);
				
				// 在整个缓冲区的结尾重设限制指针
				iobuffer.limit(oldLimit2);
				
				MessageInput request = new MessageInput(body,0,length);
				request.setMsgCode(businessCode);
				request.setMagicNum(magicNum);
				protocolDecoderOutput.write(request);
				return true;
			} else {
				// 如果消息包不完整， 将指针重新移动消息头的起始位置
				iobuffer.reset();
				return false;
			}
		
	}

	public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
	}

	public void dispose(IoSession session) throws Exception {
	}

//	private boolean passHttp(IoBuffer buffer) {
//		buffer.mark();
//		int len = buffer.remaining();
//		byte[] cont = new byte[len];
//		buffer.get(cont);
//
//		int nlCnt = 0;
//		for (int i = 0; i < len; i++) {
//			if (cont[i] == '\n') {
//				nlCnt++;
//				if (nlCnt == 3) {
//					buffer.position(i+1);
//					return true;
//				}
//			}
//		}
//		buffer.reset();
//		return false;
//	}
}

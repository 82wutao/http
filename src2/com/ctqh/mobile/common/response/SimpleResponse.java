package com.ctqh.mobile.common.response;

import java.util.HashMap;

import com.ctqh.mobile.common.protocol.MessageOutput;

/**
 * 消息内容比较简单的服务端响应。
 * 
 * @author serv_dev
 * 
 */
public class SimpleResponse {
	private static HashMap<String, MessageOutput> byteMsgMap = new HashMap<String, MessageOutput>();
	private static HashMap<Integer, MessageOutput> onlyMsgCodeMsgMap = new HashMap<Integer, MessageOutput>();
	private static HashMap<String, MessageOutput> intStatusMsgMap = new HashMap<String, MessageOutput>();
	private static HashMap<String, MessageOutput> shortStatusMsgMap = new HashMap<String, MessageOutput>();

	/**
	 * 消息格式 状态(byte)
	 * 
	 * @param msgcode
	 * @param value
	 * @return
	 */
	public static MessageOutput byteMsg(int msgcode, int value) {
		if (byteMsgMap.get(msgcode + "_" + value) == null) {
			MessageOutput response = new MessageOutput(msgcode);
			response.writeByte(value);
			byteMsgMap.put(msgcode + "_" + value, response);
		}
		return byteMsgMap.get(msgcode + "_" + value);
	}

	/**
	 * 消息格式 状态(byte)
	 * 
	 * @param msgcode
	 * @param value
	 * @return
	 */
	public static MessageOutput intMsg(int msgcode, int value) {
		if (intStatusMsgMap.get(msgcode + "_" + value) == null) {
			MessageOutput response = new MessageOutput(msgcode);
			response.writeInt(value);
			intStatusMsgMap.put(msgcode + "_" + value, response);
		}
		return intStatusMsgMap.get(msgcode + "_" + value);
	}
	/**
	 * 消息格式 状态(byte)
	 * 
	 * @param msgcode
	 * @param value
	 * @return
	 */
	public static MessageOutput shortMsg(int msgcode, int value) {
		if (shortStatusMsgMap.get(msgcode + "_" + value) == null) {
			MessageOutput response = new MessageOutput(msgcode);
			response.writeShort(value);
			shortStatusMsgMap.put(msgcode + "_" + value, response);
		}
		return shortStatusMsgMap.get(msgcode + "_" + value);
	}

	/**
	 * 只有消息号的消息;
	 * 
	 * @param msgcode
	 * @param value
	 * @return
	 */
	public static MessageOutput onlyMsgCodeMsg(int msgcode) {
		if (onlyMsgCodeMsgMap.get(msgcode) == null) {
			MessageOutput response = new MessageOutput(msgcode);
			onlyMsgCodeMsgMap.put(msgcode, response);
			return response;
		}
		return onlyMsgCodeMsgMap.get(msgcode);
	}
}

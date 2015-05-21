package com.ctqh.mobile.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;

import com.ctqh.mobile.common.protocol.MessageInput;
import com.ctqh.mobile.common.protocol.MessageOutput;
import com.ctqh.mobile.common.protocol.MessageOutputS;

public class HttpConnection {

	private HttpURLConnection connection = null;

	private HttpConnection() {
	}

	public static HttpConnection newHttpConnection(String url)
			throws IOException {
		HttpConnection clientTest = new HttpConnection();
		URL _url = new URL(url);
		clientTest.connection=(HttpURLConnection)_url.openConnection();
		return clientTest;
	}

	public int postMultipartData(MessageOutput output) throws IOException {
//		byte[] msg = convertMsg(output);
		
		ByteBuffer buffer = output.toHttpBuffer();
		
		
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Length", buffer.limit() + "");
		connection
				.setRequestProperty("Content-type", "multipart/form-data");

		connection.getOutputStream().write(buffer.array(),0,buffer.limit());
		
		int response = connection.getResponseCode();
		return response;
	}
	public int postMultipartData(MessageOutputS output) throws IOException {
//		byte[] msg = convertMsg(output);
		
		ByteBuffer buffer = output.toHttpBuffer();
		
		
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Length", buffer.limit() + "");
		connection
				.setRequestProperty("Content-type", "multipart/form-data");

		connection.getOutputStream().write(buffer.array(),0,buffer.limit());
		
		int response = connection.getResponseCode();
		return response;
	}
	
	public MessageInput readResponse()throws IOException{
		InputStream ins = connection.getInputStream();
		int length = ins.available();
		byte[] buff = new byte[length];
		ins.read(buff);
		MessageInput msgBuffer = new MessageInput(buff, 0, length);
		short businessCmd = msgBuffer.readShort();
		byte magicNum = msgBuffer.readByte();
		msgBuffer.readInt();//		int msgBodyLength = 
		
		msgBuffer.setMsgCode(businessCmd);
		msgBuffer.setMagicNum(magicNum);

		return msgBuffer;
	}

	public void close() {
		try {
			this.connection.getInputStream().close();
		} catch (Exception e) {
		}
		try {
			this.connection.getOutputStream().close();
		} catch (Exception e) {
		}
		try {
			this.connection.disconnect();
		} catch (Exception e) {
		}
	}

}
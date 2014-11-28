package http.net;

import http.base.SimpleHttpRequest;
import http.net.kernel.IOService;
import http.net.kernel.IProtocol;
import http.net.kernel.NetworkSession;
import http.net.kernel.XBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class HttpProtocol implements IProtocol {
	private static String Key_Request_Buffer="Key_Request_Buffer";
	private static String Key_Request_Object="Key_Request_Object";
	
	/***
	 * 
	 * @param buffer
	 * @param off
	 * @param length
	 * @return endOfHead
	 */
	private int parseProtocol(byte[] buffer,int off,int length){

		int index =off;
		while (index < length) {
			int ch=buffer[index];
			
			if (ch=='\n') {
				
				if (buffer[index+2]=='\n') {
					return index;
				}
			}
			index++;
		}
		return 0;
	}
	
	@Override
	public int decode(IOService service, NetworkSession session) throws IOException{
		XBuffer buffer=session.getAttribute(Key_Request_Buffer,XBuffer.class);
		if (buffer==null) {
			buffer = new XBuffer(4096);
			buffer.readyWritingToBuffer();
		}
		int off=buffer.getPosition();
		int limit = buffer.getLimit();
		byte[] buff=buffer.getData();
		ByteBuffer temp = ByteBuffer.wrap(buff);
		temp.position(off);
		temp.limit(limit);
		
		int readed=session.read(temp, off, limit-off);
		if (readed<=0) {
			return readed;
		}
		
		SimpleHttpRequest httpRequest=session.getAttribute(Key_Request_Object,SimpleHttpRequest.class);
		if (httpRequest!=null) {
			buffer.setPosition(off+readed);
			buffer.readyReadingFromBuffer();
			httpRequest.append2Body(buffer);
			return readed;
		}
		
		
		int endOfHead=parseProtocol(buff, 0, off+readed);
		if (endOfHead==0) {
			buffer.setPosition(off+readed);
			session.putAttribute(Key_Request_Buffer,buffer);
			return readed;
		}
		
		buffer.setLimit(endOfHead);
		SimpleHttpRequest request = new SimpleHttpRequest();
		request.setHead(buffer);
		session.removeAttribute(Key_Request_Buffer);
		
		service.onRcvMsg(session, request);
		
		if (endOfHead + 4 < (off + readed)) {
			XBuffer body = buffer.cloneSelf();
			body.setPosition(endOfHead + 4);
			body.setLimit(off + readed);
			request.append2Body(body);
			
			session.putAttribute(Key_Request_Object,request);
		}

		
		return readed;
	}

	@Override
	public ByteBuffer encode(IOService service, NetworkSession session,
			XBuffer response) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}

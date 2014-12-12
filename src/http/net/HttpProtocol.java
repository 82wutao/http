package http.net;

import http.HttpProccesser;
import http.api.HttpResponse;
import http.base.SimpleHttpRequest;
import http.net.kernel.XBuffer;

import java.io.IOException;
import java.net.Socket;

public class HttpProtocol  {
	
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
					return (index-1);
				}
			}
			index++;
		}
		return 0;
	}
	
	public SimpleHttpRequest decode(Socket session,HttpProccesser proccesser) throws IOException{
		XBuffer buffer=proccesser.buffer;
		if (buffer==null) {
			buffer = new XBuffer(4096);
			buffer.readyWritingToBuffer();
		}
		int off=buffer.getPosition();
		int limit = buffer.getLimit();
		byte[] buff=buffer.getData();
		
		int readed=session.getInputStream().read(buff, off, limit-off);
		if (readed<=0) {
			return null;
		}
		
		
		int endOfHead=parseProtocol(buff, 0, off+readed);
		if (endOfHead==0) {
			buffer.setPosition(off+readed);
			return null;
		}
		
		buffer.setLimit(endOfHead);
		SimpleHttpRequest request = new SimpleHttpRequest(session);
		request.setHead(buffer);
		
		if (endOfHead + 4 < (off + readed)) {
			XBuffer body = buffer.cloneSelf();
			body.setPosition(endOfHead + 4);
			body.setLimit(off + readed);
			request.bodyBegin(body);
		}
		
		return request;
	}

	public void encode(Socket session,HttpResponse response) throws IOException {
		response.serialize(session.getOutputStream());
	}
	
}

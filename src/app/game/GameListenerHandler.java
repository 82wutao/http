package app.game;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import common.io.XBuffer;
import http.api.HttpRequest;
import http.api.HttpResponse;
import http.api.RequestBody;
import http.base.HttpProtocol;
import http.base.SimpleHttpResponse;
import http.protocol.ContentType;
import net.Handler;
import net.IOListener;
import net.kernel.NetSession;


public class GameListenerHandler implements IOListener<HttpProtocol>,Handler<HttpProtocol>{
	private Map<NetSession<HttpProtocol>, HttpProtocol> session_request=new HashMap<NetSession<HttpProtocol>, HttpProtocol>();
	
	@Override
	public void closedChannel(NetSession<HttpProtocol> session) {
		session_request.remove(session);
	}

	@Override
	public void connectedChannel(NetSession<HttpProtocol> session) {

	}

	@Override
	public void opennedChannel(NetSession<HttpProtocol> session) {
		HttpProtocol request =new HttpProtocol(Charset.forName("UTF-8"), session);
		session_request.put(session,request);
	}

	
	@Override
	public HttpProtocol readable(NetSession<HttpProtocol> session) {
		HttpProtocol request =session_request.get(session);
		if (request == null ) {
			request=new HttpProtocol(Charset.forName("UTF-8"), session);
		}
		try {
			boolean ok =request.parseProtocol();
			if (ok) {
				return request;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void writed(NetSession<HttpProtocol> session) {

	}

	@Override
	public void handle(NetSession<HttpProtocol> session, HttpProtocol request) {
		request.getRequestHeads().put(HttpRequest.Content_Type,ContentType.Application_OctetStream);
		
		String content_length = request.getRequestHead(HttpRequest.Content_Length);
		int length = Integer.parseInt(content_length);
		
		byte[] buff = new byte[length];
		
		
		
		
		RequestBody body;
		try {
			body = request.getRequestBody();
			

		if (body.hasMorePart()!=0) {
			int readed = 0;
			while(readed < length){				
				int ret =body.read(buff,readed,length - readed);
				if (ret ==0||
						ret == -1) {
					sendError("Rrequest connection has be closed when reading data from it",null);
					return;
				}
				readed += ret;
			}
		}
		XBuffer msgBuffer= new XBuffer(buff, 0, length);
		
		msgBuffer.readString(Charset.forName("UTF-8"));
		short businessCmd = msgBuffer.readshort(1);
		byte magicNum = msgBuffer.readbyte();
		int msgBodyLength = msgBuffer.readint(1);
		
		System.out.println("MsgCode "+businessCmd);
		System.out.println("MagicNum "+magicNum);
		System.out.println("AccountName "+msgBuffer.readString(Charset.forName("UTF-8")));
		System.out.println("Password "+msgBuffer.readString(Charset.forName("UTF-8")));
		
		
		
		
		SimpleHttpResponse resp=new SimpleHttpResponse(session);
		resp.setCharset("UTF-8");
		resp.setHttpVersion("HTTP/1.1");
		resp.setStatusCode(200);
		resp.setContentType(ContentType.Application_OctetStream);
		
		resp.setResponseHead(HttpRequest.Content_Type, ContentType.Application_OctetStream);
		
		msgBuffer.readyWritingToBuffer();
		msgBuffer.setPosition(7);
		msgBuffer.writeshort((short)0,1);
		
		msgBuffer.writeString("-----------", Charset.forName("UTF-8"));
		msgBuffer.writebyte((byte)0);
		length = msgBuffer.getPosition();
		
		short msgCode=10006;
		msgBuffer.writeshort(0, msgCode,1);
		msgBuffer.writebyte(2, (byte)0);
		msgBuffer.writeint(3, length-7,1);
		msgBuffer.readyReadingFromBuffer();
		
		
		resp.write(buff, 0, length);

		resp.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	
	private void sendError(String tips,HttpResponse resp) throws IOException{
	}
}

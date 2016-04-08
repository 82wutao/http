package app.msgstream;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import common.log.AppLogger;
import common.log.AppLogger.LogLvl;
import http.api.HttpRequest;
import http.api.HttpResponse;
import http.api.RequestBody;
import http.base.HttpProtocol;
import http.base.SimpleHttpResponse;
import http.protocol.ContentType;
import net.Handler;
import net.IOListener;
import net.kernel.NetSession;

public class MsgStreamServer implements IOListener<HttpProtocol>,Handler<HttpProtocol>{
	
	private Map<NetSession<HttpProtocol>, HttpProtocol> session_request=new HashMap<NetSession<HttpProtocol>, HttpProtocol>();
	
	@Override
	public void closedChannel(NetSession<HttpProtocol> session) {
		session_request.remove(session);
	}

	@Override
	public void connectedChannel(NetSession<HttpProtocol> session) {
		// nothing
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
		
		RequestBody body = null;
		try {
			body=request.getRequestBody();
		} catch (IOException e) {
			AppLogger logger =AppLogger.getLogger("debug");
			logger.log(LogLvl.Debug, "沒有發現請求體，更不能發現json");
			
			try {
				session.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
		
		String json =null;
		try {
			if (body.hasMorePart()==0) {
				AppLogger logger =AppLogger.getLogger("debug");
				logger.log(LogLvl.Debug, "請求體不能展開，更不能發現json");
				
				try {
					session.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return;
			}
			
			json=body.getString();
			
		} catch (IOException e) {
			AppLogger logger =AppLogger.getLogger("debug");
			logger.log(LogLvl.Debug, "請求體展開出錯，更不能發現json");
			
			try {
				session.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
		
		

		System.out.println("json "+json);
	
		SimpleHttpResponse resp=new SimpleHttpResponse(session);
		resp.setCharset("UTF-8");
		resp.setHttpVersion("HTTP/1.1");
		resp.setStatusCode(200);
		resp.setContentType(ContentType.Application_Json);
		
		resp.setResponseHead(HttpRequest.Content_Type, ContentType.Application_Json);
		resp.write(json);
		
		

		try {
			resp.flush();
		} catch (IOException e) {
			AppLogger logger =AppLogger.getLogger("debug");
			logger.log(LogLvl.Debug, "測試response輸出出錯");
		}

	}
	
	

	
	private void sendError(String tips,HttpResponse resp) throws IOException{
		AppLogger logger =AppLogger.getLogger("debug");
		logger.log(LogLvl.Debug, tips);
	}
}

package http;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import common.log.AppLogger;
import http.base.SimpleHttpResponse;
import net.Handler;
import net.IOListener;
import net.kernel.NetSession;

public class HttpListenerHandler implements IOListener<HttpProtocol>,Handler<HttpProtocol> {
	
	private WebAppContext context = null;
	private Map<NetSession<HttpProtocol>, HttpProtocol> session_request=new HashMap<NetSession<HttpProtocol>, HttpProtocol>();
	
	public HttpListenerHandler( WebAppContext context ) {
		this.context = context;
	}

	
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
	public HttpProtocol readable(NetSession<HttpProtocol> session,int readable) {
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
	public void writed(NetSession<HttpProtocol> session,int writed) {

	}

	@Override
	public void handle(NetSession<HttpProtocol> session, HttpProtocol request) {
		
		SimpleHttpResponse response= new SimpleHttpResponse(session);
		response.setHttpVersion(request.getHttpVersion());
		response.setCharset(request.getCharset());
		
		try {
			this.context.doService(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {			
			try {
				response.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//		String method =request.getRequestMethod();
//		String uri = request.getRequestUri();
//		String version=request.getHttpVersion();
//		AppLogger.getLogger("debug").log(AppLogger.LogLvl.Debug, method+"_"+uri+"_"+version+"\\r\\n");
//		
//		
//		for(Entry<String, String> header:request.getRequestHeads().entrySet()){
//			AppLogger.getLogger("debug").log(AppLogger.LogLvl.Debug, header.getKey()+":"+header.getValue()+"\\r\\n");
//		}
//		AppLogger.getLogger("debug").log(AppLogger.LogLvl.Debug, "\\r\\n");
//
//		
//		SimpleHttpResponse response=new SimpleHttpResponse(session);
//		response.setCharset("UTF-8");
//		
//		response.setHttpVersion(version);
//
//		
//		response.setHttpVersion("HTTP/1.1");
//		response.setStatusCode(200);
//		response.setContentType("text/html");
//		response.write("<html><head><title>It is a response page!</title></head>");
//		
//		response.write("hello world");
//		response.write("</body></html>");
//		try {
//			response.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}finally {
//			session_request.remove(session);
//		}
		
	}
}

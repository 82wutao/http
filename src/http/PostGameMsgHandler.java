package http;

import java.io.IOException;
import java.util.Map.Entry;

import http.base.HttpProtocol;
import http.base.SimpleHttpResponse;
import net.Handler;
import net.kernel.NetSession;

public class PostGameMsgHandler implements Handler<HttpProtocol> {
	public PostGameMsgHandler() {
	}

	@Override
	public void handle(NetSession<HttpProtocol> session, HttpProtocol request) {
		String method =request.getRequestMethod();
		String uri = request.getRequestUri();
		String version=request.getHttpVersion();
		System.out.println(method+"_"+uri+"_"+version+"\\r\\n");
		
		for(Entry<String, String> header:request.getRequestHeads().entrySet()){
			System.out.println(header.getKey()+":"+header.getValue()+"\\r\\n");			
		}
		System.out.println("\\r\\n");

		SimpleHttpResponse response=new SimpleHttpResponse(session);
		response.setHttpVersion(version);

		
		response.setHttpVersion("HTTP/1.1");
		response.setStatusCode(200);
		response.setContentType("text/html");
		response.write("<html><head><title>It is a response page!</title></head>");
		
		response.write("hello world");
		response.write("</body></html>");
		try {
			response.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

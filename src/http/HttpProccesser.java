package http;

import http.api.ServerContext;
import http.api.WebAppContext;
import http.base.SimpleHttpRequest;
import http.base.SimpleHttpResponse;

import java.io.IOException;
import java.net.Socket;

import common.io.XBuffer;
import net.HttpProtocol;

public class HttpProccesser implements Runnable {
	private Socket client = null;
	public XBuffer buffer = null;
	HttpProtocol protocol = null;
	ServerContext context = null;

	public HttpProccesser(ServerContext serverContext, Socket socket,
			HttpProtocol protocol, int capacity) {
		client = socket;
		buffer = new XBuffer(capacity);
		this.protocol = protocol;
		context = serverContext;
	}

	@Override
	public void run() {
		SimpleHttpRequest request = null;
		Exception exception = null;
		SimpleHttpResponse response =null;
		try {
			request = protocol.decode(client, this);
			while (request == null) {
				request = protocol.decode(client, this);
			}
		} catch (IOException e) {
			exception = e;
			response = new SimpleHttpResponse(null,this.buffer);
			response.setHttpVersion("HTTP/1.1");
			response.setStatusCode(500);
			response.setContentType("text/html");
			response.write("<html><head><title>Server Error</title></head>");
			
			response.write("<body>");
			response.write(e.getMessage()+"<br/>");
			StackTraceElement[] stackInfo=e.getStackTrace();
			for (int i = 0; i < stackInfo.length; i++) {
				response.write("&nbsp;&nbsp;&nbsp;&nbsp;"+stackInfo[i].getClassName()+":"+stackInfo[i].getLineNumber()+"<br/>");
			}
			response.write("</body></html>");
		}
		

		
		if (exception == null) {
			try {
				request.parseParamers();
				
				String uri = request.getRequestUri();
				WebAppContext appContext = context.mappingAppContext(uri);
				
				response=new SimpleHttpResponse(appContext,buffer);
				response.setHttpVersion("HTTP/1.1");
				
				appContext.doService(request, response);
			} catch (Exception e) {
				exception = e;
				e.printStackTrace();
				response = new SimpleHttpResponse(null,buffer);
				response.setHttpVersion("HTTP/1.1");
				response.setStatusCode(500);
				response.setContentType("text/html");
				response.write("<html><head><title>Server Error</title></head>");
				
				response.write("<body>");
				response.write(e.getMessage()+"<br/>");
				StackTraceElement[] stackInfo=e.getStackTrace();
				for (int i = 0; i < stackInfo.length; i++) {
					response.write("&nbsp;&nbsp;&nbsp;&nbsp;"+stackInfo[i].getClassName()+":"+stackInfo[i].getLineNumber()+"<br/>");
				}
				response.write("</body></html>");
			}
		}

		try {
			protocol.encode(client, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void destory() throws IOException {
		client.close();
	}
}

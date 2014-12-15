package http;

import http.api.ServerContext;
import http.api.WebAppContext;
import http.base.SimpleHttpRequest;
import http.base.SimpleHttpResponse;
import http.net.HttpProtocol;
import http.net.kernel.XBuffer;

import java.io.IOException;
import java.net.Socket;

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
			response = new SimpleHttpResponse(null);
			response.setHttpVersion("HTTP/1.1");
			response.setStatusCode(404);
			response.setContentType("text/html");
			response.write("<html><head><title>Not founded</title></head><body>Not founded</body></html>");
		}
		

		
		if (exception == null) {
			try {
				request.parseParamers();
				
				String uri = request.getRequestUri();
				WebAppContext appContext = context.mappingAppContext(uri);
				
				response=new SimpleHttpResponse(appContext);
				response.setHttpVersion("HTTP/1.1");
				
				appContext.doService(request, response);
			} catch (Exception e) {
				exception = e;
				e.printStackTrace();
				response = new SimpleHttpResponse(null);
				response.setHttpVersion("HTTP/1.1");
				response.setStatusCode(500);
				response.setContentType("text/html");
				response.write("<html><head><title>Not founded</title></head><body>Not founded</body></html>");
			}
		}

		try {
			protocol.encode(client, response);
		} catch (IOException e) {
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

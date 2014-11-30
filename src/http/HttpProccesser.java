package http;

import http.app.WebAppContext;
import http.base.SimpleHttpRequest;
import http.base.SimpleHttpResponse;
import http.net.kernel.HttpProtocol;
import http.net.kernel.XBuffer;

import java.io.IOException;
import java.net.Socket;

public class HttpProccesser implements Runnable {
	private Socket client = null;
	public XBuffer buffer = null;
	HttpProtocol protocol = null;
	HttpServerContext context = null;

	public HttpProccesser(HttpServerContext serverContext, Socket socket,
			HttpProtocol protocol, int capacity) {
		client = socket;
		buffer = new XBuffer(capacity);
		this.protocol = protocol;
		context = serverContext;
	}

	@Override
	public void run() {
		SimpleHttpRequest request = null;
		SimpleHttpResponse response = new SimpleHttpResponse("HTTP/1.1");

		Exception exception = null;

		try {
			request = protocol.decode(client, this);
			while (request == null) {
				request = protocol.decode(client, this);
			}
		} catch (IOException e) {
			exception = e;
			response.setStatusCode(400);
		}
		if (exception == null) {
			try {
				String uri = request.getRequestUri();
				WebAppContext appContext = context.mappingAppContext(uri);
				appContext.doService(request, response);
			} catch (Exception e) {
				exception = e;
				response.setStatusCode(500);
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

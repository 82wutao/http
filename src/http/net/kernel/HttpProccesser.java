package http.net.kernel;

import http.HttpServerContext;
import http.base.SimpleHttpRequest;
import http.base.SimpleHttpResponse;

import java.io.IOException;
import java.net.Socket;

public class HttpProccesser implements Runnable {
	private Socket client = null;
	public XBuffer buffer = null;
	HttpProtocol protocol =null;
	HttpServerContext context =null;
	
	public HttpProccesser(HttpServerContext serverContext,Socket socket, HttpProtocol protocol, int capacity) {
		client = socket;
		buffer = new XBuffer(capacity);
		this.protocol = protocol;
		context = serverContext;
	}

	@Override
	public void run() {
		try {
			SimpleHttpRequest readed =protocol.decode( client, this);
			while (readed==null) {
				readed =protocol.decode( client, this);
			}
			
			context.doFilters(this,readed,new SimpleHttpResponse(client));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void destory() throws IOException{
		client.close();
	}
}

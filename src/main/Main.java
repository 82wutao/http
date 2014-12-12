package main;

import http.HttpServerContext;
import http.api.ServerContext;
import http.net.HttpProtocol;
import http.net.kernel.IOService;
import http.net.kernel.NetworkConfig;

import java.io.File;
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		ServerContext serverContext = new HttpServerContext();
		serverContext.initial(new File("conf.txt"));

		NetworkConfig config = new NetworkConfig("", 8080, new HttpProtocol());
		
		IOService service = new IOService(serverContext);
		service.configure(config);
		service.startListen();
	}
}

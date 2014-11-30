package main;

import http.HttpServerContext;
import http.net.kernel.HttpProtocol;
import http.net.kernel.IOService;
import http.net.kernel.NetworkConfig;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		HttpServerContext serverContext = new HttpServerContext();
		serverContext.initial("conf.txt");

		NetworkConfig config = new NetworkConfig("", 8090, new HttpProtocol());
		IOService service = new IOService(serverContext);
		service.configure(config);

		service.startListen();
	}
}

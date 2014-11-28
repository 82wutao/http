package main;

import http.HttpServerContext;
import http.net.HttpProtocol;
import http.net.kernel.Acceptor;
import http.net.kernel.IOHandler;
import http.net.kernel.IOService;
import http.net.kernel.NetworkConfig;

import java.io.IOException;

public class Main {
public static void main(String[] args) throws IOException {
	
	NetworkConfig config=new NetworkConfig("",8090, new HttpServerContext(), new HttpProtocol());
	IOService service=new IOService();
	service.configure(config, true);
	
	Acceptor acceptor=new Acceptor();
	acceptor.configure(config);
	service.addAcceptor(acceptor);
	
	service.startListen();
}
}

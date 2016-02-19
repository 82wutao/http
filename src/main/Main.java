package main;

import http.HttpServerContext;
import http.api.ServerContext;

import java.io.File;
import java.io.IOException;

import net.HttpProtocol;
import net.kernel.IOService;
import net.kernel.NetworkConfig;

public class Main {
	public static void main(String[] args) throws IOException {
		if (args.length<2) {
			System.err.println("java -jar tools.jar path_of_conf.txt port");
			System.exit(1);
		}
		
		ServerContext serverContext = new HttpServerContext();
		serverContext.initial(new File(args[0]));

		NetworkConfig config = new NetworkConfig("", Short.parseShort(args[1]), new HttpProtocol());
		
		IOService service = new IOService(serverContext);
		service.configure(config);
		service.startListen();
	}
}

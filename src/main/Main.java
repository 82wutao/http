package main;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import app.game.GameListenerHandler;
import http.HttpServerContext;
import http.api.ServerContext;
import http.base.HttpProtocol;
import net.kernel.NetSession;
import net.kernel.XIOService;
import net.kernel.XNetworkConfig;

public class Main {
	public static void main(String[] args) throws IOException {
//		if (args.length<2) {
//			System.err.println("java -jar tools.jar path_of_conf.txt port");
//			System.exit(1);
//		}
		
		ServerContext serverContext = new HttpServerContext();
//		serverContext.initial(new File(args[0]));
//
//		NetworkConfig config = new NetworkConfig("", Short.parseShort(args[1]), new HttpProtocol());
//		
//		IOService service = new IOService(serverContext);
//		service.configure(config);
//		service.startListen();
		
		
		final XIOService<HttpProtocol> xioService=new XIOService<HttpProtocol>(serverContext);
		
		
		XNetworkConfig<HttpProtocol> config=new XNetworkConfig<HttpProtocol>("",80,1000,true) {
			@Override
			public NetSession<HttpProtocol> newNetworkSession(SocketChannel channel) {
				return new NetSession<HttpProtocol>(xioService, this, channel, true, this.rcvBuffer, this.sendBuffer);
			}
		};
		GameListenerHandler app = new GameListenerHandler();
		config.setupApplication(app, app);
		
		List<XNetworkConfig<HttpProtocol>> configs = new ArrayList<XNetworkConfig<HttpProtocol>>();
		configs.add(config);
		
		xioService.start();
		xioService.newServerAccepter(configs);
	}
}

package http;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import net.kernel.NetSession;
import net.kernel.XIOService;
import net.kernel.XNetworkConfig;

public class HttpServer {
	
	private XIOService<HttpProtocol> xioService =null;
	
	public void start() throws IOException{
		WebAppContext serverContext = new WebAppContext();
		serverContext.initial(new File("config.txt"));
		
		xioService=new XIOService<HttpProtocol>(serverContext);
		
		XNetworkConfig<HttpProtocol> config=new XNetworkConfig<HttpProtocol>("",80,1000,true) {
			@Override
			public NetSession<HttpProtocol> newNetworkSession(SocketChannel channel) {
				return new NetSession<HttpProtocol>(xioService, this, channel, true, this.rcvBuffer, this.sendBuffer);
			}
		};
		HttpListenerHandler app = new HttpListenerHandler(serverContext);
		config.setupApplication(app, app);
		
		List<XNetworkConfig<HttpProtocol>> configs = new ArrayList<XNetworkConfig<HttpProtocol>>();
		configs.add(config);
		
		xioService.start();
		xioService.newServerAccepter(configs);
		
		
	}
	public void stop() throws IOException{
		xioService.stopListen();
	}
}

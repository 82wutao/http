package main;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import app.game.GameListenerHandler;
import common.log.AppLogger;
import http.HttpListenerHandler;
import http.HttpProtocol;
import http.HttpServerContext;
import net.ServerContext;
import net.kernel.NetSession;
import net.kernel.XIOService;
import net.kernel.XNetworkConfig;

public class Main {
	public static void main(String[] args) throws IOException {
		AppLogger.initailLogs("debug", AppLogger.LogLvl.Debug,System.out);
		
		ServerContext serverContext = new HttpServerContext();

		final XIOService<HttpProtocol> xioService=new XIOService<HttpProtocol>(serverContext);
		
		
		XNetworkConfig<HttpProtocol> config=new XNetworkConfig<HttpProtocol>("",80,1000,true) {
			@Override
			public NetSession<HttpProtocol> newNetworkSession(SocketChannel channel) {
				return new NetSession<HttpProtocol>(xioService, this, channel, true, this.rcvBuffer, this.sendBuffer);
			}
		};
		HttpListenerHandler app = new HttpListenerHandler();
		config.setupApplication(app, app);
		
		List<XNetworkConfig<HttpProtocol>> configs = new ArrayList<XNetworkConfig<HttpProtocol>>();
		configs.add(config);
		
		xioService.start();
		xioService.newServerAccepter(configs);
	}
}

package app.msgstream;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import common.log.AppLogger;
import http.HttpProtocol;
import http.WebAppContext;
import net.ServerContext;
import net.kernel.NetSession;
import net.kernel.XIOService;
import net.kernel.XNetworkConfig;

public class Runner {
	public static void main(String[] args) throws IOException {
		AppLogger.initailLogs("debug", AppLogger.LogLvl.Debug,System.out);
		ServerContext serverContext = new WebAppContext();
		
		
		final XIOService<HttpProtocol> xioService=new XIOService<HttpProtocol>(serverContext);
		
		
		XNetworkConfig<HttpProtocol> config=new XNetworkConfig<HttpProtocol>("",80,1000,true) {
			@Override
			public NetSession<HttpProtocol> newNetworkSession(SocketChannel channel) {
				return new NetSession<HttpProtocol>(xioService, this, channel, true, this.rcvBuffer, this.sendBuffer);
			}
		};
		MsgStreamServer app = new MsgStreamServer();
		config.setupApplication(app, app);
		
		List<XNetworkConfig<HttpProtocol>> configs = new ArrayList<XNetworkConfig<HttpProtocol>>();
		configs.add(config);
		
		xioService.start();
		xioService.newServerAccepter(configs);
	}
}

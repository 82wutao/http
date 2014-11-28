package http.net.kernel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * 关于网络服务的简单配置。
 * @author wt
 *
 */
public class NetworkConfig {
	public SocketAddress address;
	public IOHandler handler;
	public int acceptBacklog;
	public IProtocol factory;
	public boolean tcpNoDelay;
	public int rcvBuffer;
	public int sendBuffer;
	
	public NetworkConfig(String host,int port,IOHandler handler,IProtocol protocol){
		if (host==null
				||host.equals("")) {
			address = new InetSocketAddress(port);
		}else{
			address = new InetSocketAddress(host,port);
		}
			
		
		this.handler = handler;
		
		acceptBacklog=1000;
		factory=protocol;
		tcpNoDelay = true;
		rcvBuffer = 5120;
		sendBuffer = 32768;
		
	}
	
}

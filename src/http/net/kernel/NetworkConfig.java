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
	public int acceptBacklog;
	public boolean tcpNoDelay;
	public int rcvBuffer;
	public int sendBuffer;
	public HttpProtocol protocol;
	
	public NetworkConfig(String host,int port,HttpProtocol protocol){
		if (host==null
				||host.equals("")) {
			address = new InetSocketAddress(port);
		}else{
			address = new InetSocketAddress(host,port);
		}
			
		
		acceptBacklog=1000;
		tcpNoDelay = true;
		rcvBuffer = 5120;
		sendBuffer = 32768;
		this.protocol=protocol;
	}
	
}

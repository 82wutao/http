package http.net.kernel;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * {@link Acceptor} 用来接受新的连接�? * @author wt
 *
 */
public class Acceptor {
	ServerSocketChannel ssc = null;

	public Acceptor() {


	}
	public void configure(NetworkConfig config) throws IOException{
		ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		ssc.bind(config.address);
	}

	void register(Selector selector) throws ClosedChannelException {
		ssc.register(selector, SelectionKey.OP_ACCEPT);
	}
	
	void close()throws IOException{
		ssc.close();
	}
}

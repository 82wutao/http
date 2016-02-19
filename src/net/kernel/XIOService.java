package net.kernel;

import http.HttpProccesser;
import http.api.ServerContext;
import io.XBuffer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketOption;
import java.net.SocketOptions;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.Pair;
import common.memories.Pool;
import common.memories.PoolableObject;
import common.memories.PoolableObjectFactory;

/***
 * 监听多个端口。每个端口配一个handler
 * @author wutao
 *
 */
public class XIOService implements Runnable {

	private List<XNetworkConfig> networkConfig = null;
	private List<ServerSocketChannel> serverChannel = null;
	private Map<ServerSocketChannel, XNetworkConfig> port_app=new HashMap<ServerSocketChannel, XNetworkConfig>();
	private Selector selector=null;
	
	private boolean running = false;
//	private Pool<XBuffer> bufferPool= new Pool<XBuffer>(1024, 2048, new PoolableObjectFactory<XBuffer>() {
//		@Override
//		public XBuffer newObject() {
//			return null;
//		}
//		public void freeObject(XBuffer t) {
//			
//		}
//	});
	
	private ServerContext serverContext=null;

	
	public XIOService(ServerContext serverContext) {
		this.serverContext=serverContext;
	}

	public void configure(List<XNetworkConfig> configs)
			throws IOException {
		
		networkConfig = configs;
		running = true;

	}

	public void startListen() throws IOException{
		selector = Selector.open();

		Thread service = new Thread(this);
		service.start();
	}

	public void stopListen() throws IOException {
		running = false;
		
		for ( ServerSocketChannel channel:serverChannel) {
			channel.close();
		}
		selector.close();
	}



	/**
	 * IOService
	 */
	public void run() {
		
		try {
			for (XNetworkConfig config :networkConfig) {
				ServerSocketChannel server = ServerSocketChannel.open();
				serverChannel.add(server);
				
				server.configureBlocking(false);
				server.bind(config.address);
				
				port_app.put(server, config);
				
				server.register(selector,SelectionKey.OP_ACCEPT);
			}
			
			List<Pair<SocketChannel, XNetworkConfig>> goclosing=new ArrayList<Pair<SocketChannel,XNetworkConfig>>();
			while (running) {
				int selected = selector.select(1000);//TODO auto update this value
				if (selected == 0) {
					//TODO  auto update this value
				}
				
				goclosing.clear();
				
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				for (Iterator<SelectionKey> keyIterator = selectedKeys.iterator();keyIterator.hasNext();) {
					SelectionKey key =keyIterator.next();
					keyIterator.remove();
					
					if (key.isAcceptable()) {
						ServerSocketChannel server =(ServerSocketChannel)key.channel();
						XNetworkConfig app=port_app.get(server);
						
						SocketChannel client= server.accept();
						accept(client,app);
						app.ioListener.opennedChannel(client);
					}
					
					SocketChannel client =(SocketChannel)key.channel();
					Pair<ByteBuffer, XNetworkConfig> clientAttachment=(Pair)key.attachment();
					if (key.isConnectable()) {
						client.register(selector, SelectionKey.OP_READ);
						clientAttachment.t.ioListener.connectedChannel(client);
					}
					else if (key.isReadable()) {
						ByteBuffer buffer = clientAttachment.k;
						buffer.clear();
						
						int read = client.read(buffer);
						
						do{
							if(read ==-1){
								goclosing.add(new Pair<SocketChannel, XNetworkConfig>(client, clientAttachment.t));
								break;
							}
							
							XBuffer xBuffer=new XBuffer(buffer.array(), 0, read);
							while(xBuffer.remain()>0){
								clientAttachment.t.ioListener.readable(client,xBuffer);
							}
						}while(false);						
					}
					else if (key.isWritable()) {
						
					}
					
					for(Pair<SocketChannel, XNetworkConfig> pair:goclosing){
						pair.k.close();
						pair.t.ioListener.closedChannel(pair.k);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	private void accept(SocketChannel client,XNetworkConfig config) throws IOException {
		client.configureBlocking(false);
		client.setOption(StandardSocketOptions.TCP_NODELAY,config.tcpNoDelay);
		client.setOption(StandardSocketOptions.SO_RCVBUF, config.rcvBuffer);
		client.setOption(StandardSocketOptions.SO_SNDBUF, config.sendBuffer);
		client.register(selector, SelectionKey.OP_READ,new Pair<ByteBuffer, XNetworkConfig>(ByteBuffer.allocate(config.rcvBuffer), config));
	}
	public void newConnection(XNetworkConfig config)throws IOException {
		SocketChannel socketChannel=SocketChannel.open();
		socketChannel.configureBlocking(false);
		socketChannel.setOption(StandardSocketOptions.TCP_NODELAY,config.tcpNoDelay);
		socketChannel.setOption(StandardSocketOptions.SO_RCVBUF, config.rcvBuffer);
		socketChannel.setOption(StandardSocketOptions.SO_SNDBUF, config.sendBuffer);
		socketChannel.connect(config.address);
		
		socketChannel.register(selector, SelectionKey.OP_CONNECT,new Pair<ByteBuffer, XNetworkConfig>(ByteBuffer.allocate(config.rcvBuffer), config));
		selector.wakeup();
	}

}

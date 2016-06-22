package net.kernel;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import common.Pair;
import net.ServerContext;

/***
 * 监听多个端口。每个端口配一个handler
 * @author wutao
 *
 */
public class XIOService<Request> implements Runnable,ChannelInterestEvent<Request> {

	private List<XNetworkConfig<Request>> networkConfig = null;
	private List<ServerSocketChannel> serverChannel = null;
	private Map<ServerSocketChannel, XNetworkConfig<Request>> port_app=new HashMap<ServerSocketChannel, XNetworkConfig<Request>>();
	
	private Selector selector=null;
	private int wakeupLock = 0;
	
	private boolean running = false;
	
	private ServerContext serverContext=null;

	
	public XIOService(ServerContext serverContext) {
		this.serverContext=serverContext;
	}

	public void stopListen() throws IOException {
		running = false;
		
		if (serverChannel!=null) {
			for ( ServerSocketChannel channel:serverChannel) {
				channel.close();
			}
		}
		selector.close();
	}

	/**
	 * IOService
	 */
	public void run() {
		try {
			List<Pair<SocketChannel, XNetworkConfig<Request>>> goclosing=new ArrayList<Pair<SocketChannel,XNetworkConfig<Request>>>();
			while (running) {
				
				int selected = 0;
				synchronized (selector) {
					wakeupLock=1;
					selected = selector.select(1000);
				}
				wakeupLock = 0;
				
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
						XNetworkConfig<Request> app=port_app.get(server);
						
						SocketChannel client= server.accept();
						NetSession<Request> session=accept(client,app);
						app.ioListener.opennedChannel(session);
						continue;
					}
					
					SocketChannel client =(SocketChannel)key.channel();
					Pair<NetSession<Request>, XNetworkConfig<Request>> clientAttachment=(Pair)key.attachment();
					if (key.isConnectable()) {
						client.register(selector, SelectionKey.OP_READ);
						NetSession<Request> session = clientAttachment.k;
						clientAttachment.t.ioListener.connectedChannel(session);
					}
					else if (key.isReadable()) {
						NetSession<Request> session = clientAttachment.k;
						int read = session.readBytesFromChanel();
						
						do{
							if(read ==-1){
								closeNetSession(session);
								clientAttachment.t.ioListener.closedChannel(session);
								break;
							}
							if (read==0) {
								break;
							}
							
							//TODO async handler
							for(int readable =session.readableBufferRemaining();readable>0;readable =session.readableBufferRemaining()){
								Request request =clientAttachment.t.ioListener.readable(session,readable);
								if (request == null){break;}
								
								//TODO async handler
								clientAttachment.t.appHandler.handle(session, request);
							}
						}while(false);						
					}
					else if (key.isWritable()) {
						NetSession<Request> session = clientAttachment.k;
						
						do{
							int write = session.writeBytesToChanel();
							if (write==0) {
								break;
							}							
							if(write ==-1){
								closeNetSession(session);
								clientAttachment.t.ioListener.closedChannel(session);
								break;
							}
							clientAttachment.t.ioListener.writed(session,write);
							
						}while(false);	
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	private NetSession<Request> accept(SocketChannel client,XNetworkConfig<Request> config) throws IOException {
		client.configureBlocking(false);
		client.setOption(StandardSocketOptions.TCP_NODELAY,config.tcpNoDelay);
		client.setOption(StandardSocketOptions.SO_RCVBUF, config.rcvBuffer);
		client.setOption(StandardSocketOptions.SO_SNDBUF, config.sendBuffer);
		
		NetSession<Request> session = config.newNetworkSession(client);
		client.register(selector, SelectionKey.OP_READ,new Pair<NetSession<Request>, XNetworkConfig<Request>>(session, config));
		return session;
	}
	public void newConnection(XNetworkConfig<Request> config)throws IOException {
		SocketChannel socketChannel=SocketChannel.open();
		socketChannel.configureBlocking(false);
		socketChannel.setOption(StandardSocketOptions.TCP_NODELAY,config.tcpNoDelay);
		socketChannel.setOption(StandardSocketOptions.SO_RCVBUF, config.rcvBuffer);
		socketChannel.setOption(StandardSocketOptions.SO_SNDBUF, config.sendBuffer);
		socketChannel.connect(config.address);
		
		NetSession<Request> session = config.newNetworkSession(socketChannel);
		socketChannel.register(selector, SelectionKey.OP_CONNECT,new Pair<NetSession<Request>, XNetworkConfig<Request>>(session, config));
		wakeupSelector();
	}
	public void newServerAccepter(List<XNetworkConfig<Request>> configs) throws IOException{
		networkConfig = configs;
		
				
		for (XNetworkConfig<Request> config :networkConfig) {
			ServerSocketChannel server = ServerSocketChannel.open();
			serverChannel.add(server);
			
			server.configureBlocking(false);
			server.bind(config.address);
			
			port_app.put(server, config);
			
			server.register(selector,SelectionKey.OP_ACCEPT);
		}
		wakeupSelector();
	}
	public void start() throws IOException{
		serverChannel = new ArrayList<ServerSocketChannel>();
		running = true;
		selector = Selector.open();

		Thread service = new Thread(this);
		service.start();
	}
	@Override
	public void changeInterestEvent(NetSession<Request> session, int event) throws ClosedChannelException {
		session.getChannel().register(selector, event,
				new Pair<NetSession<Request>, XNetworkConfig<Request>>(session, session.config));
		wakeupSelector();
	}
	protected void wakeupSelector() {
		synchronized (selector) {
			if (wakeupLock==0) {
				return ;
			}
			selector.wakeup();
		}		
	}
	protected void closeNetSession(NetSession<Request> session) throws IOException{
		session.channel.register(selector, 0, null);
		session.close();
	}
}

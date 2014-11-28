package http.net.kernel;

import http.concurrents.AsynchronizedCondition;
import http.memories.Pool;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 网络服务的核心实现，基于JDK NIO特性。 维护一个session的集合。有新连接即加入，连接关闭即移除。
 * 有IO事件发生，记录在相应的数组中，最后批量处理。 根据异步参数，最后的处理可能为同步执行，也有可能为线程池内并发执行。
 * 
 * @author wt
 * 
 */
public class IOService implements Runnable {
	private Acceptor acceptor;

	NetworkConfig networkConfig = null;
	Selector selector = null;
	IOHandler handler = null;

	private boolean running = false;

	Set<NetworkSession> networkSessions = new HashSet<NetworkSession>();

	private List<NetworkSession> newSessions = new ArrayList<NetworkSession>();
	private List<NetworkSession> messages_session = new ArrayList<NetworkSession>();
	private List<Object> messages = new ArrayList<Object>();
	private List<NetworkSession> closes = new ArrayList<NetworkSession>();
	private List<NetworkSession> connected = new ArrayList<NetworkSession>();

	private boolean asyncHandle = false;
	private ExecutorService executor = null;

	private Pool<AsynchronizedTask> eventHandlePool = null;
	
	public IOService() {
	}

	public void configure(NetworkConfig config, boolean asyncHandle)
			throws IOException {
		selector = Selector.open();
		networkConfig = config;
		handler = config.handler;
		running = true;

		this.asyncHandle = asyncHandle;
		if (this.asyncHandle) {
			executor = Executors.newCachedThreadPool();
			eventHandlePool = new Pool<AsynchronizedTask>(256, 0,
					new AsynchronizedTaskFactory());
			
		}
	}

	public void startListen() {

		Thread service = new Thread(this);
		service.start();
	}

	public void stopListen() {
		running = true;
		if (asyncHandle) {
			executor.shutdown();
		}
	}

	public void addAcceptor(Acceptor acceptor) throws ClosedChannelException {
		acceptor.register(selector);
		this.acceptor = acceptor;
	}

	public void addConnector(NetworkSession connector)
			throws ClosedChannelException {
		SelectionKey channelKey=connector.connetion.register(selector, SelectionKey.OP_CONNECT,
				connector);
		connector.selectionKey=channelKey;
	}

	public void onRcvMsg(NetworkSession session, Object requestMsg) {
		if (asyncHandle) {
			session.rcvRequestMsg(requestMsg);
			return;
		}
		messages.add(requestMsg);
		messages_session.add(session);
	}

	public boolean onSendingMsg(NetworkSession session, ByteBuffer data)
			throws IOException {
		int leng = data.limit();
		int write = session.connetion.write(data);
		if (leng == write) {
			return true;
		} else {

			int limit = session.sendBuffer.limit();
			if (limit == 0) {
				limit = session.sendBuffer.capacity();
				session.sendBuffer.limit(limit);
			}
			int position = session.sendBuffer.position();
			int free = limit - position;
			if ((leng - write) <= free) {
				byte[] _data = data.array();
				session.sendBuffer.put(_data, write, leng - write);
			} else {
				byte[] newBlock = new byte[session.sendBuffer.capacity() * 2];
				System.arraycopy(session.sendBuffer, 0, newBlock, 0, position);
				session.sendBuffer = ByteBuffer.wrap(newBlock);
				session.sendBuffer.position(position);

				byte[] _data = data.array();
				session.sendBuffer.put(_data, write, leng - write);
			}
			return false;
		}
	}

	/**
	 * IOService
	 */
	public void run() {
		while (running) {
			// check session if closed
			for (Iterator<NetworkSession> iterator = networkSessions.iterator(); iterator
					.hasNext();) {
				NetworkSession session = iterator.next();
				if (!session.isClosedNetworkSession()) {
					continue;
				}

				try {
					session.connetion.shutdownInput();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					session.connetion.register(this.selector,SelectionKey.OP_WRITE,session);
				} catch (ClosedChannelException e) {
					e.printStackTrace();
				}
			}

			try {
				selector.select();
			} catch (IOException e) {
				e.printStackTrace();
				running = false;
				break;
			}

			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> keyIterator = keys.iterator();
			while (keyIterator.hasNext()) {
				SelectionKey key = keyIterator.next();
				keyIterator.remove();
				if (!key.isValid()) {
					continue;
				}
				if (key.isAcceptable()) {
					try {
						accept(key);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (key.isReadable()) {
					try {
						read(key);
					} catch (IOException e) {
						closes.add((NetworkSession) key.attachment());
						networkSessions.remove((NetworkSession) key.attachment());
					}
				} else if (key.isWritable()) {
					try {
						write(key);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (key.isConnectable()) {
					NetworkSession session = (NetworkSession) key.attachment();
					try {
						session.connetion.finishConnect();
						session.connetion.register(selector,
								SelectionKey.OP_READ, session);
						connected.add(session);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			for (Iterator<NetworkSession> iterator= networkSessions.iterator();iterator.hasNext();) {
				NetworkSession session= iterator.next();
				try {
					if (!session.sendingBuffers.isEmpty()
							|| session.responseMsgsSize() > 0) {
						if (session.isClosedNetworkSession()) {
							session.connetion.register(this.selector,SelectionKey.OP_WRITE,
									session);
						}else {
							session.connetion.register(this.selector,
									SelectionKey.OP_READ | SelectionKey.OP_WRITE,
									session);
						}
					} else {
						if (!session.isClosedNetworkSession()) {
							session.connetion.register(this.selector,
								SelectionKey.OP_READ, session);
						}else {
							closes.add(session);
							iterator.remove();
						}
					}
				} catch (ClosedChannelException e) {
					e.printStackTrace();
				}
			}
			
			if (asyncHandle) {
				asynchronizedHandleEvents();
			} else {
				synchronizedHandleEvents();
			}
		}

		try {
			selector.close();
			acceptor.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (NetworkSession session : networkSessions) {
			handler.sessionClose(session);
			networkSessions.remove(session);
			session.popResponseMsgs();
			session.connetion = null;
		}
		networkSessions.clear();
		if (eventHandlePool != null) {
			eventHandlePool.destory();
		}
	}

	private void read(SelectionKey key) throws IOException {
		NetworkSession session = (NetworkSession) key.attachment();
		
		int readed = networkConfig.factory.decode(this, session);
		if (readed<=0) {
			closes.add(session);
			networkSessions.remove(session);
		}
		while(readed > 0){
			readed = networkConfig.factory.decode(this, session);
		}
		
	}

	private void write(SelectionKey key) throws IOException {
		SocketChannel ssc = (SocketChannel) key.channel();
		NetworkSession session = (NetworkSession) key.attachment();
		int diff = session.write(ssc);
		if (diff == -1) {
			closes.add(session);
			return;
		}
	}

	private void accept(SelectionKey key) throws IOException {
		ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
		SocketChannel clientChannel = ssc.accept();
		clientChannel.configureBlocking(false);

		NetworkSession session = new NetworkSession(networkConfig.rcvBuffer,
				networkConfig.sendBuffer);
		session.setConnection(clientChannel);
		clientChannel.socket().setTcpNoDelay(networkConfig.tcpNoDelay);
		newSessions.add(session);
		SelectionKey channelKey =clientChannel.register(selector, SelectionKey.OP_READ, session);
		session.selectionKey=channelKey;
	}

	private void asynchronizedHandleEvents() {
		List<AsynchronizedTask> task_Ref = new ArrayList<AsynchronizedTask>();

		// newSession event
		int newSessionCount = newSessions.size();
		AsynchronizedCondition newSessionCondition = new AsynchronizedCondition();
		for (NetworkSession session : newSessions) {
			AsynchronizedTask task = eventHandlePool.allocObject();
			task_Ref.add(task);
			task.setData(session, AsynchronizedTask.Task_NewSession, this,
					newSessionCondition);
			executor.execute(task);
		}
		newSessions.clear();
		newSessionCondition.waitFor(newSessionCount);
		for (AsynchronizedTask task : task_Ref) {
			eventHandlePool.freeObject(task);
		}
		task_Ref.clear();

		// rcvMsg event
		int rcvMsgCount = networkSessions.size();
		AsynchronizedCondition rcvMsgCondition = new AsynchronizedCondition();
		for (NetworkSession session : networkSessions) {
			AsynchronizedTask task = eventHandlePool.allocObject();
			task_Ref.add(task);
			task.setData(session, AsynchronizedTask.Task_ReceiveMsg, this,
					rcvMsgCondition);
			executor.execute(task);
		}
		rcvMsgCondition.waitFor(rcvMsgCount);
		for (AsynchronizedTask task : task_Ref) {
			eventHandlePool.freeObject(task);
		}
		task_Ref.clear();

		// session close event
		int closeMsgCount = closes.size();
		AsynchronizedCondition closeMsgCondition = new AsynchronizedCondition();
		for (NetworkSession session : closes) {
			AsynchronizedTask task = eventHandlePool.allocObject();
			task_Ref.add(task);
			task.setData(session, AsynchronizedTask.Task_CloseSession, this,
					closeMsgCondition);
			executor.execute(task);
		}
		closes.clear();
		closeMsgCondition.waitFor(closeMsgCount);
		for (AsynchronizedTask task : task_Ref) {
			eventHandlePool.freeObject(task);
		}
		task_Ref.clear();

		// connected Session event
		int connectedCount = connected.size();
		AsynchronizedCondition connectedCondition = new AsynchronizedCondition();
		for (NetworkSession session : connected) {
			AsynchronizedTask task = eventHandlePool.allocObject();
			task_Ref.add(task);
			task.setData(session, AsynchronizedTask.Task_Connected, this,
					connectedCondition);
			executor.execute(task);
		}
		connected.clear();
		connectedCondition.waitFor(connectedCount);
		for (AsynchronizedTask task : task_Ref) {
			eventHandlePool.freeObject(task);
		}
		task_Ref.clear();

		// send those msg that is in session's send-buffer
		int sendMsgCount = 0;
		AsynchronizedCondition sendMsgCondition = new AsynchronizedCondition();
		for (NetworkSession session : networkSessions) {
			if (session.sendingBuffers.size() > 0) {
				continue;
			}
			sendMsgCount++;
			AsynchronizedTask task = eventHandlePool.allocObject();
			task_Ref.add(task);
			task.setData(session, AsynchronizedTask.Task_WriteMsg, this,
					sendMsgCondition);
			executor.execute(task);
		}
		sendMsgCondition.waitFor(sendMsgCount);
		for (AsynchronizedTask task : task_Ref) {
			eventHandlePool.freeObject(task);
		}
		task_Ref.clear();
	}

	private void synchronizedHandleEvents() {
		// newSession event
		for (NetworkSession session : newSessions) {
			handler.newSession(session);
			networkSessions.add(session);
		}
		newSessions.clear();

		// rcvMsg event
		for (int i = 0; i < messages.size(); i++) {
			handler.recievMessage(messages_session.get(i), messages.get(i));
		}
		messages.clear();
		messages_session.clear();

		// session close event
		for (NetworkSession session : closes) {
			
			try {
				session.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			session.selectionKey.cancel();
			
			handler.sessionClose(session);
			networkSessions.remove(session);
			session.popResponseMsgs();
			session.connetion = null;
		}
		closes.clear();

		// session connected
		for (NetworkSession session : connected) {
			handler.connected(session);
			networkSessions.add(session);
		}
		connected.clear();

		// send those msg that is in session's send-buffer
		for (NetworkSession session : networkSessions) {
			if (session.sendingBuffers.size() > 0) {
				continue;
			}
			List<XBuffer> msgs = session.popResponseMsgs();
			for (Iterator<XBuffer> it = msgs.iterator(); it.hasNext();) {
				XBuffer msg = it.next();
				it.remove();
				try {
					ByteBuffer buffer = this.networkConfig.factory.encode(this,
							session, msg);
					boolean goon = this.onSendingMsg(session, buffer);
					if (goon) {
						continue;
					} else {
						session.sendingBuffers.add(buffer);
						break;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			for (Iterator<XBuffer> it = msgs.iterator(); it.hasNext();) {
				XBuffer msg = it.next();
				try {
					ByteBuffer buffer = this.networkConfig.factory.encode(this,
							session, msg);
					session.sendingBuffers.add(buffer);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

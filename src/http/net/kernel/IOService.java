package http.net.kernel;


import http.HttpServerContext;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
	private ServerSocket serverSocket=null;

	NetworkConfig networkConfig = null;

	private boolean running = false;

	private ExecutorService executor = null;
	private HttpServerContext serverContext=null;

	
	public IOService(HttpServerContext serverContext) {
		this.serverContext=serverContext;
	}

	public void configure(NetworkConfig config)
			throws IOException {
		serverSocket = new ServerSocket();
		
		
		networkConfig = config;
		running = true;

		executor = Executors.newCachedThreadPool();
	}

	public void startListen() {

		Thread service = new Thread(this);
		service.start();
	}

	public void stopListen() throws IOException {
		running = true;
		executor.shutdown();
		serverSocket.close();
	}



	/**
	 * IOService
	 */
	public void run() {
		try {
			serverSocket.bind(networkConfig.address);
			
			while (running) {
				Socket clientSocket= serverSocket.accept();
				accept(clientSocket);
			}

			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}


	private void accept(Socket client) throws IOException {
		HttpProccesser proccesser=new HttpProccesser(serverContext,client,networkConfig.protocol,4096);
		executor.execute(proccesser);
	}

}

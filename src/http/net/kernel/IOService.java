package http.net.kernel;


import http.HttpProccesser;
import http.api.ServerContext;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wt
 * 
 */
public class IOService implements Runnable {
	private ServerSocket serverSocket=null;

	NetworkConfig networkConfig = null;

	private boolean running = false;

	private ExecutorService executor = null;
	private ServerContext serverContext=null;

	
	public IOService(ServerContext serverContext) {
		this.serverContext=serverContext;
	}

	public void configure(NetworkConfig config)
			throws IOException {
		serverSocket = new ServerSocket();
		
		
		networkConfig = config;
		running = true;

		executor = Executors.newFixedThreadPool(serverContext.getWorkerThreads());
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

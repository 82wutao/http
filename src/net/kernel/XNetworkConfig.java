package net.kernel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import net.Handler;
import net.IOListener;

public class XNetworkConfig {
	public SocketAddress address;
	public int acceptBacklog;
	public boolean tcpNoDelay;
	public int rcvBuffer;
	public int sendBuffer;

	public IOListener ioListener;

	public Handler appHandler;

	public XNetworkConfig(String host, int port, int backlog, boolean noDelay) {
		if (host == null || host.equals("")) {
			address = new InetSocketAddress(port);
		} else {
			address = new InetSocketAddress(host, port);
		}

		acceptBacklog = backlog;
		tcpNoDelay = noDelay;

		rcvBuffer = 5120;
		sendBuffer = 32768;
	}

	public void setBufferSize(int rcvSizer, int sendSizer) {
		rcvBuffer = rcvSizer;
		sendBuffer = sendSizer;
	}

	public void setupApplication(IOListener listener, Handler app) {
		ioListener = listener;
		appHandler = app;
	}
}

package net.kernel;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;

public interface ChannelInterestEvent<Request> {
	public static final int Accept =SelectionKey.OP_ACCEPT;
	public static final int Connect =SelectionKey.OP_CONNECT;
	public static final int Read =SelectionKey.OP_READ;
	public static final int Write =SelectionKey.OP_WRITE;
	
	public void changeInterestEvent(NetSession<Request> channel, int event)throws ClosedChannelException;
}

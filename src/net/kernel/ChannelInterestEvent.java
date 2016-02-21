package net.kernel;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;

public interface ChannelInterestEvent<Request> {
	public int Accept =SelectionKey.OP_ACCEPT;
	public int Connect =SelectionKey.OP_CONNECT;
	public int Read =SelectionKey.OP_READ;
	public int Write =SelectionKey.OP_WRITE;
	
	public void changeInterestEvent(NetSession<Request> channel, int event)throws ClosedChannelException;
}

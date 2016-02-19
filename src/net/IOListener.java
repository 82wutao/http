package net;

import io.XBuffer;

import java.nio.channels.SocketChannel;

public interface IOListener {
	public void opennedChannel(SocketChannel channel);
	public void connectedChannel(SocketChannel channel);
	public void readable(SocketChannel channel,XBuffer buffer);
	public void writeable(SocketChannel channel);
	public void closedChannel(SocketChannel channel);
}

package net.kernel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.Queue;

import common.io.XBuffer;

public class NetSession<Request> {
	static final int Status_Connecting=1;
	static final int Status_Closed=2;

	protected boolean quick;
	protected SocketChannel channel;
	protected ByteBuffer readableBuffer;
	protected ByteBuffer sendableBuffer;
	protected Queue<ByteBuffer> sendQueue = null;
	
	protected XNetworkConfig<Request> config=null;
	protected ChannelInterestEvent<Request> channelInterestEvent;
	
	protected int connection_status=Status_Connecting;
	
	public NetSession(ChannelInterestEvent<Request> channelInterestEvent,XNetworkConfig<Request> networkConfig,SocketChannel socketChannel
			,boolean quick
			,int readBufferSize,int sendBufferSize) {
		this.channelInterestEvent = channelInterestEvent;
		
		this.config = networkConfig;
		channel = socketChannel;
		this.quick = quick;
		readableBuffer =  ByteBuffer.allocate(readBufferSize);
		sendableBuffer = ByteBuffer.allocate(sendBufferSize);
		sendQueue=new ArrayDeque<ByteBuffer>();
	}
	
	public SocketChannel getChannel() {
		return channel;
	}	
	public boolean isQuick() {
		return quick;
	}
	
	public void write(XBuffer msg) throws IOException{
		if (connection_status == Status_Closed) {
			throw new IOException("connect is closed");
		}
		
		byte[] bytes = msg.getData();
		int off = msg.getPosition();
		int length = msg.getLimit() - off;
		write(bytes, off, length);
		msg.setPosition(off+length);
	}
	public void write(String msg,Charset charset) throws IOException{
		if (connection_status == Status_Closed) {
			throw new IOException("connect is closed");
		}
		byte[] bytes = msg.getBytes(charset);
		write(bytes,0,bytes.length);
	}
	public void write(byte[] data,int offset,int length) throws IOException{
		if (connection_status == Status_Closed) {
			throw new IOException("connect is closed");
		}
		
		if (quick) {
			ByteBuffer buffer = ByteBuffer.allocate(length);
			buffer.put(data,offset,length);
			buffer.flip();
			
			synchronized (sendQueue) {
				sendQueue.add(buffer);
			}
			
			channelInterestEvent.changeInterestEvent(this, 
					ChannelInterestEvent.Read|ChannelInterestEvent.Write);
		}else {
			if (sendableBuffer.remaining()<length) {
				synchronized (sendQueue) {
					sendableBuffer.flip();
					sendQueue.add(sendableBuffer);
				}
				
				sendableBuffer =  ByteBuffer.allocate(config.sendBuffer);
			}
			sendableBuffer.put(data,offset,length);
		}
	}
	public void write(byte v) throws IOException{
		if (connection_status == Status_Closed) {
			throw new IOException("connect is closed");
		}
		
		if (quick) {
			ByteBuffer buffer = ByteBuffer.allocate(1);
			buffer.put(v);
			buffer.flip();
			synchronized (sendQueue) {
				sendQueue.add(buffer);
			}
			channelInterestEvent.changeInterestEvent(this, 
					ChannelInterestEvent.Read|ChannelInterestEvent.Write);
		}else {
			if (sendableBuffer.remaining()<1) {
				synchronized (sendQueue) {
					sendableBuffer.flip();
					sendQueue.add(sendableBuffer);
				}
				
				sendableBuffer =  ByteBuffer.allocate(config.sendBuffer);
			}
			sendableBuffer.put(v);
		}
	}
	int writeBytesToChanel() throws IOException{
		if (connection_status == Status_Closed) {
			throw new IOException("connect is closed");
		}
		ByteBuffer img= null;
		synchronized (sendQueue) {
			if (sendQueue.isEmpty()) {
				channelInterestEvent.changeInterestEvent(this, 
						ChannelInterestEvent.Read);
				return 0;
			}
			img=sendQueue.peek();
		}
		
		int length = img.remaining();
		int write = channel.write(img);
		
		if(write == length){
			synchronized (sendQueue) {
				sendQueue.poll();
			}
		}
		return write;
	}
	public void flush() throws IOException{
		if (connection_status == Status_Closed) {
			throw new IOException("connect is closed");
		}
		
		synchronized (sendQueue) {
			sendableBuffer.flip();
			sendQueue.add(sendableBuffer);
		}
		sendableBuffer =  ByteBuffer.allocate(config.sendBuffer);
		
		channelInterestEvent.changeInterestEvent(this, 
				ChannelInterestEvent.Read|ChannelInterestEvent.Write);
	}
///////////////////////////////////////////////////////////////////////////////////	
	public int readableBufferRemaining(){
		return readableBuffer.remaining();
	}
	int readBytesFromChanel() throws IOException{
		if (connection_status == Status_Closed) {
			throw new IOException("connect is closed");
		}
//		readableBuffer.rewind();//position comebing 0;for read again
		if (readableBuffer.remaining() != readableBuffer.capacity()) {			
			readableBuffer.compact();//take bytes unreaded to the begin,position is after bytes ,limit is capacity ,for write 2 self 
		}
		int readed = channel.read(readableBuffer);
		readableBuffer.flip();//for read from self
		return readed;
	}
	
	public String readLine(byte[] end) throws IOException{
		StringBuilder builder = new StringBuilder();
		int compareIndex =0;
		while (true){
			byte c = read();
			if (c==-1) {
				if (builder.length()==0) {
					return null;
				}
				return builder.toString();
			}
			
			if (end[compareIndex] == c) {
				compareIndex++;
				if (compareIndex==end.length) {
					break;
				}
			}else {
				compareIndex=0;
				if (end[compareIndex] == c) {
					compareIndex++;
				}
			}
			builder.append(c);
		}
		return builder.toString();
	}
	
	private void skipRead(int skip){
		int position = readableBuffer.position();
		readableBuffer.position(position + skip);
	}
	public void skipBytes(int skip) throws IOException{
		int remaining = readableBufferRemaining();
		int skiped = 0;
		while (remaining + skiped < skip ) {
			skipRead(remaining);
			skiped = skiped + remaining;
			
			readBytesFromChanel();
			remaining = readableBufferRemaining();
		}
		skipRead(skip-skiped);
		
	}
	
	public void backRead(int back){
		int position = readableBuffer.position();
		readableBuffer.position(position - back);
	}
	public int read(XBuffer dest) throws IOException{
		if (!readableBuffer.hasRemaining()) {
			int readed = readBytesFromChanel();
			if (readed == -1) {
				return -1;
			}
		}
		
		int remaining =readableBuffer.remaining();
		byte[] bytes = dest.getData();
		int off = dest.getPosition();
		int length = dest.getLimit() - off;
		
		if (remaining>length) {
			readableBuffer.get(bytes,off,length);
			dest.setPosition(off+length);
			return length;
		}else {
			readableBuffer.get(bytes,off,remaining);
			dest.setPosition(off+remaining);
			return remaining;
		}	
	}

	public int read(byte[] dest,int offset,int length) throws IOException{
		if (!readableBuffer.hasRemaining()) {
			int readed = readBytesFromChanel();
			if (readed == -1) {
				return -1;
			}
		}
		
		int remaining = readableBuffer.remaining();
		if (remaining < length) {
			readableBuffer.get(dest,offset,remaining);
			return remaining;
		}
		readableBuffer.get(dest,offset,length);
		return length;
	}
	public byte read() throws IOException{
		if (!readableBuffer.hasRemaining()) {
			int readed = readBytesFromChanel();
			if (readed == -1
					||readed==0) {
				return -1;
			}
		}
		return readableBuffer.get();
	}
	////////////////////////////////////////////////////////////////////////////////
	public void close() throws IOException{
		connection_status = Status_Closed;
		channel.close();
	}
}

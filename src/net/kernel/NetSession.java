package net.kernel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.Queue;

import common.io.XBuffer;

public class NetSession<Request> {
	protected boolean quick;
	protected SocketChannel channel;
	protected ByteBuffer readableBuffer;
	protected Queue<ByteBuffer> sendQueue = null;
	
	protected XNetworkConfig<Request> config=null;
	protected ChannelInterestEvent<Request> channelInterestEvent;
	
	protected int connection_status=Status_Connecting;//TODO check it before readwrite
	
	public NetSession(ChannelInterestEvent<Request> channelInterestEvent,XNetworkConfig<Request> networkConfig,SocketChannel socketChannel
			,boolean quick
			,int readBufferSize,int sendBufferSize) {
		this.channelInterestEvent = channelInterestEvent;
		
		this.config = networkConfig;
		channel = socketChannel;
		this.quick = quick;
		readableBuffer =  ByteBuffer.allocate(readBufferSize);
		sendQueue=new ArrayDeque<ByteBuffer>();
	}
	
	public SocketChannel getChannel() {
		return channel;
	}	
	public boolean isQuick() {
		return quick;
	}
	
	static final int Status_Connecting=1;
	static final int Status_Closed=2;
	void setConnectionStatus(int statusConst){
		connection_status=statusConst;
	}
	public void write(XBuffer msg) throws ClosedChannelException{
		byte[] bytes = msg.getData();
		int off = msg.getPosition();
		int length = msg.getLimit() - off;
		
		ByteBuffer buffer = ByteBuffer.allocate(length);
		buffer.put(bytes,off,length);
		buffer.flip();
		msg.setPosition(off+length);
		
		synchronized (sendQueue) {
			sendQueue.add(buffer);
		}
		if (quick) {
			flush();
		}		
	}
	public void write(String msg,Charset charset) throws ClosedChannelException{
		byte[] bytes = msg.getBytes(charset);
		
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		buffer.put(bytes,0,bytes.length);
		buffer.flip();
		
		synchronized (sendQueue) {
			sendQueue.add(buffer);
		}
		if (quick) {
			flush();
		}		
	}
	public void write(byte[] data,int offset,int length) throws ClosedChannelException{
		
		ByteBuffer buffer = ByteBuffer.allocate(length);
		buffer.put(data,0,length);
		buffer.flip();
		
		synchronized (sendQueue) {
			sendQueue.add(buffer);
		}
		if (quick) {
			flush();
		}
	}
	public void write(byte v) throws ClosedChannelException{
		
		ByteBuffer buffer = ByteBuffer.allocate(1);
		buffer.put(v);
		buffer.flip();
		
		synchronized (sendQueue) {
			sendQueue.add(buffer);
		}
		if (quick) {
			flush();
		}
	}
	int writeBytesToChanel() throws IOException{
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
//		sendableBuffer.flip();//for read from self
//		if (!sendableBuffer.hasRemaining()){
//			return 0;
//		}
//		int write =channel.write(sendableBuffer);
//		sendableBuffer.compact();
//		return write;
	}
	public void flush() throws ClosedChannelException{
		channelInterestEvent.changeInterestEvent(this, 
				ChannelInterestEvent.Read|ChannelInterestEvent.Write);
	}
///////////////////////////////////////////////////////////////////////////////////	
	public int readableBufferRemaining(){
		return readableBuffer.remaining();
	}
	public int readBytesFromChanel() throws IOException{
//		readableBuffer.rewind();//position comebing 0;for read again
		if (readableBuffer.position() ==0
				&&readableBuffer.limit()==readableBuffer.capacity()) {
			
		}else {			
			readableBuffer.compact();//take bytes unreaded to the begin,position is after bytes ,limit is capacity ,for write 2 self 
		}
		int readed = channel.read(readableBuffer);
		readableBuffer.flip();//for read from self
		return readed;
	}
	public void skipRead(int skip){
		int position = readableBuffer.position();
		readableBuffer.position(position + skip);
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

	public int read(byte[] dest,int offset) throws IOException{
		if (!readableBuffer.hasRemaining()) {
			int readed = readBytesFromChanel();
			if (readed == -1) {
				return -1;
			}
		}
		
		int remaining = readableBuffer.remaining();
		int length = dest.length - offset;
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
			if (readed == -1) {
				return -1;
			}
		}
		return readableBuffer.get();
	}
	////////////////////////////////////////////////////////////////////////////////
	public void close() throws IOException{
		channel.close();
	}
}

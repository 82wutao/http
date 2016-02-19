package net;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import io.XBuffer;

public class NetSession {
	protected boolean quick;
	
	protected XBuffer readableBuffer;
	protected XBuffer sendableBuffer;
	
	protected NetSession(boolean quick) {
		this.quick = quick;
	}
public void setReadableBuffer(XBuffer readableBuffer) {
	this.readableBuffer = readableBuffer;
}
public XBuffer getReadableBuffer() {
	return readableBuffer;
}
public void setSendableBuffer(XBuffer sendableBuffer) {
	this.sendableBuffer = sendableBuffer;
}
public XBuffer getSendableBuffer() {
	return sendableBuffer;
}
public boolean isQuick() {
	return quick;
}
	
	public void write(XBuffer msg){}
	public void write(String msg,Charset charset){}
	public void write(byte[] data,int offset,int length){}
	
	public int read(XBuffer dest){return 0;}
	public String read(Charset charset){return null;}
	public int read(byte[] dest,int offset){return 0;}

	public void close(){}
}

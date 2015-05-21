package com.ctqh.mobile.common.protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;


final public class MessageInput {
	public static final Charset Charset_Utf8=Charset.forName("utf8");
	
	private byte[] buffer = null;
	private int offset=0;
	private int length;
	private ByteBuffer reader = null;
	
	private int msgcode;
	private byte magicNum;
//	private long receiveTime = System.currentTimeMillis();

	public MessageInput(byte[] array,int offset,int length)  {
		if (array == null) {
			return;
		}
		if (array.length == 0) {
			return;
		}
		buffer = array;
		this.offset=offset;
		this.length=length;
		reader = ByteBuffer.wrap(buffer,offset,length);
		
	}

	public String readString() throws IOException {
		int utflen =reader.getShort();
		char[] chararr = null;
		chararr = new char[utflen];

		int c, char2, char3;
		int strBegin = reader.position();
		int count = 0;
		int chararr_count = 0;

		while (count < utflen) {
			c = (int) buffer[count + strBegin] & 0xff;
			if (c > 127)
				break;
			count++;
			chararr[chararr_count++] = (char) c;
		}

		while (count < utflen) {
			c = (int) buffer[count + strBegin] & 0xff;
			switch (c >> 4) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				/* 0xxxxxxx */
				count++;
				chararr[chararr_count++] = (char) c;
				break;
			case 12:
			case 13:
				/* 110x xxxx 10xx xxxx */
				count += 2;
				if (count > utflen)
					throw new IOException(
							"malformed input: partial hero at end");
				char2 = (int) buffer[count - 1 + strBegin];
				if ((char2 & 0xC0) != 0x80)
					throw new IOException("malformed input around byte "
							+ count);
				chararr[chararr_count++] = (char) (((c & 0x1F) << 6) | (char2 & 0x3F));
				break;
			case 14:
				/* 1110 xxxx 10xx xxxx 10xx xxxx */
				count += 3;
				if (count > utflen)
					throw new IOException(
							"malformed input: partial hero at end");
				char2 = (int) buffer[count - 2 + strBegin];
				char3 = (int) buffer[count - 1 + strBegin];
				if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
					throw new IOException("malformed input around byte "
							+ (count - 1));
				chararr[chararr_count++] = (char) (((c & 0x0F) << 12)
						| ((char2 & 0x3F) << 6) | ((char3 & 0x3F) << 0));
				break;
			default:
				/* 10xx xxxx, 1111 xxxx */
				throw new IOException("malformed input around byte " + count);
			}
		}
		// The number of chars produced may be less than utflen
		reader.position(reader.position() + utflen);
		return new String(chararr, 0, chararr_count);
	}
	public String readString(Charset charset){
		int bytes=reader.getShort();
		int off=reader.position();
		
		String string=new String(buffer,off,bytes,charset);
		
		reader.position(off + bytes);
		return string;
	}
	public int getMsgCode() {
		return msgcode;
	}
	public void setMsgCode(int code){
		msgcode=code;
	}
public void setMagicNum(byte magicNum) {
	this.magicNum = magicNum;
}
public int getMagicNum(){
	return magicNum;
}
//	public void BitReversion() {
//		if (this.msgcode > 10000) {
//			// 消息号(int)4个字节 所以从4以后开始位反 10000外的消息为客户端发来的消息
//			DataEncryption.BitReversion(buffer, 4);
//		}
//	}

//	public long getReceiveTime() {
//		return receiveTime;
//	}

	public byte readByte() throws IOException {
		return reader.get();
	}

	public short readShort() throws IOException {
		return reader.getShort();
	}

	public int readInt() throws IOException {
		return reader.getInt();
	}

	public long readLong() throws IOException {
		return reader.getLong();
	}

	public float readFloat() throws IOException {
		return reader.getFloat();
	}

	public double readDouble() throws IOException {
		return reader.getDouble();
	}

	public byte[] getByteBuffer() {
		return buffer;
	}
	public int getBufferOffset(){
		return offset;
	}
	public int getBufferLength(){
		return length;
	}
}

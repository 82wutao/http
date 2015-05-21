package com.ctqh.mobile.common.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DataReader {
	private DataReader() {
	}

	private InputStream inputStream;
	private byte[] buffer = new byte[8];
	
	public static DataReader newReader(String file) throws IOException {
		DataReader txtReader = new DataReader();
		txtReader.inputStream = new FileInputStream(file);
		return txtReader;
	}

	public static DataReader newReader(File file) throws IOException {
		DataReader txtReader = new DataReader();
		txtReader.inputStream = new FileInputStream(file);
		return txtReader;
	}
	public static void close(DataReader reader){
		if(reader==null){
			return ;
		}
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void close() throws IOException {
		this.inputStream.close();
	}

	public int read(byte[] dest, int destOffset, int destLen)
			throws IOException {
		return inputStream.read(dest, destOffset, destLen);
	}
	
	public byte readInt8()throws IOException{
		int val = inputStream.read();
//		System.out.println("readInt8::"+val);
		return (byte)val;
	}
	public short readInt16()throws IOException{
		inputStream.read(buffer, 0, 2);
		long ret = convert(2);
//		System.out.println("readInt16::"+ret);
		return (short)ret;
	}
	public int readInt32()throws IOException{
		inputStream.read(buffer, 0, 4);
		long ret = convert(4);
//		System.out.println("readInt32::"+ret);
		return (int)ret;
	}
	public long readInt64() throws IOException{
		inputStream.read(buffer, 0, 8);
		long ret = convert(8);
//		System.out.println("readInt64::"+ret);
		return (long)ret;
	}
	public String readString(Charset charset) throws IOException{
		inputStream.read(buffer, 0, 2);
		long ret = convert(2);
		short len = (short)ret;
		
		byte[] string = new byte[len];
		inputStream.read(string, 0, len);
		
		String val = new String(string,charset==null?Charset.defaultCharset():charset);
//		System.out.println("readString::"+val);
		return val;
	}
	
	public long convert(int bytes) {
		long out = 0x0000000000000000;
		for (int i = 0; i < bytes; i++) {
			out = out << 8;
			out = (buffer[0 + i] & 0xff) | out;

		}
		return out;
	}
}

package com.ctqh.mobile.common.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class DataWriter {
	private DataWriter() {
	}

	private OutputStream outputStream;
	private byte[] buffer = new byte[8];

	public static DataWriter newWriter(String file, boolean append)
			throws IOException {
		DataWriter txtReader = new DataWriter();
		txtReader.outputStream = new FileOutputStream(file, append);
		return txtReader;
	}

	public static DataWriter newWriter(File file, boolean append)
			throws IOException {
		DataWriter txtReader = new DataWriter();
		txtReader.outputStream = new FileOutputStream(file, append);
		return txtReader;
	}
	public static void close(DataWriter writer){
		if(writer==null){
			return ;
		}
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void close() throws IOException {
		this.outputStream.close();
	}

	public void write(byte[] src, int srcOffset, int srcLen) throws IOException {
		outputStream.write(src, srcOffset, srcLen);
		outputStream.flush();
	}
	
	public void writeInt8(byte val)throws IOException{
//		System.out.println("writeInt8::"+val);
		outputStream.write(val);
	}
	public void writeInt16(short val)throws IOException{
//		System.out.println("writeInt16::"+val);
		convert(2, val);
		write(buffer, 0, 2);
	}
	public void writeInt32(int val)throws IOException{
//		System.out.println("writeInt32::"+val);
		convert(4, val);
		write(buffer, 0, 4);
	}
	public void writeInt64(long val)throws IOException{
//		System.out.println("writeInt64::"+val);
		convert(8, val);
		write(buffer, 0, 8);
	}
	public void writeString(String val,Charset charset)throws IOException{
//		System.out.println("writeString::"+val);
		byte[] string =val.getBytes(charset==null?Charset.defaultCharset():charset);
		convert(2, string.length);
		write(buffer, 0, 2);
		
		write(string,0,string.length);
	}
	public void convert(int bytes, long value){
		for (int i = 0; i < bytes; i++) {
			buffer[0+i] = (byte) (value >> (bytes * 8 - (i+1)*8));
		}
	}
}
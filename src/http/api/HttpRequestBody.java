package http.api;

import http.HttpProtocol;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.kernel.NetSession;

import common.io.XBuffer;

/**
 * 
 * @author wutao
 *
 */
public class HttpRequestBody {
	public static final String Part_File = "File";
	public static final String Part_Paramater = "Param";

	private byte[] boundary = null;
	private ArrayList<Byte> boundary_check = new ArrayList<Byte>();
	
	private String charset = null;
	private long bodyLength;
	private long readed;
	private NetSession<HttpProtocol> session;

	private String type = null;
	private Map<String, String[]> parameters=new HashMap<String, String[]>();
	private String fileName = null;
	private boolean partEnd=false;

	public HttpRequestBody(long bodyLength, byte[] boundary,
			NetSession<HttpProtocol> netSession,String charset) {
		this.boundary = boundary;
		this.bodyLength = bodyLength;
		session = netSession; 
		this.charset = charset;
	}

	public boolean hasMorePart() throws IOException {
		if (session.readableBufferRemaining()==0) {
			return false;
		}
		if (readed + boundary.length + 6 == bodyLength) {//--~--\r\n
			skipBytes(boundary.length + 6);
			return false;
		}
		
		skipBytes(boundary.length + 4);//--boundary\r\n		

		String desc = readline();// Content-Disposition: form-data; name="myfile"; filename="23277.html"
		desc = URLDecoder.decode(desc, charset);
		
		
///////////////////////////////////////////////////////////////////
		int fileTag = desc.indexOf("filename");
		if (fileTag == -1) {
			this.type = HttpRequestBody.Part_Paramater;
			
			skipBytes(2);//\r\n	
			
			String val = readline();//form field value
			val = URLDecoder.decode(val, charset);

			String[] head_body = desc.split(":");
			String[] fields = head_body[1].split(";");
			String[] nameField = fields[1].split("=");
			String paramName = nameField[1].trim().substring(1,
					nameField[1].trim().length() - 1);
			
			parameters.put(paramName, new String[]{val});
			partEnd=true;
			return true;
		}
		
		this.type = HttpRequestBody.Part_File;

		String type = readline();//Content-Type: text/plain
		
		skipBytes(2);

		String[] head_body = desc.split(":");
		String[] fields = head_body[1].split(";");
		String[] nameField = fields[2].split("=");
		fileName = nameField[1].trim().substring(1,
				nameField[1].trim().length() - 1);
		partEnd=false;
		return true;
	}
	private String readline() throws IOException{
		StringBuilder builder = new StringBuilder();
		while (true){
			byte c = session.read();
			if (c ==-1) {
				session.readBytesFromChanel();				
				continue;
			}
			readed ++;
			if (c == '\r') {
				continue;
			}
			if (c == '\n') {
				break;
			}
			builder.append(c);
		}
		return builder.toString();
	}
	private void skipBytes(int skip) throws IOException{
		int remaining = 0;
		int skiped = 0;
		do {
			remaining = session.readableBufferRemaining();
			if (remaining+skip >= skip){				
				break;
			}
			session.skipRead(remaining);
			skiped = skip + remaining;
			
			session.readBytesFromChanel();
		}while(remaining+skiped < skip);
		
		session.skipRead(skip-skiped);
		readed += skip;
	}
	
	public String getPartType() {
		return type;
	}

	//TODO first read boundary's length;second read more as you can
	public int read(byte[] destBuffer) throws IOException {
		if (partEnd) {
			return -1;
		}
		
		//TODO boundary_check is not empty
		
		int destLength = destBuffer.length;
		int destLimit=0;
		
		destLimit=session.read(destBuffer, 0);//TODO 根据boundary 来定义offset
		
		
		for (int i = 0; i < destLimit; i++) {
			
			int ret =isBoundary(destBuffer,i,destLimit);
			if (ret == 0) {
				destLimit = i;
				//TODO src buffer pos ？
				 
				for (int j = i; j < destLimit; j++) {
					boundary_check.add(destBuffer[j]);
				}
				break;
			}
			if (ret == 1) {
				readed += 2;//\r\n(--boundary)
				destLimit = i;
				//TODO src buffer pos ？
				break;
			}
			readed++;
		}
		return destLimit;
		
		int destSize =destBuffer.length;
		int destIndex=0;
		
		buffer.compact();

		int begin = buffer.getPosition();
		int limit = buffer.getLimit();
		byte[] data = buffer.getData();
		

		if (readed + begin < bodyLength) {
			long unreaded = bodyLength - readed;
			long space =  limit - begin;
			int ret = inputStream.read(data, begin, (int)(space < unreaded?space:unreaded));
			buffer.setPosition(begin + ret);			
		}
		buffer.readyReadingFromBuffer();
		begin = buffer.getPosition();
		limit = buffer.getLimit();
		
		for (int i = begin; 
				i < limit & destIndex<destSize; 
				i++) {
			int ret =isBoundary(data,i,limit);
			if (ret==0) {
				partEnd=false;
				break;
			}
			if (ret==1) {
				partEnd=true;
				break;
			}
			
			destBuffer[destIndex]=data[i];
			destIndex++;
		}

		if (partEnd) {
			buffer.setPosition(begin+destIndex+2);
			readed += destIndex+2;
		}else {
			buffer.setPosition(begin+destIndex);
			readed += destIndex;
		}
		return destIndex;
	}

	public String[] getParameter(String name) {
		return parameters.get(name);
	}

	public String getFileName() {
		return fileName;
	}
	/**
	 * 
	 * @param bodyData
	 * @param testBegin
	 * @param limit
	 * @return -1 false,0 part yes ,1 full yes
	 */
	private int isBoundary(byte[] bodyData,int testBegin,int limit){
		int index = testBegin;
		
		int len = limit - testBegin;
		if (len < 1) {
			return -1;
		}
		
		if (bodyData[index]!='\r') {//不相等，不用判断了
			return -1;
		}
		if (len < 2) {//相等，但是没有内容了，所以是部分相同
			return 0;
		}
		
		if (bodyData[index+1]!='\n') {
			return -1;
		}
		if (len < 3) {//相等，但是没有内容了，所以是部分相同
			return 0;
		}
		
		if (bodyData[index+2]!='-') {
			return -1;
		}
		if (len < 4) {//相等，但是没有内容了，所以是部分相同
			return 0;
		}
		
		if (bodyData[index+3]!='-') {
			return -1;
		}
		if (len < 5) {//相等，但是没有内容了，所以是部分相同
			return 0;
		}
		
		index += 4;
		if (len < (boundary.length+4)) {
			for (int i = index; i < len; i++) {
				if (bodyData[i]==boundary[i]) {//相等，等待最后的长度判断来决定是不是部分相等
					continue;
				}
				
				return -1;//不相等，所以是不相等
			}
			
			return 0;
		}
		
		for (int i = index; i < boundary.length+4; i++) {
			if (bodyData[i]!=boundary[i]) {
				return -1;
			}
		}
		return 1;
	}
}

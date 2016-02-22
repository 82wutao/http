package http.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

import common.io.XBuffer;

/**
 * 
 * @author wutao
 *
 */
//TODO rename Body
public class MultiPartForm {
	public static final String Part_File = "File";
	public static final String Part_Paramater = "Param";

	private byte[] boundary = null;

	private XBuffer buffer = null;
	private long bodyLength;

	private InputStream inputStream = null;
	private long readed;

	private String type = null;
	private String paramName = null;
	private String paramValue = null;
	private String fileName = null;
	private boolean partEnd=false;

	public MultiPartForm(long bodyLength, byte[] boundary, XBuffer bodyBegin,
			InputStream inputStream) {
		this.boundary = boundary;
		this.bodyLength = bodyLength;
		this.buffer = bodyBegin;
		this.inputStream = inputStream;
	}

	public boolean hasMorePart() throws IOException {
		if (readed == bodyLength) {
			return false;
		}
		if (readed + boundary.length + 6 == bodyLength) {//--~--\r\n
			readed += boundary.length + 6;
			return false;
		}

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

		readed += boundary.length + 4;//boundary
		buffer.setPosition(begin + boundary.length + 4);
		begin = buffer.getPosition();

		int end = readLine(data, begin, limit);//Content-Disposition
		String desc = new String(data, begin, end - begin, "UTF8");
		desc = URLDecoder.decode(desc, "UTF8");
		readed += ((end - begin) + 2);
		buffer.setPosition(begin + (end - begin) + 2);
		begin = buffer.getPosition();
///////////////////////////////////////////////////////////////////
		int fileTag = desc.indexOf("filename");
		if (fileTag == -1) {
			this.type = MultiPartForm.Part_Paramater;

			end = readLine(data, begin + 2, limit);//\r\nform field value
			String val = new String(data, begin + 2, end - begin - 2, "UTF8");
			val = URLDecoder.decode(val, "UTF8");
			readed += (end - begin) + 2;
			buffer.setPosition(begin + (end - begin) + 2);

			// Content-Disposition: form-data; name="myfile"; filename="23277.html"
			String[] head_body = desc.split(":");
			String[] fields = head_body[1].split(";");
			String[] nameField = fields[1].split("=");
			paramName = nameField[1].trim().substring(1,
					nameField[1].trim().length() - 1);
			paramValue = val;
			partEnd=true;
//			buffer.readyReadingFromBuffer();
			return true;
		}
		this.type = MultiPartForm.Part_File;

		end = readLine(data, begin, limit);//Content-Type: text/plain
		readed += (end - begin) + 4;
		buffer.setPosition(begin + (end - begin) + 4);

		String[] head_body = desc.split(":");
		String[] fields = head_body[1].split(";");
		String[] nameField = fields[2].split("=");
		fileName = nameField[1].trim().substring(1,
				nameField[1].trim().length() - 1);
		partEnd=false;
		return true;
	}

	public String getPartType() {
		return type;
	}

	public int read(byte[] destBSuffer) throws IOException {
		if (partEnd) {
			return -1;
		}
		
		int destSize =destBSuffer.length;
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
				i < limit&destIndex<destSize; 
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
			
			destBSuffer[destIndex]=data[i];
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

	private int readLine(byte[] data, int begin, int limit) {
		for (int i = begin; i < limit; i++) {
			if (data[i] == '\r' && data[i + 1] == '\n') {
				return i ;
			}
		}
		return -1;
	}

	public String getParamName() {
		return paramName;
	}

	public String getParamValue() {
		return paramValue;
	}

	public String getFileName() {
		return fileName;
	}
	/**
	 * 
	 * @param bodyData
	 * @param testBegin
	 * @param limit
	 * @return -1 false,0 not enough,1yes
	 */
	private int isBoundary(byte[] bodyData,int testBegin,int limit){
		int testLength =limit-testBegin;
		if (testLength<boundary.length+4) {
			return 0;
		}
		
		if (bodyData[testBegin]!='\r') {
			return -1;
		}
		if (bodyData[testBegin+1]!='\n') {
			return -1;
		}
		if (bodyData[testBegin+2]!='-') {
			return -1;
		}
		if (bodyData[testBegin+3]!='-') {
			return -1;
		}
		testBegin += 4;
		for (int i = 0; i < boundary.length; i++) {
			if (bodyData[i+testBegin]!=boundary[i]) {
				return -1;
			}
		}
		return 1;
	}
}

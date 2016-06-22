package http.body;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import http.HttpProtocol;
import http.api.RequestBody;
import net.kernel.NetSession;

/**
 * 
 * @author wutao
 *
 */
public class RequestBodyMultipartForm implements RequestBody {
	private byte[] boundary = null;
	
	private String charset = null;
	private long bodyLength;
	private long readed;
	private NetSession<HttpProtocol> session;

	private String type = null;
	private Map<String, String[]> parameters=new HashMap<String, String[]>();
	private String fileName = null;
	private boolean partEnd=true;

	public RequestBodyMultipartForm(long bodyLength, byte[] boundary,
			NetSession<HttpProtocol> netSession,String charset) {
		this.boundary = boundary;
		this.bodyLength = bodyLength;
		session = netSession; 
		this.charset = charset;
	}

	/* (non-Javadoc)
	 * @see http.api.RequestBody#hasMorePart()
	 */
	@Override
	public int hasMorePart() throws IOException {
		if (!partEnd) {
			return -1;
		}
		if (session.readableBufferRemaining()==0) {
			return 0;
		}
		if (readed + boundary.length + 6 == bodyLength) {//--~--\r\n
			session.skipBytes(boundary.length + 6);
			readed = readed+boundary.length+6;
			return 0;
		}
		
		session.skipBytes(boundary.length + 4);//--boundary\r\n		
		readed = readed+boundary.length+4;
		
		String desc = session.readLine(new byte[]{'\r','\n'});// Content-Disposition: form-data; name="myfile"; filename="23277.html"
		readed = readed +desc.length();
		desc = URLDecoder.decode(desc, charset);
		
		
///////////////////////////////////////////////////////////////////
		int fileTag = desc.indexOf("filename");
		if (fileTag == -1) {
			this.type = RequestBody.Part_Paramater;
			
			session.skipBytes(2);//\r\n	
			readed = readed+2;
			
			String val = session.readLine(new byte[]{'\r','\n'});//form field value
			readed = readed +val.length();
			val = URLDecoder.decode(val, charset);

			String[] head_body = desc.split(":");
			String[] fields = head_body[1].split(";");
			String[] nameField = fields[1].split("=");
			String paramName = nameField[1].trim().substring(1,
					nameField[1].trim().length() - 1);
			
			parameters.put(paramName, new String[]{val});
			partEnd=true;
			return 1;
		}
		
		this.type = RequestBody.Part_File;

		String mimeType =session.readLine(new byte[]{'\r','\n'});//Content-Type: text/plain
		readed = readed +mimeType.length();
		session.skipBytes(2);//\r\n
		readed = readed+2;

		String[] head_body = desc.split(":");
		String[] fields = head_body[1].split(";");
		String[] nameField = fields[2].split("=");
		fileName = nameField[1].trim().substring(1,
				nameField[1].trim().length() - 1);
		partEnd=false;
		return 1;
	}
	
	/* (non-Javadoc)
	 * @see http.api.RequestBody#getPartType()
	 */
	@Override
	public String getPartType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see http.api.RequestBody#read(byte[])
	 */
	@Override
	public int read(byte[] destBuffer,int offset,int length) throws IOException {
		if (partEnd) {
			return -1;
		}
		
		int readed=session.read(destBuffer, offset,length);
		for (int i = offset; i < readed; i++) {
			int ret =isBoundary(destBuffer,i,readed);
			if (ret == 0) {
				readed = i;
				session.backRead(readed - i);
				break;
			}
			if (ret == 1) {
				partEnd =true;
				readed += 2;//\r\n(--boundary\r\n|--)
				readed = i;
				session.backRead(readed - i+2);
				break;
			}
			readed++;
		}
		return readed;
	}

	/* (non-Javadoc)
	 * @see http.api.RequestBody#getParameter(java.lang.String)
	 */
	@Override
	public String[] getParameter(String name) {
		return parameters.get(name);
	}

	/* (non-Javadoc)
	 * @see http.api.RequestBody#getFileName()
	 */
	@Override
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
	@Override
	public String getString() {
		return null;
	}
}

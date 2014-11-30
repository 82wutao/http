package http.base;

import http.app.Cookie;
import http.app.HttpResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SimpleHttpResponse implements HttpResponse {
	Map<String, String> headMap = new HashMap<String, String>();

	String pv = null;
	String statusCode = "200";

	File document=null;
	public SimpleHttpResponse(String protocolVersion) {
		pv = protocolVersion;
	}

	@Override
	public void setResponseHead(String key, String value) {
		headMap.put(key, value);
	}

	@Override
	public void setResponseHeads(Map<String, String> heads) {
		headMap.putAll(heads);
	}

	@Override
	public void setStatusCode(int code) {
		statusCode = code+"";
	}

	@Override
	public void setContentType(String type) {
		headMap.put(Head_ContentType_Response, type);
	}

	@Override
	public void setContentLength(long lenght) {
		headMap.put(Head_ContentLength_Response, lenght+"");
	}

	@Override
	public void write(byte[] data, int off, int length) {
	}

	@Override
	public void write(String text) {
	}

	@Override
	public void write(File file) {
		document=file;
	}

	@Override
	public void addCookie(Cookie cookie) {
		
	}

	@Override
	public void serialize(OutputStream outputStream) throws IOException {
		byte[] version =pv.getBytes(Charset.forName("ASCII"));
		outputStream.write(version);
		
		outputStream.write(' ');
		byte[] status =statusCode.getBytes(Charset.forName("ASCII"));
		outputStream.write(status);
		
		byte[] line = "\r\n".getBytes(Charset.forName("ASCII"));
		outputStream.write(line);
		
		Set<Entry<String, String>> headers =headMap.entrySet();
		for (Entry<String, String> entry :headers) {
			byte[] key = entry.getKey().getBytes(Charset.forName("ASCII"));
			byte[] value = entry.getValue().getBytes(Charset.forName("ASCII"));
			outputStream.write(key);
			outputStream.write(':');
			outputStream.write(value);
			
			outputStream.write(line);
		}
		outputStream.write(line);
		
		byte[] buffer = new byte[1024];
		FileInputStream inputStream = new FileInputStream(document);
		try{
		for (int readed  = inputStream.read(buffer,0,1024); readed  >0;readed  = inputStream.read(buffer,0,1024) ) {
			outputStream.write(buffer,0,readed);
		}
		}finally{
			inputStream.close();
		}
	}
}

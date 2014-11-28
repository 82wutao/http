package http.base;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import http.app.HttpRequest;
import http.net.kernel.XBuffer;

public class SimpleHttpRequest implements HttpRequest {
	private XBuffer header;
	
	private List<XBuffer> body=new ArrayList<XBuffer>();
	private int bodyChunkIndex=0;
	
	
	public SimpleHttpRequest() {
		// TODO Auto-generated constructor stub
	}
	
	

	@Override
	public Map<String, String> getRequestHeads() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestHead(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHttpVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestUri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContentLength() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParamerValue(String paramer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean endOfBody() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int readFromBody(byte[] buffer) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void setHead(XBuffer buffer){
		header=buffer;
	}
	public void append2Body(XBuffer buffer){
		
	}



	@Override
	public String dumpHead() {
		int off=header.getPosition();
		int limit = header.getLimit();
		int length = limit - off;
		return new String(header.getData(),off,length,Charset.forName("utf8"));
	}



	@Override
	public String dumpBody() {
		// TODO Auto-generated method stub
		return null;
	}

}

package http.app;

import java.util.Map;

public interface HttpRequest {
	
	public Map<String, String> getRequestHeads() ;
	public String getRequestHead(String key) ;

	public String getRequestMethod();
	public String getHttpVersion();
	public String getRequestUri();
	public String getContentLength();
	
	public String getParamerValue(String paramer);
	
	public boolean endOfBody();
	public int readFromBody(byte[] buffer);
	
	public String dumpHead();
	public String dumpBody();
}

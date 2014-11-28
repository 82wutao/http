package http.app;

import java.util.Map;

public interface HttpRequest {
	public static final String Head_METHOD_Request="Method";
	public static final String Head_Uri_Request="Uri";
	public static final String Head_HttpVersion_Request="Version";
	public static final String Head_Host_Request="Host";
	public static final String Head_UserAgent_Request="User-Agent";
	public static final String Head_Accept_Request="Accept";
	public static final String Head_AcceptLanguage_Request="Accept-Language";
	public static final String Head_AcceptEcoding_Request="Accept-Encoding";
	public static final String Head_DNT_Request="DNT";
	public static final String Head_Connection_Request="Connection";
	public static final String Head_ContentLength_Request="Content-Length";
	
	public Map<String, String> getRequestHeads() ;
	public String getRequestHead(String key) ;

	public String getRequestMethod();
	public String getHttpVersion();
	public String getRequestUri();
	public String getContentLength();
	
	public String getParamerValue(String paramer);
	
	public boolean endOfBody();
	public int readFromBody(byte[] buffer,int off,int length);
	
	public String dumpHead();
	public String dumpBody();
}

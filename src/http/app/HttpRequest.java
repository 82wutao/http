package http.app;

import java.io.IOException;
import java.util.Map;

public interface HttpRequest {
	public static final String Head_METHOD_Request = "Method";
	public static final String Head_Uri_Request = "Uri";
	public static final String Head_HttpVersion_Request = "Version";
	
	public static final String Head_Accept_Request = "Accept";
	public static final String Head_AcceptLanguage_Request = "Accept-Language";
	public static final String Head_AcceptEcoding_Request = "Accept-Encoding";
	public static final String Head_Connection_Request = "Connection";
	public static final String Head_ContentLength_Request = "Content-Length";
	public static final String Head_ContentType_Request = "Content-Type";
	public static final String Head_Cookie_Request = "Cookie";
	public static final String Head_DNT_Request = "DNT";
	public static final String Head_Host_Request = "Host";
	public static final String Head_Referer_Request = "Referer";
	public static final String Head_UserAgent_Request = "User-Agent";
	
	public Map<String, String> getRequestHeads();

	public String getRequestHead(String key);

	public String getRequestMethod();

	public String getHttpVersion();

	public String getRequestUri();

	public String getContentLength();

	public String getParamerValue(String paramer);

	public Cookie[] getCookies();

	public int readFromBody(byte[] buffer, int off, int length)
			throws IOException;

	public String dumpHead();

	public String dumpBody();
}

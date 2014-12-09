package http.api;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public interface HttpResponse {
	public static final String Head_AcceptRanges_Response="Accept-Ranges";
	public static final String Head_Age_Response="Age";
	public static final String Head_CacheControl_Response="Cache-Control";
	public static final String Head_Connection_Response="Connection";
	public static final String Head_ContentEncoding_Response="Content-Encoding";
	public static final String Head_ContentLength_Response="Content-Length";
	public static final String Head_ContentType_Response="Content-Type";
	
	public static final String Head_Date_Response="Date";
	public static final String Head_Expires_Response="Expires";
	public static final String Head_Server_Response="Server";
	public static final String Head_SetCookie_Response="Set-Cookie";
	public static final String Head_Vary_Response="Vary";
	public static final String Head_XCache_Response="X-Cache";



	
	public void setResponseHead(String key, String value) ;
	public void setResponseHeads(Map<String, String> heads);

	public void setHttpVersion(String version);
	public void setStatusCode(int code);
	public void setContentType(String type);
	public void setContentLength(long lenght);
	
	public void write(byte[] data,int off,int length);
	public void write(String text);
	public void write(File file);
	
	public void serialize(OutputStream outputStream)throws IOException ;
	
	public void addCookie(Cookie cookie);
}

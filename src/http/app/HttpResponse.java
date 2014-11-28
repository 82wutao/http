package http.app;

import java.io.File;
import java.util.Map;

public interface HttpResponse {
	public void setResponseHead(String key, String value) ;
	public void setResponseHeads(Map<String, String> heads);

	public void getStatusCode(int code);
	public void setContextType(String type);
	public void setContextLength(int lenght);
	
	public void write(byte[] data,int off,int length);
	public void write(String text);
	public void write(File file);
}

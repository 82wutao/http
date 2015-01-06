package http.api;

import java.io.File;

public interface WebAppContext {
	
	public void initial(File config) ;

	public String getContextPath() ;

	public String getContextFileSystemPath() ;

	public ServerContext getServerContext();

	public void doService(HttpRequest request, HttpResponse response)throws Exception ;
	public String mimeType(String resourceType) ;
	
	public String getContextAttribute(String param);
}

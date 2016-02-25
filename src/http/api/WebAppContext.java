package http.api;

import java.io.File;

public interface WebAppContext {
	
	public void initial(File config) ;
	public void destory();
	
	public String getContextPath();
	public String getContextFileSystemPath() ;

	
	public String getMimeType(String resourceType);
	public String getContextAttribute(String param);
	
}

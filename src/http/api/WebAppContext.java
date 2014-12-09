package http.api;

import java.io.File;

public class WebAppContext {
	
	public void initial(File config) {

	}

	public String getContextPath() {
		return "";
	}

	public String getContextFileSystemPath() {
		return "";
	}

	public ServerContext getServerContext() {
		return null;
	}

	public void doService(HttpRequest request, HttpResponse response) {
	}
	public String mimeType(String resourceType) {
		return null;
	}
}

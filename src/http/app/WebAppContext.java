package http.app;

import java.io.File;

import http.HttpServerContext;

public class WebAppContext {
	
	public void initial(File config) {

	}

	public String getContextPath() {
		return "";
	}

	public String getContextFileSystemPath() {
		return "";
	}

	public HttpServerContext getServerContext() {
		return null;
	}

	public void doService(HttpRequest request, HttpResponse response) {
	}
	public String mimeType(String resourceType) {
		return null;
	}
}

package http.base.staticdoc;

import http.api.HttpRequest;
import http.api.HttpResponse;
import http.api.HttpServerlet;
import http.api.ServerContext;
import http.api.WebAppContext;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SimpleWebAppContext implements WebAppContext {
	ServerContext serverContext;
	String contextPath;
	String filePath;
	HttpServerlet documentServerlet = new StaticDocumentServerlet();
	HttpServerlet dirServerlet = new DirectoryServerlet();
	
	Map<String, String> mimeMap = new HashMap<String, String>();

	public SimpleWebAppContext(ServerContext serverContext, String contextPath,
			String fileSysPath) {
		this.serverContext = serverContext;
		this.contextPath = contextPath;
		filePath = fileSysPath;
	}

	public void initial(File config) {
		boolean exist = config.exists();
		if (!exist) {
			return;
		}
	}

	public String getContextPath() {
		return contextPath;
	}

	public String getContextFileSystemPath() {
		return filePath;
	}

	public ServerContext getServerContext() {
		return serverContext;
	}
	public void doService(HttpRequest request, HttpResponse response) {
		String method = request.getRequestMethod();
		HttpServerlet serverlet =documentServerlet;
		String uri = request.getRequestUri();
		if (uri.endsWith("/")) {
			serverlet=dirServerlet;
		}
		
		if (method.equals("CONNECT")) {
			serverlet.doCONNECT(this, request, response);
		} else if (method.equals("OPTIONS")) {
			serverlet.doOPTIONS(this, request, response);
		} else if (method.equals("TRACE")) {
			serverlet.doTRACE(this, request, response);
		} else if (method.equals("DELETE")) {
			serverlet.doDELETE(this, request, response);
		} else if (method.equals("PUT")) {
			serverlet.doPUT(this, request, response);
		} else if (method.equals("POST")) {
			serverlet.doPOST(this, request, response);
		} else if (method.equals("HEAD")) {
			serverlet.doHEAD(this, request, response);
		} else if (method.equals("GET")) {
			serverlet.doGET(this, request, response);
		}

	}

	public String mimeType(String resourceType) {
		String mime = mimeMap.get(resourceType);
		return mime == null ? "application/octet-stream" : mime;
	}
}

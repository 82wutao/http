package http.base.staticdoc;

import http.api.HttpRequest;
import http.api.HttpResponse;
import http.api.HttpServerlet;
import http.api.WebAppContext;
import http.protocol.ContentType;
import net.ServerContext;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class SimpleWebAppContext implements WebAppContext {
	
	ServerContext serverContext;
	String contextPath;
	String filePath;
	HttpServerlet documentServerlet = new StaticDocumentServerlet();
	HttpServerlet monitorServerlet = new MonitorServlet();
	
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
	public void doService(HttpRequest request, HttpResponse response)throws Exception {
		String method = request.getRequestMethod();
		HttpServerlet serverlet =documentServerlet;
		if (request.getRequestUri().endsWith("module.php")) {
			serverlet =monitorServerlet;
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
	
	@Override
	public String getContextAttribute(String param) {
		return serverContext.geProperty(param, null);
	}

	@Override
	public void destory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getMimeType(String resourceType) {
		String mime = mimeMap.get(resourceType);
		if (mime==null) {
			mime=serverContext.getMimeType(resourceType);
		}
		return mime == null ? ContentType.Application_OctetStream : mime;
	}
	
}

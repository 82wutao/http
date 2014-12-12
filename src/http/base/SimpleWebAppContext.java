package http.base;

import http.api.HttpRequest;
import http.api.HttpResponse;
import http.api.HttpServerlet;
import http.api.ServerContext;
import http.api.WebAppContext;
import http.base.staticdoc.StaticDocumentServerlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class SimpleWebAppContext extends WebAppContext {
	ServerContext serverContext;
	String contextPath;
	String filePath;
	HttpServerlet documentServerlet = new StaticDocumentServerlet();
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
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream("mime.txt");
			Properties properties = new Properties();
			properties.load(inputStream);
			Set<Entry<Object, Object>> pSet = properties.entrySet();
			for (Entry<Object, Object> entry : pSet) {
				mimeMap.put(entry.getKey().toString(), entry.getValue()
						.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
			}
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

	public void doServie(HttpRequest request, HttpResponse response) {
		String method = request.getRequestMethod();
		if (method.equals("CONNECT")) {
			documentServerlet.doCONNECT(this, request, response);
		} else if (method.equals("OPTIONS")) {
			documentServerlet.doOPTIONS(this, request, response);
		} else if (method.equals("TRACE")) {
			documentServerlet.doTRACE(this, request, response);
		} else if (method.equals("DELETE")) {
			documentServerlet.doDELETE(this, request, response);
		} else if (method.equals("PUT")) {
			documentServerlet.doPUT(this, request, response);
		} else if (method.equals("POST")) {
			documentServerlet.doPOST(this, request, response);
		} else if (method.equals("HEAD")) {
			documentServerlet.doHEAD(this, request, response);
		} else if (method.equals("GET")) {
			documentServerlet.doGET(this, request, response);
		}

	}

	public String mimeType(String resourceType) {
		String mime = mimeMap.get(resourceType);
		return mime == null ? "application/octet-stream" : mime;
	}
}

package http.base;

import http.HttpServerContext;
import http.app.HttpRequest;
import http.app.HttpResponse;
import http.app.HttpServerlet;
import http.app.WebAppContext;
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
	HttpServerContext serverContext;
	String contextPath;
	String filePath;
	HttpServerlet documentServerlet = new StaticDocumentServerlet();
	Map<String, String> mimeMap = new HashMap<String, String>();

	public SimpleWebAppContext(HttpServerContext serverContext,
			String contextPath, String fileSysPath) {
		this.serverContext = serverContext;
		this.contextPath = contextPath;
		filePath = fileSysPath;
	}

	public void initial(File config) {
		boolean exist = config.exists();
		if (!exist) {
			return;
		}
		FileInputStream inputStream =null;
		try {
			inputStream= new FileInputStream("mime.txt");
			Properties properties=new Properties();
			properties.load(inputStream);
			Set<Entry<Object, Object>> pSet=properties.entrySet();
			for (Entry<Object, Object> entry:pSet) {
				mimeMap.put(entry.getKey().toString(), entry.getValue().toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
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

	public HttpServerContext getServerContext() {
		return serverContext;
	}

	public void doService(HttpRequest request, HttpResponse response) {
		documentServerlet.doit(this, request, response);
	}

	public String mimeType(String resourceType) {
		String mime=mimeMap.get(resourceType);
		return mime==null?"application/octet-stream":mime;
	}
}

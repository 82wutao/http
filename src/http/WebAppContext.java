package http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import http.api.HttpRequest;
import http.api.HttpResponse;
import http.api.HttpServerlet;
import http.api.Route;
import http.base.staticdoc.StaticDocumentServerlet;
import http.protocol.ContentType;
import net.ServerContext;

public class WebAppContext implements ServerContext {

	private Properties properties=null;
	private Map<String, Object> attributes = new HashMap<String,Object>();

	private HttpServerlet documentServerlet = new StaticDocumentServerlet();
	
	private Route route = new Route();
	public WebAppContext() {

	}

	@Override
	public void initial(File file) {
		if (!file.exists()) {
			System.exit(1);
		}
		FileInputStream fileInputStream = null;
		Properties properties = new Properties();
		try {
			fileInputStream = new FileInputStream(file);
			properties.load(fileInputStream);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				fileInputStream.close();
			} catch (IOException e) {
			}
		}
		this.properties = properties;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see http.HttpServletContext#destory()
	 */
	@Override
	public void destory() {
	}
	

	@Override
	public String getProperty(String key,String defaultv) {
		String v=properties.getProperty(key);
		if (v==null){
			v = defaultv;
		}
		return v;
	}

	@Override
	public String getMimeType(String resourceType) {
		return ContentType.defaultMappings.get(resourceType);
	}
	@Override
	public Object getContextAttribute(String key) {
		return attributes.get(key);
	}
	
	@Override
	public void setContextAttribute(String key, Object value) {
		attributes.put(key, value);
	}
	
	
	public String getWWWRoot(){
			return getProperty("wwwroot", "./");
	}
	public Route getRoute() {
		return route;
	}
	public void registRoute(String url,HttpServerlet serverlet){
		route.registRoute(url, serverlet);
	}
	public void doService(HttpRequest request, HttpResponse response)throws Exception {
		String uri = request.getRequestUri();
		HttpServerlet serverlet=this.route.matchHttpServerlet(uri);
		
		String method = request.getRequestMethod();
		
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
	
	public void redirect(String url,HttpResponse response){
		
	}
	public void forward(String url,HttpRequest request){
		
	}
	
}

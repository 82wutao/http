package http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import http.protocol.ContentType;
import net.ServerContext;

public class HttpServerContext implements ServerContext {

	private Properties properties=null;
	private Map<String, Object> attributes = new HashMap<String,Object>();
	
	public HttpServerContext() {

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
	public String geProperty(String key,String defaultv) {
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
}

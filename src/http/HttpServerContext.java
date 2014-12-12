package http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import http.api.ServerContext;
import http.api.WebAppContext;
import http.base.Alias;

public class HttpServerContext implements ServerContext {
	String wwwDir;
	String workerThread;
	Alias alias = null;

	public HttpServerContext() {
	alias=	new Alias(this);
	}

	/* (non-Javadoc)
	 * @see http.HttpServletContext#initial(java.lang.String)
	 */
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
		wwwDir = (String) properties.get("wwwRoot");
		workerThread = (String) properties.get("workerThread");

		Set<Entry<Object, Object>> prop = properties.entrySet();
		for (Entry<Object, Object> e : prop) {
			String name = (String) e.getKey();
			if (!name.startsWith("alias.")) {
				continue;
			}
			String[] fix_value = name.split("\\.");
			alias.addAlia(fix_value[1], (String) e.getValue());
		}
	}

	/* (non-Javadoc)
	 * @see http.HttpServletContext#destory()
	 */
	@Override
	public void destory() {
	}

	/* (non-Javadoc)
	 * @see http.HttpServletContext#mappingAppContext(java.lang.String)
	 */
	@Override
	public WebAppContext mappingAppContext(String url) {
		return alias.getWebApp(url);
	}

	/* (non-Javadoc)
	 * @see http.HttpServletContext#getWwwDir()
	 */
	@Override
	public String getWwwDir() {
		return wwwDir;
	}
}

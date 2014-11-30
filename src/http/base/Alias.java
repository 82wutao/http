package http.base;

import http.HttpServerContext;
import http.app.WebAppContext;
import http.base.SimpleWebAppContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Alias {
	WebAppContext staticDocumentContext=null;
	HttpServerContext serverContext=null;
	
	List<String> naming = new ArrayList<String>();
	List<String> diskDirs = new ArrayList<String>();
	List<WebAppContext> apps = new ArrayList<WebAppContext>();

	public Alias(HttpServerContext serverContext ) {
		this.serverContext=serverContext;
		staticDocumentContext=new SimpleWebAppContext(serverContext, "/",serverContext.getWwwDir());
	}
	public void addAlia(String urlContext,String diskPath ) {
		naming.add("/"+urlContext+"/");
		diskDirs.add(diskPath);
		
		WebAppContext context=new SimpleWebAppContext(serverContext,"/"+urlContext+"/",diskPath);
		String config = diskPath+"/conf.txt";
		File configFile =new File(config);
		context.initial(configFile);
		apps.add(context);
	}

	public WebAppContext getWebApp(String uril) {
		for (int i = 0; i < naming.size(); i++) {
			boolean start =uril.startsWith(naming.get(i));
			if(start){
				return apps.get(i);
			}
		}
		return staticDocumentContext;
	}
	
}

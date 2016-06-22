package net;

import java.io.File;

public interface ServerContext {

	public abstract void initial(File conf);
	public abstract void destory();

	public abstract String geProperty(String key,String defaultv);
	public abstract String getMimeType(String resourceType);
	
	public abstract Object getContextAttribute(String key);
	public abstract void setContextAttribute(String key,Object value);
}
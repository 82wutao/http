package http.api;

import java.io.File;

public interface ServerContext {

	public abstract void initial(File conf);

	public abstract void destory();

	public abstract WebAppContext mappingAppContext(String url);

	public abstract String getWwwDir();
	public abstract int getWorkerThreads();
	public abstract String getMimeType(String resourceType);

}
package http.app;

import http.HttpServerContext;

public interface HttpFilter {
	public void filt(HttpServerContext context,HttpRequest request,HttpResponse response);
}

package http.app;

import http.HttpServerContext;

public interface HttpServerlet {
	public void doit(HttpServerContext context, HttpRequest request,
			HttpResponse response);
}

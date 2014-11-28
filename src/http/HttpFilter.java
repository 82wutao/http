package http;

import http.app.HttpRequest;
import http.app.HttpResponse;

public interface HttpFilter {
	public void filt(HttpServerContext context,HttpRequest request,HttpResponse response);
}

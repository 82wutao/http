package http.base;

import http.app.HttpRequest;
import http.app.HttpResponse;
import http.app.HttpServerlet;
import http.app.WebAppContext;

public class SimpleHttpServerlet implements HttpServerlet {
	
	@Override
	public void doit(WebAppContext context, HttpRequest request,
			HttpResponse response) {

	}

	public SimpleHttpServerlet() {
	}
}

package http;

import http.app.HttpRequest;
import http.app.HttpResponse;
import http.app.WebAppContext;

public interface HttpServlet {
	public void doIt(WebAppContext context, HttpRequest request,
			HttpResponse response);
}

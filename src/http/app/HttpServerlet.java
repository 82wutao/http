package http.app;


public interface HttpServerlet {
	public void doit(WebAppContext context, HttpRequest request,
			HttpResponse response);
}

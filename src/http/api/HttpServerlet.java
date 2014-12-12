package http.api;

public interface HttpServerlet {
	public void doGET(WebAppContext context, HttpRequest request,
			HttpResponse response);

	public void doHEAD(WebAppContext context, HttpRequest request,
			HttpResponse response);

	public void doPOST(WebAppContext context, HttpRequest request,
			HttpResponse response);

	public void doPUT(WebAppContext context, HttpRequest request,
			HttpResponse response);

	public void doDELETE(WebAppContext context, HttpRequest request,
			HttpResponse response);

	public void doTRACE(WebAppContext context, HttpRequest request,
			HttpResponse response);

	public void doOPTIONS(WebAppContext context, HttpRequest request,
			HttpResponse response);

	public void doCONNECT(WebAppContext context, HttpRequest request,
			HttpResponse response);

}

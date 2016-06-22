package http.api;

import http.WebAppContext;

public interface HttpServerlet {
	public void doGET(WebAppContext context, HttpRequest request,
			HttpResponse response)throws Exception;

	public void doHEAD(WebAppContext context, HttpRequest request,
			HttpResponse response)throws Exception;

	public void doPOST(WebAppContext context, HttpRequest request,
			HttpResponse response)throws Exception;

	public void doPUT(WebAppContext context, HttpRequest request,
			HttpResponse response)throws Exception;

	public void doDELETE(WebAppContext context, HttpRequest request,
			HttpResponse response)throws Exception;

	public void doTRACE(WebAppContext context, HttpRequest request,
			HttpResponse response)throws Exception;

	public void doOPTIONS(WebAppContext context, HttpRequest request,
			HttpResponse response)throws Exception;

	public void doCONNECT(WebAppContext context, HttpRequest request,
			HttpResponse response)throws Exception;

}

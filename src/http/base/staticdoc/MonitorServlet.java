package http.base.staticdoc;

import app.monitor.Module;
import app.monitor.Result;
import http.api.HttpRequest;
import http.api.HttpResponse;
import http.api.HttpServerlet;
import http.api.WebAppContext;

public class MonitorServlet implements HttpServerlet {

	@Override
	public void doGET(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		
		String module =request.getParamerValue("module");
		Result result=Module.getInstance().getData(module);
		result.write2Response(response);
	}

	@Override
	public void doHEAD(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doPOST(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doPUT(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doDELETE(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doTRACE(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doOPTIONS(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doCONNECT(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		// TODO Auto-generated method stub
		
	}

}

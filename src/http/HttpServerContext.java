package http;

import http.app.HttpFilter;
import http.app.HttpRequest;
import http.app.HttpResponse;
import http.net.kernel.HttpProccesser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpServerContext {
	private List<HttpFilter> filters=new ArrayList<HttpFilter>();
	
	public String getHtmlDocumentDir(){return "";}

	public void initial() {
		
	}

	public void destory() {
	}
	
	public void doFilters(HttpProccesser proccesser,HttpRequest request,HttpResponse response) throws IOException{
		for(HttpFilter filter :filters){
			filter.filt(this, request, response);
		}
		proccesser.destory();
	}
	
	//////////////////////////////////

}

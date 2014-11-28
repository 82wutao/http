package http;

import http.app.HttpRequest;
import http.net.kernel.IOHandler;
import http.net.kernel.NetworkSession;

public class HttpServerContext extends IOHandler{
	public void setRout(Rout rout) {
	}
	
	public String getHtmlDocumentDir(){return "";}

	public void initial() {
	}

	public void destory() {
	}
	
	//////////////////////////////////
	public void newSession(NetworkSession session) {
		System.out.println("server,debug: newSession:"+session.hashCode());
	}

	public void sessionClose(NetworkSession session) {
		System.out.println("server,debug: sessionClose:"+session.hashCode());
	}

	public void recievMessage(NetworkSession session,Object msgObj) {
		HttpRequest request = (HttpRequest)msgObj;
		System.err.println(request.dumpHead());
	}
	public void connected(NetworkSession session){
		
	}
}

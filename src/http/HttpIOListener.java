package http;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;


import http.base.HttpProtocol;
import net.IOListener;
import net.kernel.NetSession;

public class HttpIOListener implements IOListener<HttpProtocol> {
	public HttpIOListener() {
	}

	private Map<NetSession<HttpProtocol>, HttpProtocol> session_request=new HashMap<NetSession<HttpProtocol>, HttpProtocol>();
	
	@Override
	public void closedChannel(NetSession<HttpProtocol> session) {
		session_request.remove(session);
	}

	@Override
	public void connectedChannel(NetSession<HttpProtocol> session) {

	}

	@Override
	public void opennedChannel(NetSession<HttpProtocol> session) {
		HttpProtocol request =new HttpProtocol(Charset.forName("GBK"), session);
		session_request.put(session,request);
	}

	
	@Override
	public HttpProtocol readable(NetSession<HttpProtocol> session) {
		HttpProtocol request =session_request.get(session);
		try {
			boolean ok =request.parseProtocol();
			if (ok) {
				return request;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void writed(NetSession<HttpProtocol> session) {

	}
}

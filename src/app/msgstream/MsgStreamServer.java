package app.msgstream;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import app.msgstream.chains.NodeChain;
import common.log.AppLogger;
import common.log.AppLogger.LogLvl;
import http.HttpProtocol;
import http.api.RequestBody;
import net.Handler;
import net.IOListener;
import net.kernel.NetSession;

public class MsgStreamServer implements IOListener<HttpProtocol>,Handler<HttpProtocol>{
	
	private Map<NetSession<HttpProtocol>, HttpProtocol> session_request=new HashMap<NetSession<HttpProtocol>, HttpProtocol>();
	private NodeChain chain=null;
	
	public void setCalcChain(NodeChain calcChain){chain = calcChain;}
	
	@Override
	public void closedChannel(NetSession<HttpProtocol> session) {
		session_request.remove(session);
	}

	@Override
	public void connectedChannel(NetSession<HttpProtocol> session) {}

	@Override
	public void opennedChannel(NetSession<HttpProtocol> session) {
		HttpProtocol request =new HttpProtocol(Charset.forName("UTF-8"), session);
		session_request.put(session,request);
	}
	@Override
	public HttpProtocol readable(NetSession<HttpProtocol> session,int readable) {
		HttpProtocol request =session_request.get(session);
		if (request == null ) {
			request=new HttpProtocol(Charset.forName("UTF-8"), session);
		}
		
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
	public void writed(NetSession<HttpProtocol> session,int writed) {}

	@Override
	public void handle(NetSession<HttpProtocol> session, HttpProtocol request) {
		RequestBody body = null;
		try {
			body=request.getRequestBody();
		} catch (IOException e) {
			AppLogger logger =AppLogger.getLogger("debug");
			logger.log(LogLvl.Debug, "沒有發現請求體，更不能發現json");
			
			try {
				session.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
		
		String json =null;
		try {
			if (body.hasMorePart()==0) {
				AppLogger logger =AppLogger.getLogger("debug");
				logger.log(LogLvl.Debug, "請求體不能展開，更不能發現json");
				
				try {
					session.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return;
			}
			
			json=body.getString();
			
		} catch (IOException e) {
			AppLogger logger =AppLogger.getLogger("debug");
			logger.log(LogLvl.Debug, "請求體展開出錯，更不能發現json");
			
			try {
				session.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
		
		Message data = new xx(json);
		data.fields=data.convertRaw2Fields(json);
		chain.calc(data);

	}
	
}

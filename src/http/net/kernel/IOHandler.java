package http.net.kernel;


import java.io.IOException;
/**
 * 暴露给程序员的网络事件处理器。一般这里也是应用程序的入口。
 * 如果ioservice没有指定异步参数，所有连接的所有事件都是同步处理的，适用于cpu简单的项目。
 * 反之，不同连接的事件是异步进行处理的，而在一个连接上的不同事件是同步处理的。目的在于使用较少的线程尽可能快速响应用户输入。
 * @author wt
 *
 */
public class IOHandler {
	public void newSession(NetworkSession session) {
		System.out.println("server,debug: newSession:"+session.hashCode());
	}

	public void sessionClose(NetworkSession session) {
		System.out.println("server,debug: sessionClose:"+session.hashCode());
	}

	public void recievMessage(NetworkSession session,Object msgObj) {
		XBuffer msg = (XBuffer)msgObj;
		byte[] str=new byte[msg.getLimit()];
		try {
			msg.readbytes(str, 0, str.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String request=new String(str);
		
		msg.readyWritingToBuffer();
		byte[] bytes =request.getBytes();
		msg.writebytes(bytes, 0, bytes.length);
		session.sendResponseMsg(msg);
		session.closeNetworkSession();
		
	
	}
	public void connected(NetworkSession session){
		
	}
}

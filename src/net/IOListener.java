package net;

import net.kernel.NetSession;

/**
 * 
 * @author wutao
 *
 * @param <T> 会话子类型
 * @param <R> 请求
 */
public interface IOListener<Request> {
	public void opennedChannel(NetSession<Request> session);
	public void connectedChannel(NetSession<Request> session);
	/**
	 * 解析成功返回,请求对象,否则返回null
	 * @param session
	 * @return
	 */
	public Request readable(NetSession<Request> session,int readable);
	public void writed(NetSession<Request> session,int writed);
	public void closedChannel(NetSession<Request> session);
}

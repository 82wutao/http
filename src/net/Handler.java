package net;

import net.kernel.NetSession;

public interface Handler<Request> {
	public void handle(NetSession<Request> session,Request request);
}

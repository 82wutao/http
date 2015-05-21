package com.ctqh.mobile.log;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpLogClientHandler extends IoHandlerAdapter {

	private static Logger logger = LoggerFactory.getLogger(UdpLogClientHandler.class);

	@Override
	public void messageReceived(IoSession session, Object obj) throws Exception {
		String msg = obj.toString();
		if (logger.isInfoEnabled()) {
			logger.info("UdpLogClient receive:"+ msg);
		}
		
		if (msg.startsWith("QOTM: ")) {
			session.close(true);
		}
	}

	@Override
	public void exceptionCaught(IoSession sessin, Throwable cause) throws Exception {
		logger.error("UdpLogClient log error:" + cause.getMessage());
	}

	public void sessionClosed(IoSession session) throws Exception {
		logger.error("UdpLogClient log session is close");
	}
}
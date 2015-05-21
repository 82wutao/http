package com.ctqh.mobile.tcp.msgs;


import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctqh.mobile.common.protocol.MessageInput;
import com.ctqh.mobile.common.protocol.MessageOutput;

public class MessageLogFilter extends IoFilterAdapter {
	private static Logger logger = LoggerFactory.getLogger(MessageLogFilter.class);

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
		super.messageReceived(nextFilter, session, message);
		if (message instanceof MessageInput) {
			MessageInput requestmsg = (MessageInput) message;
			int code = requestmsg.getMsgCode();
			if (code == 11 || code == 40301 || code == 10061) {
				return;
			}
			 logger.debug("c->s:{}" , code);
		}
	}

	@Override
	public void messageSent(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
		super.messageSent(nextFilter, session, writeRequest);

		if (writeRequest.getMessage() instanceof MessageOutput) {
			MessageOutput response = (MessageOutput) writeRequest.getMessage();
//			int code = response.getMsgCode();


			 logger.info("s->c:{}", response.getMsgCode());
		}
	}
}

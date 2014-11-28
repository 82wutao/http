package http.net.kernel;

import http.concurrents.AsynchronizedCondition;
import http.memories.PoolableObject;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
/**
 * �?��异步任务，在线程池里执行。封装了网络事件的处理�?辑�?
 * 同时也是�?��可回收的对象，一块可以复用的内存�? * @author wt
 *
 */
public class AsynchronizedTask implements Runnable,PoolableObject {
	public static final int Task_NewSession = 1;
	public static final int Task_ReceiveMsg = 2;
	public static final int Task_CloseSession = 3;
	public static final int Task_WriteMsg = 4;
	public static final int Task_Connected=5;

	AsynchronizedTask() {
	}

	NetworkSession networkSession;
	int taskType;
	IOService service;
	AsynchronizedCondition count;
	void setData(NetworkSession session, int task,IOService ioService,AsynchronizedCondition condition) {
		networkSession = session;
		taskType = task;
		if (taskType==Task_CloseSession) {
			System.err.println();
		}
		service=ioService;
		count=condition;
	}

	@Override
	public void run() {
		switch (taskType) {
		case Task_NewSession:
			service.handler.newSession(networkSession);
			service.networkSessions.add(networkSession);
			count.incrementAndNotify();
			break;
		case Task_ReceiveMsg:
			List<Object> requestMsgs=networkSession.popRequestMsgs();
			for (Object request:requestMsgs) {
				service.handler.recievMessage(networkSession, request);
			}
			count.incrementAndNotify();
			break;
		case Task_CloseSession:
			try {
				networkSession.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			networkSession.selectionKey.cancel();
			
			service.handler.sessionClose(networkSession);
			service.networkSessions.remove(networkSession);
			networkSession.popResponseMsgs();
			networkSession.connetion = null;
			count.incrementAndNotify();
			break;
		case Task_WriteMsg:
			
			List<XBuffer> msgs = networkSession.popResponseMsgs();
			for (Iterator<XBuffer> it = msgs.iterator(); it.hasNext();) {
				XBuffer msg = it.next();
				it.remove();
				try {
					ByteBuffer buffer = service.networkConfig.factory.encode(service,
							networkSession, msg);
					boolean goon=service.onSendingMsg(networkSession, buffer);
					if (goon) {
						continue;
					} else {
						networkSession.sendingBuffers.add(buffer);
						break;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			for (Iterator<XBuffer> it = msgs.iterator(); it.hasNext();) {
				XBuffer msg = it.next();
				try {
					ByteBuffer buffer = service.networkConfig.factory.encode(service,
								networkSession, msg);
					networkSession.sendingBuffers.add(buffer);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			count.incrementAndNotify();
			break;
		case Task_Connected:
			service.handler.connected(networkSession);
			service.networkSessions.add(networkSession);
			count.incrementAndNotify();
			break;
		default:
			break;
		}
	}
	@Override
	public void free() {
		networkSession=null;
		taskType=0;
		service=null;
		count=null;
	}
}
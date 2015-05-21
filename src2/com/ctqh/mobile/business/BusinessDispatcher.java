package com.ctqh.mobile.business;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctqh.mobile.business.util.MsgCodeAnn;

public class BusinessDispatcher {
	private final static Logger logger = LoggerFactory
			.getLogger(BusinessDispatcher.class);

	private static BusinessDispatcher instance = new BusinessDispatcher();

	public BusinessDispatcher() {
	}

	public static void setInstance(BusinessDispatcher dispatcher) {
		instance = dispatcher;
	}

	public static BusinessDispatcher getInstance() {
		return instance;
	}

	protected Map<Integer, BusinessProcessor> controlors = new HashMap<Integer, BusinessProcessor>();

	public BusinessProcessor getBusinessControlor(int cmd) {
		do {
			if (!logger.isInfoEnabled()) {
				break;
			}
			if (cmd == 10009) {
				break;
			}
			logger.info("we are debugging protocol :" + cmd);
		} while (false);
		return controlors.get(cmd);
	}

	public void scanSocketRequest(Set<Class<?>> classes) {
		for (Class<?> c : classes) {
			MsgCodeAnn ann = c.getAnnotation(MsgCodeAnn.class);
			if (ann == null) {
				continue;
			}
			int msgCode = ann.msgcode();
			Object instance = null;
			try {
				instance = c.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			BusinessProcessor processor = controlors.get(ann.msgcode());
			if (processor != null) {
				logger.warn("协议号{},有多个实现 {},{}", new Object[] { msgCode,
						processor.getClass().getSimpleName(),
						instance.getClass().getSimpleName() });
			}
			controlors.put(msgCode, BusinessProcessor.class.cast(instance));
		}
	}
}

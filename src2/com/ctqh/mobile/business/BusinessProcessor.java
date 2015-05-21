package com.ctqh.mobile.business;

import java.util.List;

import com.ctqh.mobile.GameSession;
import com.ctqh.mobile.common.protocol.MessageInput;
import com.ctqh.mobile.common.protocol.MessageOutput;

/**
 * 其实现类会被自动注册到系统中。请保证实现类的命名符合下列规则：<br>
 * <strong>.+Processor.*</strong>
 * <br>添加<code>MsgCodeAnn</code>接口注释
 * @author Administrator
 *
 */
public interface BusinessProcessor {
	public List<MessageOutput> process(GameSession session,MessageInput request) throws Exception;
	/**
	 * 是否立即执行
	 * @return
	 */
	public boolean immediately();
}

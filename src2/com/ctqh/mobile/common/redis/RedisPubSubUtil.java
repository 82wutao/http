package com.ctqh.mobile.common.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * redis订阅和发布消息
 * 
 * @author lizhiwei
 *
 */
public class RedisPubSubUtil extends JedisPubSub {
	private final static Logger logger = LoggerFactory
			.getLogger(RedisPubSubUtil.class);
	public static RedisPubSubUtil instance = new RedisPubSubUtil();
	private Jedis jr = null;

	public RedisPubSubUtil() {
	}

	public static RedisPubSubUtil getInstance() {
		return instance;
	}

	/**
	 * 初始化
	 */
	public void init(Jedis jedis) {
		jr = jedis;
	}

	/**
	 * 订阅多个消息
	 * @param channels
	 */
	public void proceed(String... channels) {
		getInstance().proceed(jr.getClient(), channels);
	}

	/**
	 * 根据正则表达式订阅多个消息，慎用
	 * @param patterns
	 */
	public void proceedWithPatterns(String... patterns) {
		getInstance().proceedWithPatterns(jr.getClient(), patterns);
	}

	/**
	 * 取得订阅的消息后的处理
	 */
	@Override
	public void onMessage(String channel, String message) {
		logger.info("onMessage: channel[" + channel + "], message[" + message
				+ "]");
	}

	/**
	 * 取得按表达式的方式订阅的消息后的处理  
	 */
	@Override
	public void onPMessage(String pattern, String channel, String message) {
		logger.info("onPMessage: channel[" + channel + "], message[" + message
				+ "]");
	}

	/**
	 * 初始化订阅时候的处理 
	 */
	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		logger.info("onSubscribe: channel[" + channel + "],"
				+ "subscribedChannels[" + subscribedChannels + "]");

	}

	/**
	 * 取消订阅时候的处理 
	 */
	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
		logger.info("onUnsubscribe: channel[" + channel + "], "
				+ "subscribedChannels[" + subscribedChannels + "]");

	}

	/**
	 * 取消按表达式的方式订阅时候的处理 
	 */
	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
		logger.info("onPUnsubscribe: pattern[" + pattern + "],"
				+ "subscribedChannels[" + subscribedChannels + "]");
	}

	/**
	 * 初始化按表达式的方式订阅时候的处理
	 */
	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
		logger.info("onPSubscribe: pattern[" + pattern + "], "
				+ "subscribedChannels[" + subscribedChannels + "]");
	}

}

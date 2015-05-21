package com.ctqh.mobile.common.redis;

import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

public class RedisUtil {

	private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

	private static JedisPool pool;

	private static RedisUtil instance = new RedisUtil();

	protected RedisUtil() {
	}

	public static RedisUtil getInstance() {
		return instance;
	}

	public void init(Properties properties) {
		if (properties == null) {
			throw new IllegalArgumentException(
					"[redis.properties] is not found!");
		}
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(Integer.valueOf(properties
				.getProperty("redis.pool.maxActive")));
		config.setMaxIdle(Integer.valueOf(properties
				.getProperty("redis.pool.maxIdle")));
		config.setMaxWait(Long.valueOf(properties
				.getProperty("redis.pool.maxWait")));
		config.setTestOnBorrow(Boolean.valueOf(properties
				.getProperty("redis.pool.testOnBorrow")));
		config.setTestOnReturn(Boolean.valueOf(properties
				.getProperty("redis.pool.testOnReturn")));
		String ip = properties.getProperty("redis.ip");
		int port = Integer.valueOf(properties.getProperty("redis.port"));
		pool = new JedisPool(config, ip, port);
		try {
			RedisPubSubUtil.getInstance().init(pool.getResource());
		} catch (Exception e) {
			logger.error("RedisPubSub init error",e);
			return;
		}
		logger.info("redis init success......");
	}

	/**
	 * 得到list里从start到end之间的数据
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> get(String key, int start, int end) {
		List<String> list = null;
		Jedis j = pool.getResource();
		list = j.lrange(key, start, end);
		pool.returnResource(j);
		return list;
	}

	/**
	 * 得到list中，某页的数据
	 * 
	 * @param key
	 * @param page
	 *            ：第几页, 从1开始计数
	 * @param amountByPage
	 *            ： 指定每页多少条
	 * @return
	 */
	public List<String> getByPage(String key, int page, int amountByPage) {
		if (page < 1 || amountByPage <= 0) {
			logger.warn("gage < 1 or amountByPage <= 0 !");
			return null;
		}
		List<String> list = null;
		Jedis j = pool.getResource();
		int startIndex = (page - 1) * amountByPage;
		int endIndex = startIndex + amountByPage - 1;
		list = j.lrange(key, startIndex, endIndex);
		pool.returnResource(j);
		return list;
	}

	/**
	 * 得到整个List
	 * 
	 * @param key
	 * @return
	 */
	public List<String> getList(String key) {
		List<String> list = null;
		Jedis j = pool.getResource();
		long len = j.llen(key);
		list = j.lrange(key, 0, len - 1);
		pool.returnResource(j);
		return list;
	}

	/**
	 * 删除list中的某元素
	 * 
	 * @param key
	 * @param count
	 * @param value
	 */
	public void delItemFromList(String key, long count, String value) {
		Jedis j = pool.getResource();
		j.lrem(key, count, value);
		pool.returnResource(j);
	}

	/**
	 * 得到指定key的value值
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key) {
		Jedis j = pool.getResource();
		String val = j.get(key);
		pool.returnResource(j);
		return val;
	}

	/**
	 * 返回指定key的list数组大小
	 * 
	 * @param key
	 * @return
	 */
	public long getListSize(String key) {
		long count = 0;
		Jedis j = pool.getResource();
		count = j.llen(key);
		pool.returnResource(j);
		return count;
	}

	/**
	 * 往list中添加元素 从后往前
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 */
	public void rAddToList(String key, String value, int seconds) {
		Jedis j = pool.getResource();
		Transaction tx = j.multi();
		tx.rpush(key, value);
		if (seconds > 0) {
			tx.expire(key, seconds);
		}
		tx.exec();
		pool.returnResource(j);
	}

	/**
	 * 往list中添加元素 从后往前
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 */
	public void rAddToList(String key, String value) {
		rAddToList(key, value, 0);
	}

	/**
	 * 设置多组<key value>到redis
	 * 
	 * @param list
	 *            ： 存放多组key和value, key和value必须成对
	 */
	public void mset(List<String> list) {
		if (list != null) {
			String[] strTmp = new String[list.size()];
			list.toArray(strTmp);
			Jedis j = pool.getResource();
			Transaction tx = j.multi();
			tx.mset(strTmp);
			tx.exec();
			pool.returnResource(j);
		}
	}

	/**
	 * 设置List到redis
	 * 
	 * @param key
	 * @param list
	 * @param seconds
	 */
	public void set(String key, List<String> list, int seconds) {
		if (list != null && list.size() > 0) {
			String[] strTmp = new String[list.size()];
			list.toArray(strTmp);
			Jedis j = pool.getResource();
			Transaction tx = j.multi();
			for (int i = 0; i < strTmp.length; i++) {
				tx.lpush(key, strTmp[i]);
			}
			if (seconds > 0) {
				tx.expire(key, seconds);
			}
			tx.exec();
			pool.returnResource(j);
		}
	}

	public void set(String key, String value, int seconds) {
		Jedis j = pool.getResource();
		Transaction tx = j.multi();
		tx.set(key, value);
		if (seconds > 0) {
			tx.expire(key, seconds);
		}
		tx.exec();
		pool.returnResource(j);
	}

	public void set(String key, String value) {
		set(key, value, 0);
	}

	public void flush() {
		Jedis j = pool.getResource();
		j.flushAll();
		pool.returnResource(j);
	}

	public void remove(String key) {
		Jedis j = pool.getResource();
		Transaction tx = j.multi();
		tx.del(key);
		tx.exec();
		pool.returnResource(j);
	}

	public boolean exist(String key) {
		Jedis j = pool.getResource();
		boolean res = j.exists(key);
		pool.returnResource(j);
		return res;
	}

	// 排序相关
	public void set(final String key, double weightValue, String keyString) {
		Jedis j = pool.getResource();
		j.zadd(key, weightValue, keyString);
		pool.returnResource(j);
	}

	/**
	 * 删除key 下面的members
	 * 
	 * @param key
	 * @param members
	 */
	public void zdel(final String key, String... members) {
		Jedis j = pool.getResource();
		j.zrem(key, members);
		pool.returnResource(j);
	}

	/**
	 * 前后索引都包含
	 * 
	 * @param key
	 * @param isAsce
	 *            升序
	 */
	public Set<String> zgetSortedKeys(final String key, boolean isAsce,
			int startIndex, int endIndex) {
		Jedis j = pool.getResource();

		Set<String> set = null;
		if (isAsce) {
			set = j.zrange(key, startIndex, endIndex);
		} else {
			set = j.zrevrange(key, startIndex, endIndex);
		}

		pool.returnResource(j);

		return set;

	}

}

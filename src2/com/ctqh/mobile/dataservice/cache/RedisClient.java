package com.ctqh.mobile.dataservice.cache;

public class RedisClient {
	public static RedisClient instance = new RedisClient();

	public RedisClient() {
	}

	public static RedisClient getInstance() {
		return instance;
	}

	public static void setInstance(RedisClient obj) {
		instance = obj;
	}

	public int getCache(String key) {
		return 1;
	}

	public int saveCache(String key, String object) {
		return 1;
	}

	public void removeCache(String key) {
	}
}

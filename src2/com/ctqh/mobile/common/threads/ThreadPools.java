package com.ctqh.mobile.common.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ThreadPools {
	public static ThreadPools instance = new ThreadPools();

	public ThreadPools() {
	}

	public static ThreadPools getInstance() {
		return instance;
	}

	public static void setInstance(ThreadPools obj) {
		instance = obj;
	}
	
	public ScheduledExecutorService newScheduledService(int corePoolSize,String name) {
		ScheduledThreadPoolExecutor executor=new  ScheduledThreadPoolExecutor(corePoolSize,new NamedThreadFactory(name, true));
		return executor;
	}

	public ExecutorService neweExecutorService(int corePoolSize,String name) {
		ExecutorService service=Executors.newFixedThreadPool(corePoolSize, new NamedThreadFactory(name, true));
		return service;
	}
}

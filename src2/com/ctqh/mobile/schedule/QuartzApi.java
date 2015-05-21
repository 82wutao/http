package com.ctqh.mobile.schedule;

import java.util.Set;

import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzApi {
	private final static Logger logger = LoggerFactory
			.getLogger(QuartzApi.class);

	private Scheduler sched = null;

	private QuartzApi() {
	}

	private static QuartzApi instance = new QuartzApi();

	public static QuartzApi getInstance() {
		return instance;
	}

	public void startup(Set<Class<?>> allClass){
		StdSchedulerFactory schefactory = new StdSchedulerFactory();
		Scheduler scheduler = null;
		try {
			scheduler = schefactory.getScheduler();
			if (scheduler.isShutdown()) {
				scheduler.start(); // 若关闭,启动
			}
		} catch (SchedulerException e) {
			logger.error("scheduler create error", e);
		}
		sched = scheduler;
		scanQuartzJobs(allClass);
	}

	@SuppressWarnings("rawtypes")
	private void createJob(String jobName, String expression, Class classs)
			throws Exception {
		// 任务名称，任务组名称，任务实现类
		JobDetail job = new JobDetail(jobName, Scheduler.DEFAULT_GROUP, classs);
		// 删除作业
		if (sched.getJobDetail(jobName, Scheduler.DEFAULT_GROUP) != null) {
			sched.deleteJob(jobName, Scheduler.DEFAULT_GROUP);
		}

		CronTrigger trigger = new CronTrigger(jobName, null);
		CronExpression cronExpression = new CronExpression(expression);
		trigger.setCronExpression(cronExpression);

		// 注册作业
		sched.scheduleJob(job, trigger);
		if (!sched.isShutdown()) {
			sched.start();
		}
	}

	public void shutdown() throws SchedulerException {
		sched.shutdown();
	}

	private void scanQuartzJobs(Set<Class<?>> allClass)  {
		try {
			for (Class<?> c : allClass) {
				QuartzJobAnn ann = c.getAnnotation(QuartzJobAnn.class);
				//不是定时任务类
				if (ann == null) {
					continue;
				}
				String jobName = ann.jobName();
				String cron = ann.cronExpression();
				createJob(jobName,cron,c);
			}
		} catch (Exception e) {
			logger.error("load jobs error",e);
		}
	}
}

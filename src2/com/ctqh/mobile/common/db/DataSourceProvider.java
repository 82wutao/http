package com.ctqh.mobile.common.db;

import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceProvider implements IDataSourceProvider {
	private final static Logger logger = LoggerFactory
			.getLogger(DataSourceProvider.class);
	protected Properties dbProps = null;
	protected DataSource dataSource;

	protected DataSource initDataSource(String urlKey) {
		BasicDataSource dataSource = new BasicDataSource();
		try {
			String driveClassName = dbProps.getProperty("jdbc.driverClassName");
			String username = dbProps.getProperty("jdbc.username");
			String password = dbProps.getProperty("jdbc.password");
			String url = dbProps.getProperty(urlKey);
			String initialSize = dbProps.getProperty("dataSource.initialSize");
			String minIdle = dbProps.getProperty("dataSource.minIdle");
			String maxIdle = dbProps.getProperty("dataSource.maxIdle");
			String maxWait = dbProps.getProperty("dataSource.maxWait");
			String maxActive = dbProps.getProperty("dataSource.maxActive");

			// 是否自动回收超时连接
			boolean removeAbandoned = (Boolean.valueOf(dbProps.getProperty(
					"dataSource.removeAbandoned", "false"))).booleanValue();

			// 超时时间(以秒数为单位)
			int removeAbandonedTimeout = Integer.parseInt(dbProps.getProperty(
					"dataSource.removeAbandonedTimeout", "300"));

			dataSource = new BasicDataSource();
			dataSource.setDriverClassName(driveClassName);
			dataSource.setUrl(url);
			dataSource.setUsername(username);
			dataSource.setPassword(password);

			// 初始化连接数
			if (initialSize != null)
				dataSource.setInitialSize(Integer.parseInt(initialSize));

			// 设置最小空闲连接
			if (minIdle != null)
				dataSource.setMinIdle(Integer.parseInt(minIdle));

			// 设置最大空闲连接
			if (maxIdle != null)
				dataSource.setMaxIdle(Integer.parseInt(maxIdle));

			// 超时回收时间(以毫秒为单位)
			if (maxWait != null)
				dataSource.setMaxWait(Long.parseLong(maxWait));

			// 最大活跃链接
			if (maxActive != null) {
				if (!maxActive.trim().equals("0"))
					dataSource.setMaxActive(Integer.parseInt(maxActive));
			}

			dataSource.setRemoveAbandoned(removeAbandoned);
			dataSource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
			dataSource.setValidationQuery("select 1");
			Connection conn = dataSource.getConnection();
			if (conn == null) {
				logger.error("创建连接池时,无法取得连接,请检查设置!!!");
			} else {
				conn.close();
			}
			logger.info("连接池创建成功!!");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("创建连接池失败请检查设置!!");
		}
		return dataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

}

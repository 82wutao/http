package com.ctqh.mobile.common.db.datasource;

import java.util.Properties;

import javax.sql.DataSource;

import com.ctqh.mobile.common.db.DataSourceProvider;

public class GameServerDS extends DataSourceProvider{
	public GameServerDS(Properties properties) {
		this.dbProps = properties;
		dataSource = super.initDataSource("server.jdbc.url");
	}
	public DataSource getDataSource() {
		return dataSource;
	}
}

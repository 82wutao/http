package com.ctqh.mobile.common.db.datasource;

import java.util.Properties;

import javax.sql.DataSource;

import com.ctqh.mobile.common.db.DataSourceProvider;

public class GameDataDS extends DataSourceProvider{
	
	public GameDataDS(Properties properties) {
		this.dbProps = properties;
		dataSource = super.initDataSource("data.jdbc.url");
	}

	public DataSource getDataSource() {
		return dataSource;
	}
}

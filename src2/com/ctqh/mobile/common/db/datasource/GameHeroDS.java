package com.ctqh.mobile.common.db.datasource;

import java.util.Properties;

import javax.sql.DataSource;

import com.ctqh.mobile.common.db.DataSourceProvider;

public class GameHeroDS extends DataSourceProvider{
	public GameHeroDS(Properties properties) {
		this.dbProps = properties;
		dataSource = super.initDataSource("hero.jdbc.url");
	}
	public DataSource getDataSource() {
		return dataSource;
	}
}

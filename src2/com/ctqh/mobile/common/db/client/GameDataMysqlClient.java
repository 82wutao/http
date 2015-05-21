package com.ctqh.mobile.common.db.client;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctqh.mobile.common.db.IDataSourceProvider;
import com.ctqh.mobile.common.db.MysqlClient;
import com.ctqh.mobile.common.db.datasource.GameDataDS;
import com.ctqh.mobile.common.file.PropertiesTools;

public class GameDataMysqlClient extends MysqlClient{
	private final static Logger logger = LoggerFactory.getLogger(GameDataMysqlClient.class);
	private static GameDataMysqlClient mysqlClient = null;
	public GameDataMysqlClient(IDataSourceProvider dataSourceProvider) {
		this.dataSource = dataSourceProvider.getDataSource();
	}
	
	public static GameDataMysqlClient getInstance() {
		if (mysqlClient == null) {
			Properties properties = PropertiesTools.loadProperties("/db.properties");
			mysqlClient = new GameDataMysqlClient(new GameDataDS(properties));
			logger.warn("init game data datasource success....");
		}
		return mysqlClient;
	}
	
}




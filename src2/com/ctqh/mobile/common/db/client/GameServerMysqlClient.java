package com.ctqh.mobile.common.db.client;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctqh.mobile.common.db.IDataSourceProvider;
import com.ctqh.mobile.common.db.MysqlClient;
import com.ctqh.mobile.common.db.datasource.GameServerDS;
import com.ctqh.mobile.common.file.PropertiesTools;

public class GameServerMysqlClient extends MysqlClient{
	private final static Logger logger = LoggerFactory.getLogger(GameServerMysqlClient.class);
	private static GameServerMysqlClient mysqlClient = null;
	public GameServerMysqlClient(IDataSourceProvider dataSourceProvider) {
		this.dataSource = dataSourceProvider.getDataSource();
	}
	
	public static GameServerMysqlClient getInstance() {
		if (mysqlClient == null) {
			Properties properties = PropertiesTools.loadProperties("/db.properties");
			mysqlClient = new GameServerMysqlClient(new GameServerDS(properties));
			logger.error("init game server datasource success....");
		}
		return mysqlClient;
	}
	
}




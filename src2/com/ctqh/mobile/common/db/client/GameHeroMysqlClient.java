package com.ctqh.mobile.common.db.client;

import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctqh.mobile.common.db.IDataSourceProvider;
import com.ctqh.mobile.common.db.MysqlClient;
import com.ctqh.mobile.common.db.MysqlDialect;
import com.ctqh.mobile.common.db.Record;
import com.ctqh.mobile.common.db.datasource.GameHeroDS;
import com.ctqh.mobile.common.file.PropertiesTools;
import com.ctqh.mobile.common.threads.NamedThreadFactory;
import com.ctqh.mobile.dataservice.database.DBData;

public class GameHeroMysqlClient extends MysqlClient{
	private final static Logger logger = LoggerFactory.getLogger(GameHeroMysqlClient.class);
	private static GameHeroMysqlClient mysqlClient = null;
	
//	private ExecutorService executor = Executors.newCachedThreadPool(new NamedThreadFactory("HeroDB", false));
	private ExecutorService executor = Executors.newFixedThreadPool(5,new NamedThreadFactory("HeroDB"));
	
	public GameHeroMysqlClient(IDataSourceProvider dataSourceProvider) {
		this.dataSource = dataSourceProvider.getDataSource();
	}
	
	public static GameHeroMysqlClient getInstance() {
		if (mysqlClient == null) {
			Properties properties = PropertiesTools.loadProperties("/db.properties");
			mysqlClient = new GameHeroMysqlClient(new GameHeroDS(properties));
			logger.error("init game hero datasource success....");
		}
		return mysqlClient;
	}
	
	public void asyncExecuteUpdate(String sql, Object... paras){
		final String _sql=sql;
		final Object[] _paras=paras;
		
		Runnable update= new Runnable() {
			public void run() {
				update(_sql, _paras);
			}
		};
		executor.execute(update);
	}
	public void asyncExecuteUpdate(String sql){
		asyncExecuteUpdate(sql, NULL_PARA_ARRAY);
	}
	public void asyncExecuteUpdate(final String tableName, final Record record){
		Runnable update= new Runnable() {
			public void run() {
				update(tableName, record);
			}
		};
		executor.execute(update);
	}
	
	public <T extends DBData> void  asyncExecuteUpdate(final String tableName, final String primaryKey, final Collection<T> records){
		Runnable update= new Runnable() {
			public void run() {
				update(tableName,primaryKey,records);
			}
		};
		executor.execute(update);
	}
	
	public void asyncExecuteDelete(String tableName, Object id){
		asyncExecuteDelete(tableName, MysqlDialect.getInstance().getDefaultPrimaryKey(), id);
	}
	public void asyncExecuteDelete(String tableName, String primaryKey, Object id){
		String sql = MysqlDialect.getInstance().forDbDeleteById(tableName, primaryKey);
		asyncExecuteUpdate(sql, id);
	}
	public void asyncExecuteDelete(String tableName, String primaryKey, Record record){
		asyncExecuteDelete(tableName, primaryKey, record.get(primaryKey));
	}
	public void asyncExecuteDelete(String tableName, Record record){
		String defaultPrimaryKey = MysqlDialect.getInstance().getDefaultPrimaryKey();
		asyncExecuteDelete(tableName, defaultPrimaryKey, record.get(defaultPrimaryKey));
	}
	public void asyncExecuteSave(String tableName, String primaryKey, Record record){
		final String _table=tableName;
		final String _pk=primaryKey;
		final Record _record=record;
		Runnable save= new Runnable() {
			public void run() {
				save(_table,_pk, _record);
			}
		};
		executor.execute(save);
	}
	
	public void asyncExecuteSave(String tableName, Record record){
		asyncExecuteSave(tableName, MysqlDialect.getInstance().getDefaultPrimaryKey(), record);
	}
	
	public void releaseThreadPool(){
		executor.shutdown();
	}
}





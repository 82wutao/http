package com.ctqh.mobile.dataservice.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MysqlClient {
	private final static Logger logger = LoggerFactory
			.getLogger(MysqlClient.class);
	private final static Logger badSqls = LoggerFactory
			.getLogger("badSqlsLogger");
	
	private DataSource dataSource;
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	private Connection getConnection() throws SQLException{
		Connection connection=dataSource.getConnection();
		connection.setAutoCommit(true);
		return connection;
	}
	private void executSql(String sql)throws SQLException{
		Connection connection=null;
		try {
			connection=getConnection();
			Statement statement=connection.createStatement();
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			badSqls.info(sql);
			logger.error(e.getMessage(), e);
			throw e;
		}finally{
			try {
				connection.close();
			} catch (SQLException e) {
			}
		}
	}
	public void insert(String sql) throws SQLException {
		executSql(sql);
	}

	public void delete(String sql)throws SQLException  {
		executSql(sql);
	}

	public void update(String sql) throws SQLException {
		executSql(sql);
	}
	
	public int[] execBatch(List<String> sqls) throws SQLException{
		Connection connection=null;
		int[] results=null;		
		
		try {
			connection=getConnection();
			Statement statement=connection.createStatement();
			for (String sql:sqls) {
				statement.addBatch(sql);
			}
			results=statement.executeBatch();
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
			for (String sql:sqls) {
				badSqls.info(sql);
			}
			throw e;
		}finally{
			try {
				connection.close();
			} catch (SQLException e) {
			}
		}
		return results;
	}

	public <T> List<T> query(Class<T> clazz,String querySQL,Data2Instance<T> construction) throws SQLException {
				
		Connection connection =null;
		List<T> instances =new ArrayList<T>();
		try {
			connection= getConnection();
			ResultSet resultSet=connection.createStatement().executeQuery(querySQL);
			
			Map<String, Object> fields=new HashMap<String, Object>();
			while (resultSet.next()) {
				for (int i = 0; i < construction.fieldNames.size(); i++) {
					Object field=resultSet.getObject(construction.fieldNames.get(i));
					fields.put(construction.fieldNames.get(i), field);
				}
				
				T t =construction.data2Instance(fields);
				
				instances.add(t);
			}
			
			resultSet.close();
		} finally {
			connection.close();
		}

		return instances;
	}
	
	public <T> List<T> query(String[] columns,String tableName,String where,Data2Instance<T> construction) throws SQLException {
		String query_columns = columns[0];
		for (int i = 1; i < columns.length; i++) {
			query_columns = query_columns + " , " + columns[i];
		}

		String sql = null;
		if (where == null) {
			sql = "select " + query_columns + " from " + tableName;
		} else {
			sql = "select " + query_columns + " from " + tableName + " where "
					+ where;
		}

		Connection connection = null;
		List<T> instances = new ArrayList<T>();
		try {
			connection = getConnection();
			ResultSet resultSet = connection.createStatement()
					.executeQuery(sql);

			Map<String, Object> fields = new HashMap<String, Object>();
			while (resultSet.next()) {
				for (int i = 0; i < construction.fieldNames.size(); i++) {
					Object field = resultSet.getObject(construction.fieldNames
							.get(i));
					fields.put(construction.fieldNames.get(i), field);
				}

				T t = construction.data2Instance(fields);

				instances.add(t);
			}

			resultSet.close();
		} finally {
			connection.close();
		}

		return instances;
	}
	
	public static MysqlClient dataClient=new MysqlClient();
	public static MysqlClient heroClient=new MysqlClient();
	public static MysqlClient getGameDataClient(){
		return dataClient;
	}
	public static MysqlClient getHeroDataClient(){
		return heroClient;
	}
}

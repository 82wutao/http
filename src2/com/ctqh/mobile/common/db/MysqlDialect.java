package com.ctqh.mobile.common.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.ctqh.mobile.dataservice.database.DBData;

/**
 * MysqlDialect.
 */
public class MysqlDialect{
	private static MysqlDialect instance = new MysqlDialect();
	private MysqlDialect() {
	}
	public static MysqlDialect getInstance(){
		return instance;
	}
	public String forTableBuilderDoBuild(String tableName) {
		return "select * from `" + tableName + "` where 1 = 2";
	}
	
	public void forModelSave(String tableName,Map<String, Object> attrs, StringBuilder sql, List<Object> paras) {
		sql.append("insert into `").append(tableName).append("`(");
		StringBuilder temp = new StringBuilder(") values(");
		for (Entry<String, Object> e: attrs.entrySet()) {
			String colName = e.getKey();
			if (paras.size() > 0) {
				sql.append(", ");
				temp.append(", ");
			}
			sql.append("`").append(colName).append("`");
			temp.append("?");
			paras.add(e.getValue());
		}
		sql.append(temp.toString()).append(")");
	}
	
	public String forModelDeleteById(String tableName) {
		StringBuilder sql = new StringBuilder(45);
		sql.append("delete from `");
		sql.append(tableName);
		sql.append("` where `").append(getDefaultPrimaryKey()).append("` = ?");
		return sql.toString();
	}
	
	public void forModelUpdate(String tableName, Map<String, Object> attrs, Set<String> modifyFlag, String primaryKey, Object id, StringBuilder sql, List<Object> paras) {
		sql.append("update `").append(tableName).append("` set ");
		for (Entry<String, Object> e : attrs.entrySet()) {
			String colName = e.getKey();
			if (!primaryKey.equalsIgnoreCase(colName) && modifyFlag.contains(colName)) {
				if (paras.size() > 0)
					sql.append(", ");
				sql.append("`").append(colName).append("` = ? ");
				paras.add(e.getValue());
			}
		}
		sql.append(" where `").append(primaryKey).append("` = ?");	// .append(" limit 1");
		paras.add(id);
	}
	
	public String forModelFindById(String tableName, String columns) {
		StringBuilder sql = new StringBuilder("select ");
		if (columns.trim().equals("*")) {
			sql.append(columns);
		}
		else {
			String[] columnsArray = columns.split(",");
			for (int i=0; i<columnsArray.length; i++) {
				if (i > 0)
					sql.append(", ");
				sql.append("`").append(columnsArray[i].trim()).append("`");
			}
		}
		sql.append(" from `");
		sql.append(tableName);
		sql.append("` where `").append(getDefaultPrimaryKey()).append("` = ?");
		return sql.toString();
	}
	
	public String forDbFindById(String tableName, String primaryKey, String columns) {
		StringBuilder sql = new StringBuilder("select ");
		if (columns.trim().equals("*")) {
			sql.append(columns);
		}
		else {
			String[] columnsArray = columns.split(",");
			for (int i=0; i<columnsArray.length; i++) {
				if (i > 0)
					sql.append(", ");
				sql.append("`").append(columnsArray[i].trim()).append("`");
			}
		}
		sql.append(" from `");
		sql.append(tableName.trim());
		sql.append("` where `").append(primaryKey).append("` = ?");
		return sql.toString();
	}
	
	public String forDbDeleteById(String tableName, String primaryKey) {
		StringBuilder sql = new StringBuilder("delete from `");
		sql.append(tableName.trim());
		sql.append("` where `").append(primaryKey).append("` = ?");
		return sql.toString();
	}
	
	public void forDbSave(StringBuilder sql, List<Object> paras, String tableName, Record record) {
		sql.append("insert into `");
		sql.append(tableName.trim()).append("`(");
		StringBuilder temp = new StringBuilder();
		temp.append(") values(");
		
		for (Entry<String, Object> e: record.getColumns().entrySet()) {
			if (paras.size() > 0) {
				sql.append(", ");
				temp.append(", ");
			}
			sql.append("`").append(e.getKey()).append("`");
			temp.append("?");
			paras.add(e.getValue());
		}
		sql.append(temp.toString()).append(")");
	}
	public <T extends DBData> void forDbSave(StringBuilder sql, List<Object[]> paras, String tableName, Collection<T> records) {
		if (records.isEmpty()) {
			return ;
		}
		sql.append("insert into `");
		sql.append(tableName.trim()).append("`(");
		
		StringBuilder temp = new StringBuilder();
		temp.append(") values(");
		
		Iterator<T> iterator=records.iterator();
		Record record=iterator.next().getRecord();
		int clmnSize=record.getColumns().size();
		int clmnIndex=0;
		
		Object[] paramValues=new Object[clmnSize];
		for (Entry<String, Object> e: record.getColumns().entrySet()) {
			if (clmnIndex > 0) {
				sql.append(", ");
				temp.append(", ");
			}
			sql.append("`").append(e.getKey()).append("`");
			temp.append("?");
			paramValues[clmnIndex]=e.getValue();
			clmnIndex++;
		}
		paras.add(paramValues);
		sql.append(temp.toString()).append(")");
		
		while ( iterator.hasNext()) {
			record=iterator.next().getRecord();
			clmnIndex=0;
			paramValues=new Object[clmnSize];
			for (Entry<String, Object> e: record.getColumns().entrySet()) {
				paramValues[clmnIndex]=e.getValue();
				clmnIndex++;
			}
			paras.add(paramValues);
		}
	}
	
	
	public <T extends DBData> void forDbUpdate(StringBuilder sql,  List<Object[]> paras, String tableName, String primaryKey, Collection<T> records) {
		if (records.isEmpty()) {
			return ;
		}
		sql.append("update `").append(tableName.trim()).append("` set ");
		
		Iterator<T> iterator=records.iterator();
		Record record=iterator.next().getRecord();
		int clmnSize=record.getColumns().size();
		int clmnIndex=0;
		
		Object[] paramValues=new Object[clmnSize];

		for (Entry<String, Object> e: record.getColumns().entrySet()) {
			String colName = e.getKey();
			if (!primaryKey.equalsIgnoreCase(colName)) {
				if (clmnIndex > 0) {
					sql.append(", ");
				}
				sql.append("`").append(colName).append("` = ? ");
				paramValues[clmnIndex]=e.getValue();
				clmnIndex++;
			}
		}
		paramValues[clmnIndex]=record.get(primaryKey);
		paras.add(paramValues);
		sql.append(" where `").append(primaryKey).append("` = ?");
		
		while ( iterator.hasNext()) {
			record=iterator.next().getRecord();
			clmnIndex=0;
			paramValues=new Object[clmnSize];
			for (Entry<String, Object> e: record.getColumns().entrySet()) {
				String colName = e.getKey();
				if (!primaryKey.equalsIgnoreCase(colName)) {
					paramValues[clmnIndex]=e.getValue();
					clmnIndex++;
				}
			}
			paramValues[clmnIndex]=record.get(primaryKey);
			paras.add(paramValues);
		}
	}
	
	public void forDbUpdate(String tableName, String primaryKey, Object id, Record record, StringBuilder sql, List<Object> paras) {
		sql.append("update `").append(tableName.trim()).append("` set ");
		for (Entry<String, Object> e: record.getColumns().entrySet()) {
			String colName = e.getKey();
			if (!primaryKey.equalsIgnoreCase(colName)) {
				if (paras.size() > 0) {
					sql.append(", ");
				}
				sql.append("`").append(colName).append("` = ? ");
				paras.add(e.getValue());
			}
		}
		sql.append(" where `").append(primaryKey).append("` = ?");	// .append(" limit 1");
		paras.add(id);
	}
	
	public void forPaginate(StringBuilder sql, int pageNumber, int pageSize, String select, String sqlExceptSelect) {
		int offset = pageSize * (pageNumber - 1);
		sql.append(select).append(" ");
		sql.append(sqlExceptSelect);
		// limit can use one or two '?' to pass paras
		sql.append(" limit ").append(offset).append(", ").append(pageSize);	
	}
	public String getDefaultPrimaryKey() {
		return "f_id";
	}

	public void fillStatement(PreparedStatement pst, List<Object> paras)
			throws SQLException {
		for (int i = 0, size = paras.size(); i < size; i++) {
			pst.setObject(i + 1, paras.get(i));
		}
	}

	public void fillStatement(PreparedStatement pst, Object... paras)
			throws SQLException {
		for (int i = 0; i < paras.length; i++) {
			pst.setObject(i + 1, paras[i]);
		}
	}
}

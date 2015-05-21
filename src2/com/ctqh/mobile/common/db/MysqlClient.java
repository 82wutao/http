package com.ctqh.mobile.common.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctqh.mobile.dataservice.database.DBData;


public abstract class MysqlClient {
	private final static Logger logger = LoggerFactory.getLogger(MysqlClient.class);
	protected boolean isShowSql = true;
	protected static final Object[] NULL_PARA_ARRAY = new Object[0];
	protected DataSource dataSource = null;
	protected IdAllocation idAllocation = null;
	public Connection getConnection(){
		try {
			return isShowSql ? new SqlReporter(dataSource.getConnection()).getConnection() : dataSource.getConnection();
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	public void setIdAllocation(IdAllocation idAllocation) {
		this.idAllocation = idAllocation;
	}
	/**
	 * Execute sql update
	 */
	public int update(String sql, Object... paras) {
		PreparedStatement pst;
		int result = 0;
		Connection connection = null;
		try {
			connection = getConnection();
			pst = connection.prepareStatement(sql);
			MysqlDialect.getInstance().fillStatement(pst, paras);
			result = pst.executeUpdate();
			close(pst);
		} catch (SQLException e) {
			throw new ActiveRecordException(e);
		}finally{
			close(connection);			
		}
		return result;
	}
	
	/**
	 * @see #update(String, Object...)
	 * @param sql an SQL statement
	 */
	public int update(String sql) {
		return update(sql, NULL_PARA_ARRAY);
	}
	
//	/**
//	 * Get id after insert method getGeneratedKey().
//	 */
//	private Object getGeneratedKey(PreparedStatement pst) throws SQLException {
//		ResultSet rs = pst.getGeneratedKeys();
//		Object id = null;
//		if (rs.next())
//			 id = rs.getObject(1);
//		rs.close();
//		return id;
//	}
	
	public List<Record> find(String sql, Object... paras){
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement pst = connection.prepareStatement(sql);
			MysqlDialect.getInstance().fillStatement(pst, paras);
			ResultSet rs = pst.executeQuery();
			List<Record> result = RecordBuilder.build(rs);
			close(rs, pst);
			return result;
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		}finally{
			close(connection);
		}
	}
	
	
	/**
	 * @see #find(String, String, Object...)
	 * @param sql the sql statement
	 */
	public List<Record> find(String sql) {
		return find(sql, NULL_PARA_ARRAY);
	}
	
	/**
	 * Find first record. I recommend add "limit 1" in your sql.
	 * @param sql an SQL statement that may contain one or more '?' IN parameter placeholders
	 * @param paras the parameters of sql
	 * @return the Record object
	 */
	public Record findFirst(String sql, Object... paras) {
		List<Record> result = find(sql, paras);
		return result.size() > 0 ? result.get(0) : null;
	}
	
	/**
	 * @see #findFirst(String, Object...)
	 * @param sql an SQL statement
	 */
	public Record findFirst(String sql) {
		List<Record> result = find(sql, NULL_PARA_ARRAY);
		return result.size() > 0 ? result.get(0) : null;
	}
	
	/**
	 * Find record by id.
	 * Example: Record user = DbPro.use().findById("user", 15);
	 * @param tableName the table name of the table
	 * @param idValue the id value of the record
	 */
	public Record findById(String tableName, Object idValue) {
		return findById(tableName, "f_id", idValue, "*");
	}
	
	/**
	 * Find record by id. Fetch the specific columns only.
	 * Example: Record user = DbPro.use().findById("user", 15, "name, age");
	 * @param tableName the table name of the table
	 * @param idValue the id value of the record
	 * @param columns the specific columns separate with comma character ==> ","
	 */
	public Record findById(String tableName, Number idValue, String columns) {
		return findById(tableName, "f_id", idValue, columns);
	}
	
	/**
	 * Find record by id.
	 * Example: Record user = DbPro.use().findById("user", "user_id", 15);
	 * @param tableName the table name of the table
	 * @param primaryKey the primary key of the table
	 * @param idValue the id value of the record
	 */
	public Record findById(String tableName, String primaryKey, Number idValue) {
		return findById(tableName, primaryKey, idValue, "*");
	}
	
	/**
	 * Find record by id. Fetch the specific columns only.
	 * Example: Record user = DbPro.use().findById("user", "user_id", 15, "name, age");
	 * @param tableName the table name of the table
	 * @param primaryKey the primary key of the table
	 * @param idValue the id value of the record
	 * @param columns the specific columns separate with comma character ==> ","
	 */
	public Record findById(String tableName, String primaryKey, Object idValue, String columns) {
		String sql = MysqlDialect.getInstance().forDbFindById(tableName, primaryKey, columns);
		List<Record> result = find(sql, idValue);
		return result.size() > 0 ? result.get(0) : null;
	}
	
	/**
	 * Delete record by id.
	 * Example: boolean succeed = DbPro.use().deleteById("user", 15);
	 * @param tableName the table name of the table
	 * @param id the id value of the record
	 * @return true if delete succeed otherwise false
	 */
	public boolean deleteById(String tableName, Object id) {
		return deleteById(tableName, MysqlDialect.getInstance().getDefaultPrimaryKey(), id);
	}
	
	/**
	 * Delete record by id.
	 * Example: boolean succeed = DbPro.use().deleteById("user", "user_id", 15);
	 * @param tableName the table name of the table
	 * @param primaryKey the primary key of the table
	 * @param id the id value of the record
	 * @return true if delete succeed otherwise false
	 */
	public boolean deleteById(String tableName, String primaryKey, Object id) {
		if (id == null)
			throw new IllegalArgumentException("id can not be null");
		
		String sql = MysqlDialect.getInstance().forDbDeleteById(tableName, primaryKey);
		return update(sql, id) >= 1;
	}
	
	/**
	 * Delete record.
	 * Example: boolean succeed = DbPro.use().delete("user", "id", user);
	 * @param tableName the table name of the table
	 * @param primaryKey the primary key of the table
	 * @param record the record
	 * @return true if delete succeed otherwise false
	 */
	public boolean delete(String tableName, String primaryKey, Record record) {
		return deleteById(tableName, primaryKey, record.get(primaryKey));
	}
	
	/**
	 * Example: boolean succeed = DbPro.use().delete("user", user);
	 * @see #delete(String, String, Record)
	 */
	public boolean delete(String tableName, Record record) {
		String defaultPrimaryKey = MysqlDialect.getInstance().getDefaultPrimaryKey();
		return deleteById(tableName, defaultPrimaryKey, record.get(defaultPrimaryKey));
	}
	
	
	public boolean save(String tableName, String primaryKey, Record record){
		Connection connection = null;
		try {
			connection = getConnection();
			List<Object> paras = new ArrayList<Object>();
			StringBuilder sql = new StringBuilder();
			MysqlDialect.getInstance().forDbSave(sql, paras, tableName, record);
			
			PreparedStatement pst = connection.prepareStatement(sql.toString());
			//PreparedStatement pst = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			
			MysqlDialect.getInstance().fillStatement(pst, paras);
			int result = pst.executeUpdate();
			//record.set(primaryKey, getGeneratedKey(pst));
			close(pst);
			return result >= 1;
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		}finally{
			close(connection);
		}
	}
	
	public <T extends DBData> boolean save(String tableName,Collection<T> recordList){
		for (Iterator<T> iterator = recordList.iterator(); iterator.hasNext();) {
			T t = iterator.next();
			String simpleName=t.getClass().getSimpleName();
			Record record=t.getRecord();
			if (record.get(t.getPrimaryKey()) == null) {
				int id =idAllocation.allocIdByClass(simpleName);
				record.set(t.getPrimaryKey(), id);
			}
		}
		Connection connection = null;
		try {
			connection = getConnection();
			List<Object[]> paras = new ArrayList<Object[]>();
			StringBuilder sql = new StringBuilder();
			MysqlDialect.getInstance().forDbSave(sql, paras, tableName, recordList);
			
			PreparedStatement pst = connection.prepareStatement(sql.toString());
			//PreparedStatement pst = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			
			for (Object[] param:paras) {
				for (int i=0;i<param.length;i++) {
					pst.setObject(i+1, param[i]);
				}
				pst.addBatch();
			}
			
//			MysqlDialect.getInstance().fillStatement(pst, paras);
//			int result = pst.executeUpdate();
			int[] ret=pst.executeBatch();
			//record.set(primaryKey, getGeneratedKey(pst));
			close(pst);
			
			for (int r :ret) {
				if (r==PreparedStatement.EXECUTE_FAILED ) {
					return false;
				}
			}
			
			return true;
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		}finally{
			close(connection);
		}
	}
	
	
	/**
	 * @see #save(String, String, Record)
	 */
	public boolean save(String tableName, Record record) {
		return save(tableName, MysqlDialect.getInstance().getDefaultPrimaryKey(), record);
	}
	
	public boolean update(String tableName, String primaryKey, Record record){
		Object id = record.get(primaryKey);
		if (id == null)
			throw new ActiveRecordException("You can't update model without Primary Key.");
		
		StringBuilder sql = new StringBuilder();
		List<Object> paras = new ArrayList<Object>();
		MysqlDialect.getInstance().forDbUpdate(tableName, primaryKey, id, record, sql, paras);
		
		if (paras.size() <= 1) {	// Needn't update
			return false;
		}
		
		return update(sql.toString(), paras.toArray()) >= 1;
	}
	
	
	public <T extends DBData> boolean  update(String tableName, String primaryKey, Collection<T> records){
		for(DBData record:records){
			Object id = record.getRecord().get(primaryKey);
			if (id == null)
				throw new ActiveRecordException("You can't update model without Primary Key.");
		}
		
		Connection connection = null;
		try {
			connection = getConnection();
			List<Object[]> paras = new ArrayList<Object[]>();
			StringBuilder sql = new StringBuilder();
			MysqlDialect.getInstance().forDbUpdate(sql, paras, tableName, primaryKey, records);
			
			PreparedStatement pst = connection.prepareStatement(sql.toString());
			//PreparedStatement pst = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			
			for (Object[] param:paras) {
				for (int i=0;i<param.length;i++) {
					pst.setObject(i+1, param[i]);
				}
				pst.addBatch();
			}
			
//			MysqlDialect.getInstance().fillStatement(pst, paras);
//			int result = pst.executeUpdate();
			int[] ret=pst.executeBatch();
			//record.set(primaryKey, getGeneratedKey(pst));
			close(pst);
			
			for (int r :ret) {
				if (r==PreparedStatement.EXECUTE_FAILED ) {
					return false;
				}
			}
			
			return true;
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		}finally{
			close(connection);
		}
	}
	
	/**
	 * Update Record. The primary key of the table is: "id".
	 * @see #update(String, String, Record)
	 */
	public boolean update(String tableName, Record record) {
		return update(tableName, MysqlDialect.getInstance().getDefaultPrimaryKey(), record);
	}

	public int[] batch(String sql, Object[][] paras, int batchSize) throws SQLException {
		if (paras == null || paras.length == 0)
			throw new IllegalArgumentException("The paras array length must more than 0.");
		if (batchSize < 1)
			throw new IllegalArgumentException("The batchSize must more than 0.");
		int counter = 0;
		int pointer = 0;
		int[] result = new int[paras.length];
		Connection conn = dataSource.getConnection(); 
		PreparedStatement pst = conn.prepareStatement(sql);
		for (int i=0; i<paras.length; i++) {
			for (int j=0; j<paras[i].length; j++) {
				pst.setObject(j + 1, paras[i][j]);	
			}
			pst.addBatch();
			if (++counter >= batchSize) {
				counter = 0;
				int[] r = pst.executeBatch();
				conn.commit();
				for (int k=0; k<r.length; k++){
					result[pointer++] = r[k];
				}
			}
		}
		int[] r = pst.executeBatch();
		conn.commit();
		for (int k=0; k<r.length; k++){
			result[pointer++] = r[k];
		}
		close(pst);
		close(conn);
		return result;
	}
	private static final void close(ResultSet rs, Statement st) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
			}
		}
	}

	private static final void close(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
			}
		}
	}

	private static final void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
			}
		}
	}
}

package com.ctqh.mobile.dataservice.database;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class StaticDataMnger<T extends StaticData> {
	protected Map<Integer, T> dataMap = new HashMap<Integer, T>();
	protected Collection<T> dataList=null;
	public void init(Class<T> clazz,Data2Instance<T> construction) throws SQLException{
		MysqlClient dataClient=MysqlClient.getGameDataClient();
		List<T> data =dataClient.query(clazz, null, construction);
		for (T t:data) {
			int key =t.getId();
			dataMap.put(key, t);
		}
		dataList = data;
	}

	public T getDataById(int id){
		return dataMap.get(id);
	}

	public Collection<T> getData(){
		return dataList;
	}
}

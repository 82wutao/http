package com.ctqh.mobile.dataservice.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctqh.mobile.common.KeyValue;
import com.ctqh.mobile.common.db.Record;


public abstract class DBData {
	public Record record = new Record();
	public abstract Record getRecord();
	/**
	 * 如果主键不是f_id,请自行重写此方法
	 * @return
	 */
	public String getPrimaryKey(){
		return "f_id";
	}
	public String toString(){
		return record.toJson();
	}
	
	public JSONObject getJson(String columu){
		Object obj = record.get(columu);
		if (obj instanceof String) {
			return JSON.parseObject(obj.toString());
		}else{
			return null;
		}
	}
	public JSONArray getJsonArray(String columu){
		Object obj = record.get(columu);
		if (obj instanceof String) {
			return JSONArray.parseArray((String)obj);
		}else{
			return null;
		}
	}
	
	/**
	 * 用一个map，设置属性，不包括主键
	 * @param columns
	 */
	public void copyColumns(Map<String, Object> columns){
		columns.remove(getPrimaryKey());
		record.setColumns(columns);
	}
	/**
	 * 初始化数据，子类请重写此方法
	 */
	public void init(){
		
	}
	public int getFightPower(){
		return 0;
	}
	
	/**
	 * 解析策划配置的概率数据
	 * @param jsonString
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <V> List<KeyValue<Integer,V>> getKeyValue(String jsonString){
		List<KeyValue<Integer,V>> result = new ArrayList<KeyValue<Integer,V>>();
		JSONArray jsonArray = JSON.parseArray(jsonString);
		for (Object object : jsonArray) {
			if (object instanceof JSONObject) {
				JSONObject jo = (JSONObject)object;
				Set<Map.Entry<String, Object>> set = jo.entrySet();
				for (Map.Entry<String, Object> entry : set) {
					Integer key = Integer.parseInt(entry.getKey());
					V v = (V)entry.getValue();
					KeyValue<Integer,V> kv = new KeyValue<Integer, V>(key,v);
					result.add(kv);
				}
			}else if (object instanceof JSONArray) {
				JSONArray ja = (JSONArray)object;
				int size = ja.size();
				KeyValue<Integer,V> kv;
				V value = null;
				int key = Integer.parseInt(ja.get(0).toString());
				switch (size) {
				case 1:
					break;
				case 2:
					value = (V) ja.get(1);
					break;
				case 3:
					value = (V) ja.subList(1, size - 1);
					break;
				}
				kv = new KeyValue<Integer, V>(key,value);
				result.add(kv);
			}
		}
		
		return result;
	}
}

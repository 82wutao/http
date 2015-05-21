package com.ctqh.mobile.dataservice.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Data2Instance<T> {
	protected List<String> fieldNames=new ArrayList<String>();
	
	//初始fieldNames
	public abstract void init();
	public abstract T data2Instance(Map<String, Object> fields);
}

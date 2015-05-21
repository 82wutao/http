package com.ctqh.mobile.dataservice.database;


public abstract class UserData {
	public UserData() {
	}
	
	public abstract String insertSql();
	public abstract String deleteSql();
	public abstract String updateSql();
}

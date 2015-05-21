package com.ctqh.mobile.common.db;

public interface IdAllocation  {
	public int allocIdByClass(String systemName);
	public int allocIdByClass(Class<?> systemName);
}

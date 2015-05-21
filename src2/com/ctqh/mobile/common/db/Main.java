package com.ctqh.mobile.common.db;

import com.ctqh.mobile.common.db.client.GameHeroMysqlClient;



public class Main {
	public static void main(String[] args) throws Exception{
		GameHeroMysqlClient.getInstance();
		for (int i = 0; i < 10; i++) {
			long start = System.currentTimeMillis();
			GameHeroMysqlClient.getInstance().find("select * from db_hero");
			long end = System.currentTimeMillis();
			System.out.println(end-start);
			start = System.currentTimeMillis();
			GameHeroMysqlClient.getInstance().find("select * from db_account");
			end = System.currentTimeMillis();
			System.out.println(end-start);
			start = System.currentTimeMillis();
			GameHeroMysqlClient.getInstance().find("select * from db_heroItem");
			end = System.currentTimeMillis();
			System.out.println(end-start);
			start = System.currentTimeMillis();
			GameHeroMysqlClient.getInstance().find("select * from db_sysID");
			end = System.currentTimeMillis();
			System.out.println(end-start);
		}
	}
}

package main;

import java.io.IOException;

import common.log.AppLogger;
import http.HttpServer;

public class Main {
	public static void main(String[] args) throws IOException {
		AppLogger.initailLogs("debug", AppLogger.LogLvl.Debug,System.out);
		
		HttpServer server = new  HttpServer();
		server.start();
		
	}
}

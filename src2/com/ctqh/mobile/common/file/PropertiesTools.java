package com.ctqh.mobile.common.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesTools {
	private final static Logger logger = LoggerFactory
			.getLogger(PropertiesTools.class);

	public static Properties loadProperties(String configFile) {
		InputStream inputStream = PropertiesTools.class
				.getResourceAsStream(configFile);
		Properties p = new Properties();
		try {
			p.load(inputStream);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}finally{
			try {
				inputStream.close();
			} catch (IOException e) {
			}
		}
		return p;
	}
}

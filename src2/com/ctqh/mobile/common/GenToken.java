package com.ctqh.mobile.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenToken {
	private static String tokenKey = "test";
	private final static Logger logger = LoggerFactory.getLogger(GenToken.class);
	public static String getToken(String... p) {
		if (p == null || p.length == 0) {
			return "";
		}
		StringBuffer all = new StringBuffer();
		for (String string : p) {
			all.append(string);
		}
		all.append(tokenKey);
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(all.toString().getBytes());
			StringBuilder sb = new StringBuilder();
			byte [] tmp = md5.digest();
	        for (byte b:tmp) {
	            sb.append(Integer.toHexString(b&0xff));
	        }
	        return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			logger.error("md5 error",e);
		}
		return "";
	}
	
	public static void main(String[] args) {
		System.out.println(GenToken.getToken("321312"));
	}
}

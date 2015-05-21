package com.ctqh.mobile;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameSessionManager {
	private static Map<String, GameSession> sessions=new ConcurrentHashMap<String, GameSession>();
	
	public static GameSession getSession(String token) {
		return sessions.get(token);
	}
	public static void setSession(String token,GameSession session){
		session.token = token;
		sessions.put(token, session);
	}
	public static void removeSession(String token){
		sessions.remove(token);
	}
	public static Map<String, GameSession> getAll(){
		return sessions;
	}
}

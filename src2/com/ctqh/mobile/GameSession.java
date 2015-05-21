package com.ctqh.mobile;


import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctqh.mobile.common.protocol.MessageInput;
import com.ctqh.mobile.common.protocol.MessageOutput;

/**
 * @author wutao
 */
final public class GameSession{
	private static Logger logger = LoggerFactory.getLogger(GameSession.class);
	
	public static final int Session_Type_Game=1;
	public static final int Session_Type_Chat=2;
	public static final String Key_Account="key_account";
	
	private static final AttributeKey KEY_PLAYER_SESSION = new AttributeKey(
			GameSession.class, "session.player");
	
	private int sessionType;
	private int accountid=-1;
	private boolean validate;
	private long createTime ;
	
	private  Object gameHero = null;
	/**上次心跳时间戳,上次收到客户端消息的时间戳*/
	private  long lastTime = System.currentTimeMillis();
	
	private Queue<MessageInput> requests = null;
	private IoSession client = null;
	private String address = null;
	
	private boolean islogout ;
	private String platform;

	private Map<String, Object> attributes=new HashMap<String, Object>();
	
	public String token;
	private GameSession() {
		validate =false;
		sessionType = Session_Type_Game;
	}

	/**
	 * 为新的会话创建一个玩家对象。
	 * @param session
	 */
	public static void newPlayer4Session(IoSession session){
		GameSession player=new GameSession();
		player.createTime=System.currentTimeMillis();
		
		player.requests = new ConcurrentLinkedQueue<MessageInput>();
		player.client = session;
		
		SocketAddress socketaddress = session.getRemoteAddress();
		InetSocketAddress s = (InetSocketAddress) socketaddress;
		player.address=s.getAddress().getHostAddress();
		
		player.islogout=false;
		
		session.setAttribute(KEY_PLAYER_SESSION, player);
	}
	public static GameSession newPlayer4Http(){
		GameSession player=new GameSession();
		player.createTime=System.currentTimeMillis();
		
		player.requests = new ConcurrentLinkedQueue<MessageInput>();
		
		player.islogout=false;
		
		return player;
	}
	public static GameSession getGamePlayer(IoSession session) {
		Object playerObj = session.getAttribute(KEY_PLAYER_SESSION);
		return (GameSession) playerObj;
	}
	public static GameSession getGamePlayer(String key) {
		GameSession playerObj = GameSessionManager.getSession(key);
		return playerObj;
	}
	public static void removeIOSessionMap(GameSession gameSession) {
		if (gameSession.client != null) {
			gameSession.client.removeAttribute(KEY_PLAYER_SESSION);
			synchronized (gameSession.requests) {
				gameSession.requests.clear();
			}
		}
	}
	public static void removeIOSessionMap(String key){
		GameSessionManager.removeSession(key);
	}
	
	public final int getSessionType(){
		return sessionType;
	}
	public final void setSessionType(int type){
		sessionType = type;
	}
	
	public int getAccountid() {
		return accountid;
	}
	public void setAccountid(int accountid) {
		this.accountid = accountid;
	}

	public final boolean isValidate() {
		return validate;
	}
	/**
	 * 设置该会话已经通过验证
	 * @param isvalidate
	 */
	public final void setValidate(boolean v) {
		this.validate = v;
	}
	
	public long getCreateTime() {
		return createTime;
	}
	
	
	public <T> T getCurrentHero(Class<T> castType) {
		if (gameHero != null) {
			return castType.cast(gameHero);
		}
		return null;
	}
	public <T> void setCurrentHero(T hero) {
		gameHero = hero;
	}
	
	public long getLastTime() {
		return lastTime;
	}
	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}
	
	
	public boolean addRequest(MessageInput msg) {
		return requests.offer(msg);
	}
	public MessageInput executeRequest() {
		return requests.poll();

	}
	public void clearRequests() {
		synchronized (requests) {
			this.requests.clear();
		}
	}
	
	public void close() {
		try {
			client.close(false);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	public IoSession getIoSession() {
		return client;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public WriteFuture sendMsg(MessageOutput msg) {
		if (client == null || !client.isConnected() || client.isClosing()) {
			return null;
		}
		return client.write(msg);
	}
	
	public void logout() {
		synchronized (this) {
			if (islogout) {
				return;
			}
			
			islogout = true;
			try {
				client.close(true);
			} catch (Exception e) {
			}
		}
	}
	
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getPlatform() {
		return platform;
	}

public void setAttribute(String key,Object value){
	attributes.put(key, value);
}
public <T> T getAttribute(String key,Class<T> clazz){
	Object obj=attributes.get(key);
	if (obj==null) {
		return null;
	}
	T instance =clazz.cast(obj);
	return instance;
}
public void removeAttribute(String key){
	attributes.remove(key);
}

	@Override
	public int hashCode() {
		return accountid;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}

		if (!(object instanceof GameSession)) {
			return false;
		}
		if (object == this) {
			return true;
		}
		if (this.hashCode() == object.hashCode()) {
			return true;
		}
		return false;
	}


}

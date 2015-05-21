package com.ctqh.mobile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctqh.mobile.common.controller.DownlineController;
import com.ctqh.mobile.common.controller.ShutdownServerController;
import com.ctqh.mobile.common.controller.StartServerController;
import com.ctqh.mobile.common.file.PropertiesTools;
import com.ctqh.mobile.tcp.MsgQueueHandler;
import com.ctqh.mobile.tcp.NetworkConfig;
import com.ctqh.mobile.tcp.TcpService;

public class GameServer {
	private final static Logger logger = LoggerFactory
			.getLogger(GameServer.class);
	public static String masterServer="master";
	public static String mainServer="main";
	public static Map<String,GameServer> allServer = new HashMap<String, GameServer>();
	public GameServer() {
	}

	/**
	 * 得到得到server
	 * @param server master | main
	 * @return
	 */
	public static GameServer getServer(String serverName) {
		GameServer gs = allServer.get(serverName);
		if (gs == null) {
			gs = new GameServer();
			allServer.put(serverName, gs);
		}
		return gs;
	}

//	protected boolean shutdowning = false;
	protected Properties serverProperties=new Properties();
	protected TcpService tcpService =null;
	
//	private Set<GameSession> unvalidatedSessions = new ConcurrentHashSet<GameSession>();
//	private Map<Integer, GameSession> validatedSessions = new ConcurrentHashMap<Integer, GameSession>();
	private DownlineController downlineController=null;
	private StartServerController startServerController=null;
	private ShutdownServerController shutdownServerController=null;
//	public boolean isShutdowning() {
//		return shutdowning;
//	}

	public void shutdown() {
//		shutdowning = true;
		shutdownServerController.shutdown();
		tcpService.stopNetListen();
		tcpService.destroy();
	}

	public void start(String configFile) throws IOException {
		serverProperties.clear();
		serverProperties = PropertiesTools.loadProperties(configFile);
		
		startServerController.start();
//		shutdowning = false;		
		
		String loginPort = this.serverProperties.getProperty("server.tcpport");
		tcpService = new TcpService(new NetworkConfig(Integer.parseInt(loginPort), new MsgQueueHandler(this)));// 启动网络IO
		tcpService.startNetListen();
		
//		Thread thread = new Thread(this,"poll-service");
//		thread.setDaemon(true);
//		thread.start();
	}

//	public void run() {
//		while (!shutdowning) {
//			BusinessDispatcher dispatcher = BusinessDispatcher.getInstance();
//			Collection<GameSession> sessions=GameSessionManager.getAll().values();
//			
//			for (Iterator<GameSession> iterator = sessions.iterator(); iterator.hasNext();) {
//				GameSession gameSession =  iterator.next();
//				MessageInput request =gameSession.executeRequest();
//				int msgcode = request.getMsgCode();
//				BusinessProcessor processor=dispatcher.getBusinessControlor(msgcode);
//				if (processor==null) {
//					logger.error("ip:{} account:{} business:{} is not exist",gameSession.getAddress(),gameSession.getAccountid(),msgcode);
//					continue;
//				}
//				
//				
//				try {
//					processor.process(gameSession, request);
//				} catch (Exception e) {
//					logger.error("account:{} business:{} error",e);
//				}
//			}
//			
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//
//		Collection<GameSession> sessions=GameSessionManager.getAll().values();
//		for (Iterator<GameSession> iterator = sessions.iterator(); iterator.hasNext();) {
//			GameSession gameSession =  iterator.next();
//			gameSession.clearRequests();
//		}
//	}

//	/***
//	 * 维护一个未通过验证的会话
//	 * 
//	 * @param session
//	 */
//	public void addSessionUnvalidated(GameSession session) {
//		unvalidatedSessions.add(session);
//	}
//
//	/**
//	 * 维护一个通过验证的会话
//	 * 
//	 * @param session
//	 */
//	public void addSessionValidated(GameSession session) {
//		unvalidatedSessions.remove(session);
//		validatedSessions.put(session.getAccountid(), session);
//		session.setValidate(true);
//	}

	/***
	 * 会话断开连接，通过验证与否在函数内部区分。
	 * 
	 * @param session
	 */
	public void sessionClose(GameSession session) {
		logger.error("{} going close",new Object[]{session.getAccountid()});
//		if (session.isValidate()) {
//			validatedSessions.remove(session.getAccountid());
//		} else {
//			unvalidatedSessions.remove(session);
//		}
		GameSessionManager.removeSession(session.token);
		this.downlineController.downline(session);
	}
	public void setServerProperty(String key,String value){
		serverProperties.setProperty(key,value);
	}
	public String getServerProperty(String key){
		return serverProperties.getProperty(key);
	}
	
	public void setDownlineController(DownlineController downlineController) {
		this.downlineController = downlineController;
	}

	public StartServerController getStartServerController() {
		return startServerController;
	}

	public void setStartServerController(StartServerController startServerController) {
		this.startServerController = startServerController;
	}

	public ShutdownServerController getShutdownServerController() {
		return shutdownServerController;
	}

	public void setShutdownServerController(
			ShutdownServerController shutdownServerController) {
		this.shutdownServerController = shutdownServerController;
	}
	
}

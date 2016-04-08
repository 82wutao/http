package common.log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class AppLogger {
	public enum LogLvl{
		Debug,Info,Warn,Err
	}
	LogLvl logLvl;
	Writer out;
	
	
	
	private static Map<String, AppLogger> map = new HashMap<String,AppLogger>();
	public static void initailLogs(String name,LogLvl lvl,Writer out){
		AppLogger logger = map.get(name);
		if (logger==null) {
			logger = new AppLogger();
			logger.out=out;		
			map.put(name,logger);
		}
		logger.logLvl=lvl;
	}
	
	public static AppLogger getLogger(String name ){
		AppLogger logger = map.get(name);
		if (logger==null) {
			logger = new AppLogger();
			logger.out=new BufferedWriter(new OutputStreamWriter(System.out));
			logger.logLvl=LogLvl.Debug;
			map.put(name,logger);
		}
		return logger;
	}
	
	public void log(LogLvl logLvl,String msg){
		if (logLvl.ordinal() < this.logLvl.ordinal()) {
			return ;
		}
		try {
			this.out.write(msg);
			this.out.write('\n');
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

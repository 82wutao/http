package app;

import java.io.IOException;

import app.terminal.Cmd;


public class Sql {
	public static Sql instance = new Sql();

	public Sql() {
	}

	public static Sql getInstance() {
		return instance;
	}

	public static void setInstance(Sql obj) {
		instance = obj;
	}

	public String executeSqlFile(String host,String user_passwd,String databse,String sqlFile) throws IOException, InterruptedException {
		String[] up=user_passwd.split("_");
		
		String execute="mysql -h"+host+" -u"+up[0]+" -p"+up[1]+" -D"+databse+" <"+sqlFile;
		String msg =Cmd.getInstance().execute(execute, "utf8");
		return msg;
	}
}

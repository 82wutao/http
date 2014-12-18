package app.terminal;

import io.IO;

import java.io.IOException;
import java.io.InputStream;

public class Cmd {
	public static Cmd instance = new Cmd();

	public Cmd() {
	}

	public static Cmd getInstance() {
		return instance;
	}

	public static void setInstance(Cmd obj) {
		instance = obj;
	}


	public String execute(String cmd,String outEncode) throws IOException, InterruptedException {
		Process process = Runtime.getRuntime().exec(cmd);

		process.waitFor();

		InputStream out = process.getInputStream();
		String outMsg = IO.getInstance().readMessageFromStream(out,outEncode);

		InputStream error = process.getErrorStream();
		String errorMsg = IO.getInstance().readMessageFromStream(error,outEncode);
		
		System.out.println("STD_OUT:\n"+outMsg+"\n\nSTD_ERR:\n"+errorMsg);
		return "STD_OUT:\n"+outMsg+"\n\nSTD_ERR:\n"+errorMsg;
	}

}

package common;

import java.io.IOException;
import java.io.InputStream;

import common.io.IO;

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

	public String execute(String cmd, String outEncode) throws IOException,
			InterruptedException {

		InputStream out = null;
		InputStream error = null;

		try {
			Process process = null;
			process = Runtime.getRuntime().exec(cmd);
			process.waitFor();

			out = process.getInputStream();
			error = process.getErrorStream();

			String outMsg = IO.getInstance().readMessageFromStream(out,
					outEncode);
			String errorMsg = IO.getInstance().readMessageFromStream(error,
					outEncode);

			String ret = null;
			if (outMsg != null) {
				ret = outMsg;
			}

			if (ret == null) {
				if (errorMsg != null) {
					ret = errorMsg;
				}
				return ret;
			}

			if (errorMsg != null) {
				ret = ret + "\n\n" + errorMsg;
			}
			return ret;
		} catch (IOException e) {
			throw e;
		} finally {
			if (out!=null) {
				out.close();
			}
			if(error!=null){
				error.close();
			}
		}

	}

}

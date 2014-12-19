package app.monitor;

import java.io.IOException;

import app.terminal.Cmd;

public class Module {
	public static Module instance = new Module();

	public Module() {
	}

	public static Module getInstance() {
		return instance;
	}

	public static void setInstance(Module obj) {
		instance = obj;
	}
	
	public Result getData(String param){
		Result result =new Result(param);
		
		if (param.equals("mem")) {
			calcMem(result);
		}else if (param.equals("numberofcores")) {
			calcCores(result);
		}else if (param.equals("loadavg")) {
			calcLoad(result);
		}
		else {
			otherModule(result);
		}
		
		
		return result;
	}
	
	private void calcMem(Result result){
			try {
				String out =Cmd.getInstance().execute("free -tmo | awk 'BEGIN {OFS=\",\"} {print $1,$2,$3-$6-$7,$4+$6+$7}'", "UTF8");
				String[] array=out.split("\n");
				result.setDataAsArray(array[1], " ");
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
				result.setError(e.getMessage());
			}
	}
	private void calcCores(Result result){
		try {
			String out =Cmd.getInstance().execute("grep -c ^processor /proc/cpuinfo", "UTF8");
			String[] array=out.split("\n");
			result.setDataAsInt(array[0]);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			result.setError(e.getMessage());
		}
	}
	private void calcLoad(Result result){
		try {
			String out =Cmd.getInstance().execute("grep -c ^processor /proc/cpuinfo", "UTF8");
			String[] array=out.split("\n");
			int num =Integer.parseInt(array[0]);
			
			
			out =Cmd.getInstance().execute("cat /proc/loadavg", "UTF8");
			array=out.split("\n");
			String[] loads=array[0].split(" ");

			out="[";
			out = out+"[\""+loads[0]+"\","+(int)(Float.parseFloat(loads[0])*100/num)+"]";
			for (int i = 1; i < 3; i++) {
				out = out+",[\""+loads[i]+"\","+(int)(Float.parseFloat(loads[i])*100/num)+"]";
			}
			out=out+"]";
			result.setData(out);
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			result.setError(e.getMessage());
		}
		
	}
	private void otherModule(Result result){
		result.setError("error");
	}

}

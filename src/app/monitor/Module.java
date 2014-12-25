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
		}else if (param.equals("df")) {
			calcDisk(result);
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
	private void calcDisk(Result result){
		/*
		 * {
		 * "module":"df",
		 * "data":
		 * 	[
		 * 		["\/dev\/mapper\/VolGroup-lv_root","50G","4.2G","43G","9%","\/"],
		 * 		["tmpfs","1.9G","240K","1.9G","1%","\/dev\/shm"],
		 * 		["\/dev\/sda2","485M","68M","392M","15%","\/boot"],
		 * 		["\/dev\/sda1","200M","260K","200M","1%","\/boot\/efi"],
		 * 		["\/dev\/mapper\/VolGroup-lv_home","405G","1.2G","384G","1%","\/home"]
		 * 	]
		 * }
		 * 
		 */
		try {
			String out =Cmd.getInstance().execute("df -Ph | awk \'BEGIN {OFS=\",\"} {print $1,$2,$3,$4,$5,$6}\'", "UTF8");
			System.out.println("\n"+out);
			String[] array=out.split("\n");
			
			

			out="[";
			
			String[] f=array[1].split(",");
			out = out+"[\""+f[0]+"\",\""+f[1]+"\",\""+f[2]+"\",\""+f[3]+"\",\""+f[4]+"\",\""+f[5]+"\"]";

			for (int i = 2; i < array.length; i++) {
				f=array[i].split(",");
				out = out+",[\""+f[0]+"\",\""+f[1]+"\",\""+f[2]+"\",\""+f[3]+"\",\""+f[4]+"\",\""+f[5]+"\"]";
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

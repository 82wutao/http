package app.monitor;

import http.api.HttpResponse;

public class Result {
	public String module=null;
	/**
	 * module":"mem","data":["Mem:","3811","881","2929"]}
	 */
	public String data=null;
	private String error=null;
public Result(String model) {
	module="\""+model+"\"";
}
	public void write2Response(HttpResponse response) {
		response.setStatusCode(200);
		response.setContentType("application/json; charset=UTF-8");
		
		String json="{\"module\":"+module;
		
		if (data!=null) {
			json=json+",\"data\":"+data;
		}
		
		if (error!=null) {
			json=json+",\"error\":"+error;
		}
		json=json+"}";
		System.out.println("json " +json);
		response.write(json);
	}
	
	public void setDataAsArray(String _data,String delimiter){
		String[] ele=_data.split(delimiter);
		//["Mem:","3811","881","2929"]
		String str="[";
		str = str +"\""+ele[0]+"\"";
		for (int i = 1; i < ele.length; i++) {
			if (ele[i].equals("")) {
				continue;
			}
			str = str +",\""+ele[i]+"\"";
		}
		str = str +"]";
		this.data = str;
	}
	public void setDataAsInt(String data) {
		this.data = data;
	}
	public void setDataAsString(String data) {
		this.data = "\""+data+"\"";
	}
	public void setError(String error) {
		this.error = "\""+error+"\"";
	}
	public void setData(String data){
		this.data =data;
	}
}


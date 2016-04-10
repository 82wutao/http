package app.msgstream.basicnode;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import app.msgstream.Message;
import app.msgstream.chains.Converter;
import app.msgstream.chains.FuncNode;
import app.msgstream.chains.NodeChain;

public class GroupNode<Input> extends FuncNode<Input,Map<Input, NodeChain>> {
	
	protected Map<Input, NodeChain> result=new HashMap<Input,NodeChain>();
	
	public GroupNode() {
	}
	public GroupNode(String name,String serializeFile,Converter<Input> converte,int calcFieldNum){
		super(name,FuncType.Group,serializeFile,converte,calcFieldNum);
	}
	
	@Override
	public void calc(Message msg,Input field) {
		NodeChain group =result.get(field);
		if(group ==null){
			group = this.factory.newChain(this.name+"."+field);
		}
		group.calc(msg);
		
	}
	public java.util.Map<Input,NodeChain> getResult() {
		return result;
	};
	
	@Override
	public void serialize() {
		//TODO serialize
	}
	@Override
	public void unserialize() {
		//TODO unserialize
	}

	public String briefing(){
		StringBuilder builder =new StringBuilder();
		builder.append('{');
		builder.append("name:\"").append(name).append("\",type:\"").append(type.name()).append("\",result:[");

		for(Entry<Input, NodeChain> entry:result.entrySet()){
			builder.append('\n').append(',');
			builder.append("{group:\"").append(entry.getKey().toString()).append("\",result:").append(entry.getValue().briefing());
			builder.append('}');
		}
		builder.append(']');
		builder.append('}');
		return briefing().toString();
	}
	public static class MinuteConverter implements Converter<String>{

		@Override
		public String convert(String field) {
			String[] datetime=field.split(":");
			String minute_str = datetime[1];
			int minute = Integer.parseInt(minute_str);
			
			int ten= minute/10;
	        int left = minute%10;
	        if(left<5){
	        	minute = ten*10;
	        }else{
	        	minute = ten*10+5;
	        }
	        field = datetime[0]+":"+minute+":00";
	        return field;
		}
	}
}

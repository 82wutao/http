package app.msgstream.chains;

import java.util.ArrayList;
import java.util.List;

import app.msgstream.Message;

public abstract class FuncNode<Input,Output> {
	public enum FuncType{
		Count,Sum,Average,Group,ConditionFilte;
	}
	
	public FuncNode() {
	}
	protected String name;
	protected FuncType type;
	protected int fieldNum;
	
	protected String file=null;
	@SuppressWarnings("rawtypes")
	protected List<FuncNode> depends=new ArrayList<FuncNode>();
	protected ChainFactory factory;
	protected Converter<Input> converter;
	
	public FuncNode(String name,FuncType type,
			String serializeFile,
			Converter<Input> converte,int calcFieldNum){
		this.name=name;
		this.type = type;
		file = serializeFile;
		this.converter = converte;
		this.fieldNum = calcFieldNum;
	}
	
	@SuppressWarnings("rawtypes")
	public void depend(FuncNode other){
		depends.add(other);
	}
	public void addChainFactory4Subcollection(ChainFactory chainFactory){
		this.factory = chainFactory;
	}
	
	public void executeCalc(Message msg){
		if (msg.fields==null
				||msg.fields.length==0) {
			return ;
		}
		String field = msg.fields[fieldNum];
		
		Input field_value = converter.convert(field);
		calc(msg, field_value);
	}
	public abstract void calc(Message msg,Input field);
	public abstract Output getResult();
	
	public abstract void serialize();
	public abstract void unserialize();
	public String briefing(){
		StringBuilder builder =new StringBuilder();
		builder.append('{');
		builder.append("name:\"").append(name).append("\",type:\"").append(type.name()).append("\",result:").append(getResult().toString());
		builder.append('}');
		return briefing().toString();
	}
	
}

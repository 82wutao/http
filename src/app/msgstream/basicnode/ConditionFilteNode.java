package app.msgstream.basicnode;

import app.msgstream.Message;
import app.msgstream.chains.Converter;
import app.msgstream.chains.FuncNode;
import app.msgstream.chains.NodeChain;

public abstract class ConditionFilteNode<Input> extends FuncNode<Input,NodeChain> {
	
	protected NodeChain result=null;
	
	public ConditionFilteNode(String name,String serializeFile,Converter<Input> converte,int calcFieldNum){
		super(name,FuncType.ConditionFilte,serializeFile,converte,calcFieldNum);
	}
	
	@Override
	public void calc(Message msg,Input field) {
		if (result == null) {
			result= factory.newChain(this.name+".$condition");
		}
		boolean ok = checkCondition(field);
		if (!ok) {
			return ;
		}
		
		result.calc(msg);
		
	}
	public abstract boolean checkCondition(Input inputValue);
	
	public NodeChain getResult() {
		return result;
	}
	
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
		builder.append("name:\"").append(name).append("\",type:\"").append(type.name()).append("\",result:").append(result.briefing());
		builder.append('}');
		return briefing().toString();
	}

}
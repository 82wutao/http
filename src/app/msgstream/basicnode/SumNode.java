package app.msgstream.basicnode;

import app.msgstream.Message;
import app.msgstream.chains.Converter;
import app.msgstream.chains.FuncNode;

public class SumNode extends FuncNode<Integer,Integer>{
	
	protected int result=0;

	public SumNode(String name,String serializeFile,
			Converter<Integer> converte,int calcFieldNum){
		super(name,FuncType.Sum,serializeFile,converte,calcFieldNum);
	}
	
	@Override
	public void calc(Message msg,Integer value) {
		result += value;
	}
	public Integer getResult() {
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
}

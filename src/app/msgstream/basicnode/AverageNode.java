package app.msgstream.basicnode;

import app.msgstream.Message;
import app.msgstream.chains.Converter;
import app.msgstream.chains.FuncNode;

public class AverageNode extends FuncNode<Integer,Float>{
	
	protected int count=0;
	protected int sum=0;
	
	protected float result=0;

	public AverageNode(String name,String serializeFile,
			Converter<Integer> converte,int calcFieldNum){
		super(name,FuncType.Average,serializeFile,converte,calcFieldNum);
	}
	
	@Override
	public void calc(Message msg,Integer value) {
		sum += value;
		count++;
		
		result = sum/count;
	}
	public Float getResult() {
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

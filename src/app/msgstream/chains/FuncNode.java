package app.msgstream.chains;

import java.util.ArrayList;
import java.util.List;

import app.msgstream.Message;

public abstract class FuncNode<Output> {
	
	public FuncNode() {
	}
	
	protected String file=null;
	
	@SuppressWarnings("rawtypes")
	List<FuncNode> depends=new ArrayList<FuncNode>();
	
	public FuncNode(String serializeFile){
		file = serializeFile;
	}
	
	@SuppressWarnings("rawtypes")
	public void depend(FuncNode other){
		depends.add(other);
	}
	
	public abstract void calc(Message msg);
	
	public abstract Output getResult();
	
	public abstract void serialize();
	public abstract void unserialize();
	
	
}

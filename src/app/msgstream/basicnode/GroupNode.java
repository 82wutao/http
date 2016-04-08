package app.msgstream.basicnode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.msgstream.Message;
import app.msgstream.chains.FuncNode;

public class GroupNode extends FuncNode<Map<String, java.util.List<Message>>> {
	protected int fieldNum;
	
	protected Map<String, List<Message>> result=new HashMap<String,List<Message>>();
	
	public GroupNode() {
	}
	public GroupNode(int groupByField){
		fieldNum = groupByField;
	}
	
	@Override
	public void calc(Message msg) {
		if (msg.fields==null
				||msg.fields.length==0) {
			return ;
		}
		
		String field = msg.fields[fieldNum];
		
		List<Message> group =result.get(field);
		if(group ==null){
			group = new ArrayList<Message>();
		}
		group.add(msg);
		
	}
	public java.util.Map<String,java.util.List<Message>> getResult() {
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

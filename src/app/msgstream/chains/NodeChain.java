package app.msgstream.chains;

import java.util.ArrayList;
import java.util.List;

import app.msgstream.Message;

public class NodeChain {
	@SuppressWarnings("rawtypes")
	private List<FuncNode> nodes = new ArrayList<FuncNode>();
	
	private String name;
	
	public NodeChain(String name) {
		this.name =name;
	}
	
	
	public <Input,Output> void addNode(FuncNode<Input,Output> node){
		nodes.add(node);
	}
	
	@SuppressWarnings("rawtypes")
	public void calc(Message message){
		for(FuncNode node:nodes){
			node.executeCalc(message);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public String briefing(){
		StringBuilder builder = new StringBuilder();
		builder.append('{').append('\n');
		builder.append("name:\"").append(name).append("\",\n");
		builder.append("results:[");
		
		 	for(FuncNode node:nodes){
		 		builder.append('\n').append(',').append(node.briefing());
		 	}
		builder.append(']');
		builder.append('}');
	
		return briefing().toString();
	}
	
}

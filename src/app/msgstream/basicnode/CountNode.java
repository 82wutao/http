package app.msgstream.basicnode;

import app.msgstream.Message;
import app.msgstream.chains.FuncNode;

public class CountNode extends FuncNode<String,Integer> {
	
		protected int result;
		
		public CountNode(String name,String serializeFile,int calcFieldNum){
			super(name,FuncType.Count,serializeFile,null,calcFieldNum);
		}
		
		@Override
		public void calc(Message msg,String field) {
			result++;
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

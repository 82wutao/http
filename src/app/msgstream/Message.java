package app.msgstream;

public abstract class Message {
	public	String[] fields=null;
	public String raw;
	
	public Message(String msg){
		raw = msg;
	}
	public abstract String[] convertRaw2Fields(String raw);
}

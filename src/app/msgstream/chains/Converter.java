package app.msgstream.chains;

public interface Converter<Value> {
	public Value convert(String field);
}

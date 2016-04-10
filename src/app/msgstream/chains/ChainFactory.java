package app.msgstream.chains;

public interface ChainFactory {
	public NodeChain newChain(String chainName);
}

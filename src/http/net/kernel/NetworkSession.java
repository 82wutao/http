package http.net.kernel;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
/**
 * 代表了一次网络会话。目前仅仅支持TCP连接。
 * @author wt
 *
 */
public class NetworkSession {
	ByteBuffer sendBuffer;
	List<ByteBuffer> sendingBuffers;

	public NetworkSession(int rcvBufferSize,int sendBufferSize) {
		sendBuffer=ByteBuffer.allocate(sendBufferSize);
		sendingBuffers=new ArrayList<ByteBuffer>();
		sendBuffer.limit(0);
	}
	
	SocketChannel connetion;
	SelectionKey selectionKey;
	public void setConnection(SocketChannel socket){
		connetion=socket;
	}
	SocketChannel getConnection(){
		return connetion;
	}
	
	
	public int read(ByteBuffer buffer,int off,int length) throws IOException{
//		if (off +length > buffer.length) {
//			throw new IOException("buffer's length is less than the sum of off and length");
//		}
		
//		InputStream inputStream=this.connetion.socket().getInputStream();
//		int readed = inputStream.read(buffer, off, length);
		long readed = this.connetion.read(buffer);
		return (int)readed;
	}
	/**
	 * @param channel
	 * @return
	 * @throws IOException
	 */
	int write(SocketChannel channel)throws IOException{
		for(Iterator<ByteBuffer> iterator=this.sendingBuffers.iterator();iterator.hasNext();){
			ByteBuffer buffer=iterator.next();
			int leng=buffer.limit();
			int writed=channel.write(buffer);
			if (writed==-1) {
				return -1;
			}
			if (leng==writed) {
				iterator.remove();
			}else {
				return leng-writed;
			}
			
		}
		return 0;
	}
	

	private Queue<XBuffer> responseMsgs = new ArrayDeque<XBuffer>();
	public void sendResponseMsg(XBuffer msg) {
		if (closed) {
			return ;
		}
		synchronized (responseMsgs) {
			responseMsgs.offer(msg);
		}
	}
	List<XBuffer> popResponseMsgs() {
		ArrayList<XBuffer> msgs = new ArrayList<XBuffer>();
		synchronized (responseMsgs) {
			msgs.addAll(responseMsgs);
			responseMsgs.clear();
		}
		return msgs;
	}
	int responseMsgsSize(){
		int size=0;
		synchronized (responseMsgs) {
			size=responseMsgs.size();
		}
		return size;
	}
	
	private Queue<Object> requestmsgs = new ArrayDeque<Object>();
	void rcvRequestMsg(Object msg) {
		if (closed) {
			return ;
		}
		requestmsgs.offer(msg);
	}
	List<Object> popRequestMsgs() {
		ArrayList<Object> msgs = new ArrayList<Object>();
		msgs.addAll(requestmsgs);
		requestmsgs.clear();
		return msgs;
	}
	
	private boolean closed=false;
	public boolean isClosedNetworkSession(){
		return closed;
	}
	public void closeNetworkSession() {
		closed=true;
	}
	void close()throws IOException{
		connetion.close();
	}
	
	Map<String, Object> attributes=new HashMap<String, Object>();
	public void putAttribute(String key,Object value){
		attributes.put(key, value);
	}
	public void removeAttribute(String key){
		attributes.remove(key);
	}
	public <T> T getAttribute(String key,Class<T> castType){
		Object object=attributes.get(key);
		if (object==null) {
			return null;
		}
		return castType.cast(object);
	}
}

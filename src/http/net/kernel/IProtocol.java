package http.net.kernel;


import java.io.IOException;
import java.nio.ByteBuffer;
/***
 * 协议数据序列化和发序列化的接口。例子可见{@link MsgProtocol}。
 * 调用service.onRcvMsg(2),service.onSendingMsg(2)通知ioservice安排相应事件。
 * @author wt
 *
 */
public interface IProtocol {
	public int decode(IOService service, NetworkSession session)throws IOException ;

	public ByteBuffer encode(IOService service, NetworkSession session,
			XBuffer response) throws IOException ;
}

package http;

import java.io.IOException;
import java.net.URLDecoder;

import http.api.RequestBody;
import http.base.HttpProtocol;
import net.kernel.NetSession;

/**
 * 
 * @author wutao
 *
 */
public class RequestBodyText implements RequestBody {
	
	private String charset = null;
	private NetSession<HttpProtocol> session;

	private String type = null;
	private String text =null;

	public RequestBodyText(NetSession<HttpProtocol> netSession,String charset) {
		session = netSession; 
		this.charset = charset;
	}

	/* (non-Javadoc)
	 * @see http.api.RequestBody#hasMorePart()
	 */
	@Override
	public int hasMorePart() throws IOException {
		if (session.readableBufferRemaining() == 0) {
			return 0;
		}
		
		type = RequestBody.Part_Text;
		
		StringBuilder stringBuilder = new StringBuilder();

		for (int c = session.read(); c != -1; c = session.read()) {
			stringBuilder.append((char) c);
		}

		text = URLDecoder.decode(stringBuilder.toString(), charset);
		stringBuilder.setLength(0);


		return 1;
	}
	
	/* (non-Javadoc)
	 * @see http.api.RequestBody#getPartType()
	 */
	@Override
	public String getPartType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see http.api.RequestBody#read(byte[])
	 */
	@Override
	public int read(byte[] destBuffer,int offset,int length) throws IOException {
		return -1;
	}

	/* (non-Javadoc)
	 * @see http.api.RequestBody#getParameter(java.lang.String)
	 */
	@Override
	public String[] getParameter(String name) {
		return null;
	}

	/* (non-Javadoc)
	 * @see http.api.RequestBody#getFileName()
	 */
	@Override
	public String getFileName() {
		return null;
	}
	@Override
	public String getString() {
		return text;
	}
}

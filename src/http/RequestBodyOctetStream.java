package http;

import java.io.IOException;

import http.api.RequestBody;
import http.base.HttpProtocol;
import net.kernel.NetSession;

/**
 * 
 * @author wutao
 *
 */
public class RequestBodyOctetStream implements RequestBody {
	
	private NetSession<HttpProtocol> session;

	private String type = null;
	private boolean partEnd=true;

	public RequestBodyOctetStream(NetSession<HttpProtocol> netSession) {
		session = netSession; 
	}

	/* (non-Javadoc)
	 * @see http.api.RequestBody#hasMorePart()
	 */
	@Override
	public int hasMorePart() throws IOException {
		if (!partEnd) {
			return -1;
		}
		if (session.readableBufferRemaining()==0) {
			return 0;
		}
		
		type = RequestBodyOctetStream.Part_OctetStream;

		partEnd=false;
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
	public int read(byte[] destBuffer) throws IOException {
		if (partEnd) {
			return -1;
		}
		
		int destLimit=session.read(destBuffer, 0);
		if (destLimit == -1) {
			partEnd=true;
			return -1;
		}
		
		return destLimit;
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
		return null;
	}
}

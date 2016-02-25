package http;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import http.api.RequestBody;
import http.base.HttpProtocol;
import net.kernel.NetSession;

/**
 * 
 * @author wutao
 *
 */
public class RequestBodyFormUrlencoded implements RequestBody {
	
	private String charset = null;
	private NetSession<HttpProtocol> session;

	private String type = null;
	private Map<String, String[]> parameters=new HashMap<String, String[]>();

	public RequestBodyFormUrlencoded(NetSession<HttpProtocol> netSession,String charset) {
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
		
		type = RequestBody.Part_Paramater;
		
		String name = null;
		StringBuilder stringBuilder = new StringBuilder();

		for (int c = session.read(); c != -1; c = session.read()) {
			if (c == '=') {
				name = URLDecoder.decode(stringBuilder.toString(), charset);
				stringBuilder.setLength(0);
				continue;
			} else if (c == '&') {
				String value = URLDecoder.decode(stringBuilder.toString(), charset);
				
				parameters.put(name, new String[] { value });
				name = null;

				stringBuilder.setLength(0);
				continue;
			}
			stringBuilder.append((char) c);
		}

		String value = URLDecoder.decode(stringBuilder.toString(), charset);
		parameters.put(name, new String[] { value });
		name = null;

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
	public int read(byte[] destBuffer) throws IOException {
		return -1;
	}

	/* (non-Javadoc)
	 * @see http.api.RequestBody#getParameter(java.lang.String)
	 */
	@Override
	public String[] getParameter(String name) {
		return parameters.get(name);
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

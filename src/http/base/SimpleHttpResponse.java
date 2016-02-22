package http.base;

import http.api.Cookie;
import http.api.HttpResponse;
import http.api.WebAppContext;
import net.kernel.NetSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import common.io.XBuffer;

import java.util.Set;

public class SimpleHttpResponse implements HttpResponse {
	Map<String, String> headMap = new HashMap<String, String>();

	String pv = null;
	String statusCode = "200";

	private File document = null;
	private XBuffer buffer=null;
	private List<byte[]> content = new ArrayList<byte[]>();
	private long bodySize =0;

	private WebAppContext appContext = null;

	public SimpleHttpResponse(WebAppContext appContext,SocketChannel channel) {
		this.appContext = appContext;
		headMap.put(HttpResponse.Head_Connection_Response, "close");
	}

	@Override
	public void setHttpVersion(String version) {
		pv = version;
	}

	@Override
	public void setResponseHead(String key, String value) {
		headMap.put(key, value);
	}

	@Override
	public void setResponseHeads(Map<String, String> heads) {
		headMap.putAll(heads);
	}

	@Override
	public void setStatusCode(int code) {
		statusCode = code + "";
	}

	@Override
	public void setContentType(String type) {
		headMap.put(Head_ContentType_Response, type);
	}

	@Override
	public void setContentLength(long lenght) {
		headMap.put(Head_ContentLength_Response, lenght + "");
	}

	@Override
	public void write(byte[] data, int off, int length) {
	}

	@Override
	public void write(String text) {
		byte[] data = text.getBytes(Charset.forName("UTF8"));
		bodySize+=data.length;
		this.content.add(data);
		
	}

	@Override
	public void write(File file) {
		document = file;
		bodySize+=document.length();
		
		String[] name_fix = document.getName().split("\\.");
		if (name_fix.length==1) {
			this.setContentType("application/octet-stream");
			return;
		}

		int subfix=name_fix.length-1;
		String typeString = appContext.mimeType(name_fix[subfix].trim());
		this.setContentType(typeString);	
	}

	@Override
	public void addCookie(Cookie cookie) {

	}

	@Override
	public void serialize(OutputStream outputStream) throws IOException {
		String contentType = headMap
				.get(HttpResponse.Head_ContentType_Response);
		if (contentType == null) {
			headMap.put(HttpResponse.Head_ContentType_Response, "text/plain");
		}
		headMap.put(Head_ContentLength_Response, ""+bodySize);
		
		byte[] version = pv.getBytes(Charset.forName("ASCII"));
		outputStream.write(version);

		outputStream.write(' ');
		byte[] status = statusCode.getBytes(Charset.forName("ASCII"));
		outputStream.write(status);

		byte[] line = "\r\n".getBytes(Charset.forName("ASCII"));
		outputStream.write(line);

		Set<Entry<String, String>> headers = headMap.entrySet();
		for (Entry<String, String> entry : headers) {
			byte[] key = entry.getKey().getBytes(Charset.forName("ASCII"));
			byte[] value = entry.getValue().getBytes(Charset.forName("ASCII"));
			outputStream.write(key);
			outputStream.write(':');
			outputStream.write(value);

			outputStream.write(line);
		}
		outputStream.write(line);

		if (!content.isEmpty()) {
			for (byte[] text : content) {
				if (text==null) {
					continue;
				}
				outputStream.write(text);
			}
		}
		
		if (document != null ) {
			byte[] array=buffer.getData();
			int length = array.length;
			
			FileInputStream inputStream = new FileInputStream(document);
			try {
				for (int readed = inputStream.read(array, 0, length); readed > 0; readed = inputStream
						.read(array, 0, length)) {
					outputStream.write(array, 0, readed);
				}
			} finally {
				inputStream.close();
			}
		}

	}

	@Override
	public void setCharset(String charset) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}
}

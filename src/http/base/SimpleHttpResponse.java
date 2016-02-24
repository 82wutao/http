package http.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import common.io.XBuffer;
import http.api.Cookie;
import http.api.HttpResponse;
import http.protocol.ContentType;
import net.kernel.NetSession;

public class SimpleHttpResponse implements HttpResponse {
	private NetSession<HttpProtocol> session;
	
	Map<String, String> headMap = new HashMap<String, String>();

	String pv = null;
	String statusCode = "200";

	private File document = null;
	
	private String charset=null;
	private XBuffer buffer=new XBuffer(4096);
	private long bodySize =0;


	public SimpleHttpResponse(NetSession<HttpProtocol> session) {
		headMap.put(HttpResponse.Connection, "close");
		this.session = session;
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
		headMap.put(Content_Type, type);
	}

	@Override
	public void setContentLength(long lenght) {
		headMap.put(Content_Length, lenght + "");
	}

	@Override
	public void write(byte[] data, int off, int length) {
		bodySize += (data.length-off);
		buffer.writebytes(data, off, data.length);
	}

	@Override
	public void write(String text) {
		byte[] data = text.getBytes(Charset.forName(charset));
		bodySize+=data.length;
		buffer.writebytes(data, 0, data.length);
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
		String typeString = ContentType.getMimeType(name_fix[subfix].trim());
		this.setContentType(typeString);	
	}

	@Override
	public void addCookie(Cookie cookie) {

	}

	@Override
	public void setCharset(String charset) {
		this.charset = charset;
	}

	@Override
	public void flush() throws IOException {
		
		String contentType = headMap
				.get(HttpResponse.Content_Type);
		if (contentType == null) {
			headMap.put(HttpResponse.Content_Type, "text/plain");
		}
		headMap.put(Content_Length, ""+bodySize);
		
		byte[] version = pv.getBytes(Charset.forName(charset));
		session.write(version, 0, version.length);

		session.write((byte)(' '));
		byte[] status = statusCode.getBytes(Charset.forName(charset));
		session.write(status,0,status.length);

		byte[] line = "\r\n".getBytes(Charset.forName(charset));
		session.write(line,0,line.length);

		Set<Entry<String, String>> headers = headMap.entrySet();
		for (Entry<String, String> entry : headers) {
			String headLine = entry.getKey()+": "+entry.getValue()+"\r\n";
//			byte[] key = entry.getKey().getBytes(Charset.forName(charset));
//			byte[] value = entry.getValue().getBytes(Charset.forName(charset));
			
			byte[] header = headLine.getBytes(Charset.forName(charset));
			session.write(header,0,header.length);
		}
		session.write(line,0,line.length);

		buffer.readyReadingFromBuffer();
		if (buffer.remain()>1) {
			session.write(buffer.getData(),buffer.getPosition(),buffer.getLimit() - buffer.getPosition());
		}
		
		if (document != null ) {
			byte[] array=buffer.getData();
			int length = array.length;
			
			FileInputStream inputStream = new FileInputStream(document);
			try {
				for (int readed = inputStream.read(array, 0, length); readed > 0; readed = inputStream
						.read(array, 0, length)) {
					session.write(array, 0, readed);
				}
			} finally {
				inputStream.close();
			}
		}
	}
}

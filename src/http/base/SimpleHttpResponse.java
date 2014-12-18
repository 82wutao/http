package http.base;

import http.api.Cookie;
import http.api.HttpResponse;
import http.api.WebAppContext;
import io.XBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SimpleHttpResponse implements HttpResponse {
	Map<String, String> headMap = new HashMap<String, String>();

	String pv = null;
	String statusCode = "200";

	private File document = null;
	private XBuffer buffer=null;
	
	private List<String> content = new ArrayList<String>();

	private WebAppContext appContext = null;

	public SimpleHttpResponse(WebAppContext appContext,XBuffer outBuffer) {
		this.appContext = appContext;
		buffer = outBuffer;
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
		this.content.add(text);
	}

	@Override
	public void write(File file) {
		document = file;
		String name = document.getName();
		int dot = name.indexOf('.');
		if (dot == -1) {
			this.setContentType("application/octet-stream");
			return;
		}

		String subfix = name.substring(dot + 1);
		String mimeType = this.appContext.mimeType(subfix);
		if (mimeType==null) {
			mimeType=this.appContext.getServerContext().getMimeType(subfix);
		}
		this.setContentType(mimeType);
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
			for (String text : content) {
				if (text==null) {
					continue;
				}
				if (text.equals("")) {
					continue;
				}
				outputStream.write(text.getBytes("UTF-8"));
			}
		}
		if (document != null && document.exists()) {
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
}

package http.base;

import http.app.Cookie;
import http.app.HttpRequest;
import http.net.kernel.XBuffer;

import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class SimpleHttpRequest implements HttpRequest {
	private XBuffer header;
	private Map<String, String> head_map = new HashMap<String, String>();
	private String head_str = null;

	private Map<String, String> param_map = new HashMap<String, String>();

	Socket channel;
	private XBuffer bodyBegin ;
	private long readedFromBody = 0;

	public SimpleHttpRequest(Socket channel) {
		this.channel=channel;
	}

	@Override
	public Map<String, String> getRequestHeads() {
		return head_map;
	}

	@Override
	public String getRequestHead(String key) {
		return head_map.get(key);
	}

	@Override
	public String getRequestMethod() {
		return head_map.get(Head_METHOD_Request);
	}

	@Override
	public String getHttpVersion() {
		return head_map.get(Head_HttpVersion_Request);
	}

	@Override
	public String getRequestUri() {
		return head_map.get(Head_Uri_Request);
	}

	@Override
	public String getContentLength() {
		return head_map.get(Head_ContentLength_Request);
	}

	@Override
	public String getParamerValue(String paramer) {
		return param_map.get(paramer);
	}

	@Override
	public int readFromBody(byte[] buffer, int off, int length) {
//		int chunks = body.size();
//		if (bodyChunkIndex == chunks) {
//			return -1;
//		}
//
//		XBuffer chunk = body.get(bodyChunkIndex);
//		int chunk_off = chunk.getPosition();
//		int chunk_limit = chunk.getLimit();
//		int chunk_size = chunk_limit - chunk_off;
//		byte[] chunk_data = chunk.getData();
//
//		if (chunk_size <= length) {
//			length = chunk_size;
//		}
//		System.arraycopy(chunk_data, chunk_off, buffer, off, length);
//		chunk.setPosition(chunk_off + length);
//		if ((chunk_off + length) == chunk_limit) {
//			bodyChunkIndex++;
//		}
//
//		int readed = length;
//		readedFromBody += readed;
		return 0;//readed;
	}

	public void setHead(XBuffer buffer) {
		header = buffer;
		int off = header.getPosition();
		int limit = header.getLimit();
		int length = limit - off;

		String str = new String(header.getData(), off, length,
				Charset.forName("ASCII"));
		head_str = str;

		String[] fields = str.split("\r\n");
		String[] method_uri_version = fields[0].split(" ");
		head_map.put(HttpRequest.Head_METHOD_Request, method_uri_version[0]);
		head_map.put(HttpRequest.Head_Uri_Request, method_uri_version[1]);
		head_map.put(HttpRequest.Head_HttpVersion_Request,
				method_uri_version[2]);

		for (int i = 1; i < fields.length; i++) {
			String[] kv = fields[i].split(":");
			head_map.put(kv[0], kv[1]);
		}
		
	}

	public void bodyBegin(XBuffer buffer) {
		bodyBegin=buffer;
	}

	@Override
	public String dumpHead() {
		return head_str;
	}

	@Override
	public String dumpBody() {
		// TODO Auto-generated method stub
		return null;
	}

	public void parseParamers() {
		String method =getRequestMethod();
		if (method.trim().equals("GET")) {
			
			String uri = getRequestUri();
			
			int index = uri.indexOf('?');
			if (index == -1) {
				return;
			}
			String[] uri_params = uri.split("\\?");
			if (uri_params.length == 1) {
				return;
			}
			if (uri_params[1].equals("")) {
				return;
			}
			String[] params = uri_params[1].split("&");
			for (int i = 0; i < params.length; i++) {
				String[] kv = params[i].split("=");
				param_map.put(kv[0], kv[1]);
			}
			return ;
		}
		
		

	}

	@Override
	public Cookie[] getCookies() {
		return null;
	}
}

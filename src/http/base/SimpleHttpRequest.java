package http.base;

import http.api.Cookie;
import http.api.HttpRequest;
import http.net.kernel.XBuffer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class SimpleHttpRequest implements HttpRequest {
	private XBuffer header;
	private Map<String, String> head_map = new HashMap<String, String>();
	private String head_str = null;

	private Map<String, String> param_map = new HashMap<String, String>();

	Socket channel;
	private XBuffer bodyBegin;
	private long readedFromBody = 0;

	public SimpleHttpRequest(Socket channel) {
		this.channel = channel;
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
	public int readFromBody(byte[] buffer, int off, int length)
			throws IOException {
		String contentLength=getContentLength();
		if (contentLength==null
				||contentLength.equals("0")) {
			return 0;
		}
		if (readedFromBody==Integer.parseInt(contentLength)) {
			return 0;
		}
		int pos = bodyBegin.getPosition();
		int limit = bodyBegin.getLimit();

		if (pos < limit) {
			byte[] data = bodyBegin.getData();
			int data_size = limit - pos;
			if (data_size < length) {
				length = data_size;
			}
			System.arraycopy(data, pos, buffer, off, length);
			bodyBegin.setPosition(pos + length);
			
			readedFromBody+=length;
			return length;
		}
		int readed = this.channel.getInputStream().read(buffer, off, length);
		readedFromBody+=readed;
		return readed;
	}

	public void setHead(XBuffer buffer) throws UnsupportedEncodingException {
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
		method_uri_version[1] = URLDecoder.decode(method_uri_version[1], "UTF-8");
		head_map.put(HttpRequest.Head_Uri_Request, method_uri_version[1]);
		head_map.put(HttpRequest.Head_HttpVersion_Request,
				method_uri_version[2]);

		for (int i = 1; i < fields.length; i++) {
			String[] kv = fields[i].split(":");
			head_map.put(kv[0], kv[1]);
		}

	}

	public void bodyBegin(XBuffer buffer) {
		bodyBegin = buffer;
	}

	@Override
	public String dumpHead() {
		return head_str;
	}

	@Override
	public String dumpBody() {
		return null;
	}

	public boolean parseParamers() throws IOException {
		String method = getRequestMethod();
		if (method.trim().equals("GET")) {

			String uri = getRequestUri();

			int index = uri.indexOf('?');
			if (index == -1) {
				return true;
			}
			String[] uri_params = uri.split("\\?");
			if (uri_params.length == 1) {
				return true;
			}
			
			head_map.put(HttpRequest.Head_Uri_Request, uri_params[0]);
			
			if (uri_params[1].equals("")) {
				return true;
			}
			String[] params = uri_params[1].split("&");
			for (int i = 0; i < params.length; i++) {
				String[] kv = params[i].split("=");
				param_map.put(kv[0], kv[1]);
			}
			return true;
		}
		
		if (method.trim().equals("POST")) {
			String contentTypeHeader = head_map.get(Head_ContentType_Request);
			String contentLengthHeader=head_map.get(Head_ContentLength_Request);
			
			if (contentTypeHeader.trim().equals("application/x-www-form-urlencoded")) {
				int length = Integer.parseInt(contentLengthHeader);
				
				byte[] content =new byte[length];
				int begin = bodyBegin.getPosition();
				int limit = bodyBegin.getLimit();

				int copied = limit - begin;
				System.arraycopy(bodyBegin.getData(),begin,content,0,copied);
				if(copied!=length){
					this.channel.getInputStream().read(content,copied, length-(copied));
				}
				
				String param= new String(content,Charset.forName("ASCII"));
				param =URLDecoder.decode(param, "UTF-8");
				String[] params = param.split("&");
				for (int i = 0; i < params.length; i++) {
					String[] kv = params[i].split("=");
					param_map.put(kv[0], kv[1]);
				}
			}else if (contentTypeHeader.trim().equals("multipart/form-data") ){
				
			}
			
			return true;
		}
		return true;
	}

	@Override
	public Cookie[] getCookies() {
		return null;
	}
}

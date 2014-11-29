package http.base;

import java.io.File;
import java.net.Socket;
import java.util.Map;

import http.app.Cookie;
import http.app.HttpResponse;

public class SimpleHttpResponse implements HttpResponse {
	public SimpleHttpResponse(Socket channel) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setResponseHead(String key, String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setResponseHeads(Map<String, String> heads) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getStatusCode(int code) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setContextType(String type) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setContextLength(int lenght) {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(byte[] data, int off, int length) {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(File file) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addCookie(Cookie cookie) {
		// TODO Auto-generated method stub

	}

}

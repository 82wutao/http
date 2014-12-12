package http.base.staticdoc;

import java.io.File;

import http.api.HttpRequest;
import http.api.HttpResponse;
import http.api.HttpServerlet;
import http.api.WebAppContext;

public class StaticDocumentServerlet implements HttpServerlet {

	@Override
	public void doGET(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		String uri = request.getRequestUri();

		String filesysPath = context.getContextFileSystemPath();
		String urlContext = context.getContextPath();

		String diskFile = uri.replaceFirst(urlContext, filesysPath + "/");
		File doc = new File(diskFile);
		if (!doc.exists()) {
			response.setStatusCode(404);
			return;
		}

		response.setStatusCode(200);
		response.setResponseHead(HttpResponse.Head_AcceptRanges_Response,
				"bytes");
		response.setContentLength(doc.length());

		String[] name_fix = diskFile.split("\\.");
		String typeString = context.mimeType(name_fix[1]);
		response.setContentType(typeString);
		response.write(doc);
	}

	@Override
	public void doHEAD(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doPOST(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doPUT(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doDELETE(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doTRACE(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doOPTIONS(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doCONNECT(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		// TODO Auto-generated method stub

	}
}

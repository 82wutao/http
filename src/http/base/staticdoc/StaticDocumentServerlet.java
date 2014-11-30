package http.base.staticdoc;

import java.io.File;

import http.app.HttpRequest;
import http.app.HttpResponse;
import http.app.HttpServerlet;
import http.app.WebAppContext;

public class StaticDocumentServerlet implements HttpServerlet {
	@Override
	public void doit(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		String uri = request.getRequestUri();

		String filesysPath = context.getContextFileSystemPath();
		String urlContext = context.getContextPath();

		String diskFile = uri.replace(urlContext, filesysPath+"/");
		File doc = new File(diskFile);
		if (!doc.exists()) {
			response.setStatusCode(404);
			return ;
		}
		
		response.setStatusCode(200);
		response.setResponseHead(HttpResponse.Head_AcceptRanges_Response, "bytes");
		response.setContentLength(doc.length());
		
		String[] name_fix=diskFile.split("\\.");
		String typeString = context.mimeType(name_fix[1]);
		response.setContentType(typeString);
		response.write(doc);
	}
}

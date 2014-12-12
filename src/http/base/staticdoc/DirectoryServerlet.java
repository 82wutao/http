package http.base.staticdoc;

import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import http.api.HttpRequest;
import http.api.HttpResponse;
import http.api.HttpServerlet;
import http.api.WebAppContext;

public class DirectoryServerlet implements HttpServerlet {

	@Override
	public void doGET(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		String uri = request.getRequestUri();

		String filesysPath = context.getContextFileSystemPath();
		String urlContext = context.getContextPath();

		String diskFile = uri.replaceFirst(urlContext, filesysPath + "/");
		File dir = new File(diskFile);
		if (!dir.exists()) {
			response.setStatusCode(404);
			return;
		}
		
		String html=generateHtml(uri,dir);
		
		response.setStatusCode(200);
		response.setResponseHead(HttpResponse.Head_AcceptRanges_Response,
				"bytes");
		try {
			response.setContentLength(html.getBytes("UTF-8").length);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		response.setContentType("text/html");
		response.write(html);
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
	
	
	private String generateHtml(String uri,File dir){
        String heading = "Directory " + uri;
        StringBuilder msg = new StringBuilder("<html><head><title>" + heading + "</title><style><!--\n" +
            "span.dirname { font-weight: bold; }\n" +
            "span.filesize { font-size: 75%; }\n" +
            "// -->\n" +
            "</style>" +
            "</head><body><h1>" + heading + "</h1>");

        String up = uri+"../";

        List<String> files = Arrays.asList(dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isFile();
            }
        }));
        Collections.sort(files);
        
        List<String> directories = Arrays.asList(dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isDirectory();
            }
        }));
        Collections.sort(directories);
        
        msg.append("<ul>");
        msg.append("<section class=\"directories\">");
        if (!up.equals("/../")) {
        	msg.append("<li><a rel=\"directory\" href=\"").append(up).append("\"><span class=\"dirname\">..</span></a></b></li>");
		}
        for (String directory : directories) {
            String subdir = directory + "/";
            msg.append("<li><a rel=\"directory\" href=\"").append(uri+subdir).append("\"><span class=\"dirname\">").append(subdir).append("</span></a></b></li>");
        }
        msg.append("</section>");
        msg.append("<section class=\"files\">");
        for (String file : files) {
            msg.append("<li><a href=\"").append(uri + file).append("\"><span class=\"filename\">").append(file).append("</span></a>");
            File curFile = new File(dir, file);
            long len = curFile.length();
            msg.append("&nbsp;<span class=\"filesize\">(");
            if (len < 1024) {
                msg.append(len).append(" bytes");
            } else if (len < 1024 * 1024) {
                msg.append(len / 1024).append(".").append(len % 1024 / 10 % 100).append(" KB");
            } else {
                msg.append(len / (1024 * 1024)).append(".").append(len % (1024 * 1024) / 10 % 100).append(" MB");
            }
            msg.append(")</span></li>");
        }
        msg.append("</section>");
        msg.append("</ul>");
        msg.append("</body></html>");
        
return msg.toString();
        
	}
}

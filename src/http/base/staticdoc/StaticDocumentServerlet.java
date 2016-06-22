package http.base.staticdoc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import http.WebAppContext;
import http.api.HttpRequest;
import http.api.HttpResponse;
import http.api.HttpServerlet;
import http.api.RequestBody;

public class StaticDocumentServerlet implements HttpServerlet {

	@Override
	public void doGET(WebAppContext context, HttpRequest request,
			HttpResponse response) throws Exception{
		String uri = request.getRequestUri();
		String root = context.getWWWRoot();
		if (!uri.equals("/")) {
			String diskFile = root+uri;		

			handleDoc(diskFile, context, request, response);
			return ;
		}

		String indexpage = root+"/index.html";
		File file = new File(indexpage);
		if (file.exists()) {
			handleDoc(indexpage, context, request, response);
			return;
		}
		
		response.setStatusCode(200);
		response.setContentType("text/html");
		response.write("This server is working!!");
		return;
	}

	@Override
	public void doHEAD(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doPOST(WebAppContext context, HttpRequest request,
			HttpResponse response)throws Exception {
		RequestBody form=request.getRequestBody();
		if (form!=null) {
			handleMultiPartForm(context,request,response);
		}
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
	private void handleMultiPartForm(WebAppContext context, HttpRequest request,
			HttpResponse response) throws IOException{
		RequestBody multiPartForm=request.getRequestBody();
		try {
			while(multiPartForm.hasMorePart()==1){
				String type =multiPartForm.getPartType();
				if (type.equals(RequestBody.Part_Paramater)) {
				}else{
					byte[] buff =new byte[512];
					String name = multiPartForm.getFileName();
					
					File file=new File(context.getProperty("upload","./")+"/"+name);
					OutputStream outputStream = new FileOutputStream(file);
					
					try {
						int readed = multiPartForm.read(buff,0,buff.length);
						while(readed!=-1){
							outputStream.write(buff, 0, readed);
							readed = multiPartForm.read(buff,0,buff.length);
						}
					} catch (Exception e) {
						throw e;
					}finally{
						outputStream.close();
					}

					
					
				}
					
			}
			response.setStatusCode(200);
			response.write("good");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void handleDoc(String diskFile,WebAppContext context, HttpRequest request,
			HttpResponse response){
		
		
		File file = new File(diskFile);
		if (!file.exists()) {
			response.setStatusCode(404);
			return;
		}
		
		if (file.isDirectory()) {
			String html=listDir(request.getRequestUri(),file);
			response.setStatusCode(200);
			response.setResponseHead(HttpResponse.Accept_Ranges,
					"bytes");
			try {
				response.setContentLength(html.getBytes("UTF-8").length);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			response.setContentType("text/html");
			response.write(html);
		}else {
			response.setStatusCode(200);
			response.setResponseHead(HttpResponse.Accept_Ranges,
					"bytes");
			response.setContentLength(file.length());

			String[] name_fix = diskFile.split("\\.");
			int subfix=name_fix.length-1;
			String typeString = context.getMimeType(name_fix[subfix].trim());
			response.setContentType(typeString);
			response.write(file);
		}
	}
	
	private String listDir(String uri,File dir){
        String heading = "Directory " + uri;
        StringBuilder msg = new StringBuilder("<html><head><meta charset=\"UTF-8\"><title>" + heading + "</title><style><!--\n" +
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


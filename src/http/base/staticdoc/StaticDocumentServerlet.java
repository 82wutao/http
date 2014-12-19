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

import app.terminal.Cmd;

import http.api.HttpRequest;
import http.api.HttpResponse;
import http.api.HttpServerlet;
import http.api.MultiPartForm;
import http.api.WebAppContext;

public class StaticDocumentServerlet implements HttpServerlet {

	@Override
	public void doGET(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		String uri = request.getRequestUri();
		if (!uri.equals("/")) {
			
			String filesysPath = context.getContextFileSystemPath();
			String urlContext = context.getContextPath();

			String diskFile = uri.replaceFirst(urlContext, filesysPath + "/");			
			
			if (uri.endsWith("ctlor")) {
				handleScript(diskFile,context,request,response);
				return ;
			}

			handleDoc(diskFile, context, request, response);
			return ;
		}

		String indexpage = context.getContextAttribute("index");
		if (indexpage == null) {
			response.setStatusCode(200);
			response.setContentType("text/html");
			response.write("This server is working!!");
			return;
		}
		
		String filesysPath = context.getContextFileSystemPath();
		File index=new File(filesysPath+"/"+indexpage);
		if (!index.exists()) {
			response.setStatusCode(404);
			response.setContentType("text/html");
			response.write("The /"+indexpage+" not be found");
			return;
		}
		
		response.setStatusCode(200);
		response.setContentType("text/html");
		response.write(index);
	}

	@Override
	public void doHEAD(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doPOST(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		MultiPartForm form=request.getMultiPartForm();
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
			HttpResponse response){
		MultiPartForm multiPartForm=request.getMultiPartForm();
		try {
			while(multiPartForm.hasMorePart()){
				String type =multiPartForm.getPartType();
				if (type.equals(MultiPartForm.Part_Paramater)) {
				}else{
					byte[] buff =new byte[512];
					String name = multiPartForm.getFileName();
					
					File file=new File(context.getContextAttribute("upload")+"/"+name);
					OutputStream outputStream = new FileOutputStream(file);
					
					try {
						int readed = multiPartForm.read(buff);
						while(readed!=-1){
							outputStream.write(buff, 0, readed);
							readed = multiPartForm.read(buff);
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
			String html=generateHtml(request.getRequestUri(),file);
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
		}else {
			response.setStatusCode(200);
			response.setResponseHead(HttpResponse.Head_AcceptRanges_Response,
					"bytes");
			response.setContentLength(file.length());

			String[] name_fix = diskFile.split("\\.");
			int subfix=name_fix.length-1;
			String typeString = context.mimeType(name_fix[subfix].trim());
			response.setContentType(typeString);
			response.write(file);
		}
	}
	private void handleScript(String url,WebAppContext context, HttpRequest request,
			HttpResponse response){
		String cmd = request.getParamerValue("script");
		response.setContentType("text/html");
		response.write("<html><head><meta charset=\"UTF-8\"><title>"+cmd+"</title></head><body>");//输出数据
		Exception exception=null;
		try{
			String appContext=context.getContextFileSystemPath();
			
			String result =Cmd.getInstance().execute("sh "+appContext+"/scripts/"+cmd+".sh", "utf8");
			response.write(result);
		}catch(Exception e){
			exception=e;
			
			response.write(exception.getMessage());//输出数据
			response.write("<br/>");//输出一个换行符
			
			StackTraceElement[] stacks=exception.getStackTrace();
			for(int i=0;i<stacks.length;i++){
				String clazz= stacks[i].getClassName();
				String method= stacks[i].getMethodName();
				int line =stacks[i].getLineNumber();
				
				response.write(clazz);//输出数据
				response.write(":");//输出数据
				response.write(method);//输出数据
				response.write(":");//输出数据
				response.write(line+"");//输出数据
				response.write("<br/>");//输出一个换行符
			}
		}
		response.write("</body></html>");
	}
	
	private String generateHtml(String uri,File dir){
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


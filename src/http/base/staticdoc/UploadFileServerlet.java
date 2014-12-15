package http.base.staticdoc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;

import http.api.HttpRequest;
import http.api.HttpResponse;
import http.api.HttpServerlet;
import http.api.MultiPartForm;
import http.api.WebAppContext;

public class UploadFileServerlet implements HttpServerlet {

	@Override
	public void doGET(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doHEAD(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doPOST(WebAppContext context, HttpRequest request,
			HttpResponse response) {
		MultiPartForm multiPartForm=request.getMultiPartForm();
		try {
			while(multiPartForm.hasMorePart()){
				String type =multiPartForm.getPartType();
				if (type.equals(MultiPartForm.Part_Paramater)) {
					String name = multiPartForm.getParamName();
					String value = multiPartForm.getParamValue();
					System.out.println(name+" " +value);
				}else{
					byte[] buff =new byte[512];
					String name = multiPartForm.getFileName();
					File file=new File(name);
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
		} catch (IOException e) {
			e.printStackTrace();
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

}

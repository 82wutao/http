package http.base;

import http.RequestBodyFormUrlencoded;
import http.RequestBodyJson;
import http.RequestBodyMultipartForm;
import http.RequestBodyOctetStream;
import http.RequestBodyText;
import http.api.HttpRequest;
import http.api.RequestBody;
import http.protocol.ContentType;
import http.protocol.HttpHeaders;
import http.protocol.HttpMethods;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.kernel.NetSession;

public class HttpProtocol  implements HttpRequest {
	public static final int Parse_State_Begin=0;
	public static final int Parse_State_Verb=1;
	public static final int Parse_State_Path=2;
	public static final int Parse_State_Version=3;
	public static final int Parse_State_HeaderName=6;
	public static final int Parse_State_HeaderValue=7;
	public static final int Parse_State_AfterHead=8;
	public static final int Parse_State_Body=9;
	public static final int Parse_State_Finish=10;
	
	
	private int currentState;
	private int substate;
	private StringBuilder stringBuilder=new StringBuilder();
	private String temp_name=null;
	
	private Charset charset = null;

	private Map<String, String[]> paramaters=new HashMap<String, String[]>();
	
	private NetSession<HttpProtocol> session;
	
	private String verb;
	private String uri;
	private String version;
	
	private Map<String, String> headers =new HashMap<String, String>();
	private RequestBody body =null;

	public HttpProtocol(Charset charset,NetSession<HttpProtocol> netSession) {
		this.charset = charset;
		currentState =Parse_State_Begin;
		substate=0;
		stringBuilder.setLength(0);
		
		session = netSession;
	}
	
	
	public boolean parseProtocol() throws Exception{
		
		while(currentState <= Parse_State_AfterHead){
			System.out.println("parse protocol state is "+currentState);
			switch (currentState) {
			case Parse_State_Begin:
			case Parse_State_Verb:
				parseVerb();
				break;
			case Parse_State_Path:
				parsePath();
				break;
			case Parse_State_Version:
				parseVersion();
				break;
			case Parse_State_HeaderName:
				parseHeaderName();
				break;
			case Parse_State_HeaderValue:
				parseHeaderValue();
				break;
			case Parse_State_AfterHead:
				if (verb.equals(HttpMethods.Http11_POST)
						||verb.equals(HttpMethods.Http11_PUT)) {
					currentState = Parse_State_Body;
				}else {
					currentState = Parse_State_Finish;
				}
				return true;
//			case Parse_State_Body:
//				
//				break;
//			case Parse_State_Finish:
//				
//				return true;
//	
			}
		}

		return false;
	}
	protected void parseVerb() throws Exception {
		int remaining = session.readableBufferRemaining();
		
	    
	    while(remaining >0){
	    	int c =session.read();
	    	remaining--;
	    	
	    	if (c == ' '|| c == '\t'){
	    		verb = stringBuilder.toString().trim();
	    		break;
	    	}
	    	else if (c == '\r' 
            		|| c == '\n') {
                throw new Exception("failed To Parse Http-Method");
            }
	    	stringBuilder.append((char)c);
	    }
	    
	    if (verb == null) {
			return ;
		}
	    
	    currentState = Parse_State_Path;
	    substate = Path_State_Path;
	    stringBuilder.setLength(0);
	}
	
	private static final int Path_State_Path=1;
	private static final int Path_State_Name=2;
	private static final int Path_State_Value=3;
	protected void parsePath() throws Exception {
	    /*
	     *  /path?k=v&k=v
	     */
		int remainning = session.readableBufferRemaining();
		boolean compelet = false;
	    while(remainning > 0){
	    	int c =session.read();
	    	remainning--;
	    	
	    	if (c == '\r' 
	    			|| c == '\n') {
	    		throw new Exception("failed To Parse Http-Path");
	    	}
	    	
	    	boolean append=true;
	    	switch (substate) {
			case Path_State_Path:
				if (c == ' '|| c == '\t'){
		    		uri = stringBuilder.toString().trim();
		    		compelet =true;
		    		
					stringBuilder.setLength(0);
					append=false;
		    	}else if (c == '?') {
					uri = stringBuilder.toString().trim();
					substate = Path_State_Name;
					
					stringBuilder.setLength(0);
					append=false;
				}
				break;
			case Path_State_Name:
				if (c == ' '|| c == '\t'){
		    		temp_name = stringBuilder.toString().trim();
		    		compelet =true;
		    		
					stringBuilder.setLength(0);
					append=false;
					
					paramaters.put(temp_name, new String[]{""});
		    	}else if (c == '=') {
		    		temp_name = stringBuilder.toString().trim();
					substate = Path_State_Value;
					
					stringBuilder.setLength(0);
					append=false;
				}
				break;
			case Path_State_Value:
				String value=null;
				if (c == ' '|| c == '\t'){
		    		value = stringBuilder.toString().trim();
		    		compelet =true;
		    		
					stringBuilder.setLength(0);
					append=false;
		    	}else if (c == '&') {
		    		value = stringBuilder.toString().trim();
					substate = Path_State_Name;
					
					stringBuilder.setLength(0);
					append=false;
				}
				
				if (value!=null) {
					paramaters.put(temp_name, value.split(","));
				}
				break;
			}
	    	if (append) {
	    		stringBuilder.append((char)c);
			}
		    if (compelet) {
				break ;
			}
		    
		    
	    }
	    
	    if (compelet == false) {
			return ;
		}
	    
	    currentState = Parse_State_Version;
	    substate=0;
	    temp_name =null;
	    stringBuilder.setLength(0);
	    base64Decode();
	}
	protected void parseVersion() throws IOException {
		int remainning = session.readableBufferRemaining();
		boolean comp=false;
		while(remainning>0){
			int c = session.read();
			remainning--;
			
			if (c == '\r' ){
				continue;
			}
			if ( c == '\n'){
				comp =true;
				break;
			}
			stringBuilder.append((char)c);
		}
		if (comp){
			version = stringBuilder.toString().trim();
		    currentState = Parse_State_HeaderName;
			stringBuilder.setLength(0);
		}
	}

	protected void parseHeaderName() throws IOException {
		boolean comp=false;
		
		int remainning = session.readableBufferRemaining();
		while(remainning > 0){
			int c =session.read();
			remainning--;
			
			if ( c == ':'){
				comp =true;
				break;
			}
			
			if (c == '\r') {
				continue;
			}
			if (c == '\n'){
			    currentState =Parse_State_AfterHead;
				stringBuilder.setLength(0);
				break;
			}
			stringBuilder.append((char)c);
		}
		
		if (comp){
			temp_name = stringBuilder.toString().trim();
			
		    currentState = Parse_State_HeaderValue;
			stringBuilder.setLength(0);			
		}
	}
	protected void parseHeaderValue() throws IOException {
		boolean comp=false;
		
		String value_str = null;
		int remmainning = session.readableBufferRemaining();
		while(remmainning > 0){
			int c =session.read();
			remmainning--;
			
			if ( c == '\r'){
				continue;
			}else if ( c == '\n'){
				comp =true;
				break;
			}
			stringBuilder.append((char)c);
		}
		
		if (comp){
			value_str = stringBuilder.toString();
			
			headers.put(temp_name, value_str.trim());
			
			temp_name=null;
		    currentState = Parse_State_HeaderName;
			stringBuilder.setLength(0);			
		}
	}
	
	protected void base64Decode() throws IOException{
		int decode =this.uri.indexOf('%');
		if (decode != -1) {
			uri=URLDecoder.decode(uri, charset.displayName());
		}
	    
		for(Entry<String, String[]> entry:this.paramaters.entrySet()){
			String[] array=entry.getValue();
			
			for (int i = 0; i < array.length; i++) {
				String value = array[i];
				
				decode =value.indexOf('%');
				if (decode ==-1) {
					continue;
				}
				array[i]=URLDecoder.decode(value, charset.displayName());
			}
		}
		
	}


	@Override
	public String getRequestMethod() {
		return verb;
	}


	@Override
	public String getRequestUri() {
		return uri;
	}


	@Override
	public String getHttpVersion() {
		return version;
	}


	@Override
	public Map<String, String> getRequestHeads() {
		return headers;
	}


	@Override
	public String getRequestHead(String key) {
		return headers.get(key);
	}


	@Override
	public String getContentLength() {
		return headers.get(HttpRequest.Content_Length);
	}


	@Override
	public String getContentType() {
		return headers.get(HttpRequest.Content_Type);
	}


	@Override
	public String getCharset() {
		String value =headers.get(HttpRequest.Content_Type);
		String set =HttpHeaders.extractQuotedValueFromHeader(value, "charset");
		if (set==null
				||set.equals("")) {
			set=this.charset.displayName();
		}else {
			this.charset = Charset.forName(set);
		}
		return set;
	}


	@Override
	public http.api.Cookie[] getCookies() {
		String value =headers.get(HttpRequest.Cookie);
		String[] cookie = value.split("; ");
		return null;
		//TODO 
	}


	@Override
	public RequestBody getRequestBody() throws IOException {
		if (body != null) {
			return body;
		}
		if (!verb.equals(HttpMethods.Http11_POST)
				&& !verb.equals(HttpMethods.Http11_PUT)) {
			return null;
		}
		parseBody();
		return body;
	}
	
	protected void parseBody() throws IOException {
	
		if (verb.equals("POST")) {
			String contentTypeHeader = headers.get(Content_Type);
			String contentLengthHeader=headers.get(Content_Length);
			
			if (contentTypeHeader.startsWith(ContentType.Application_X_WWWFormUrlencoded)) {
				this.body=new RequestBodyFormUrlencoded(session,charset.displayName());
			}else if (contentTypeHeader.trim().startsWith(ContentType.Multipart_Formdata) ){
				String[] type_split= contentTypeHeader.trim().split(";");
				String boundary=type_split[1].trim().split("=")[1];
				byte[] boundaryBytes=boundary.getBytes(charset);
				this.body=new RequestBodyMultipartForm(Long.parseLong(contentLengthHeader.trim()),boundaryBytes, session,charset.displayName());
			}
			else if (contentTypeHeader.trim().startsWith(ContentType.Application_Json) ){
				this.body=new RequestBodyJson(session,charset.displayName());
			}
			else if (contentTypeHeader.trim().startsWith(ContentType.Text_Xml) ){
				this.body=new RequestBodyText(session,charset.displayName());
			}
			else if (contentTypeHeader.trim().startsWith(ContentType.Application_OctetStream) ){
				this.body=new RequestBodyOctetStream(session);
			}
		}
	}

	@Override
	public String getParamerValue(String paramer) {
		String[] value =this.paramaters.get(paramer);
		if (value ==null
				||value.length==0){
			if (!verb.equals(HttpMethods.Http11_POST)
					&&!verb.equals(HttpMethods.Http11_PUT)) {
				return null;
			}
			try {
				parseBody();
				if (body.hasMorePart()!=0) {
					return this.body.getParameter(paramer)[0]; 
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		else{
			return value[0];
		}
	}

	
}

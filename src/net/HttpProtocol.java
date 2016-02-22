package net;

import http.HttpProccesser;
import http.api.HttpResponse;
import http.base.SimpleHttpRequest;
import http.protocol.HttpVersions;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import common.io.XBuffer;

public class HttpProtocol  {
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
	
	private String verb;
	private String uri;
	private String version;
	
	private Map<String, String> headers =new HashMap<String, String>();

	public HttpProtocol(Charset charset) {
		this.charset = charset;
		currentState =Parse_State_Begin;
		substate=0;
		stringBuilder.setLength(0);
	}
	
	
	public boolean parseProtocol(ByteBuffer buffer){
		while(currentState < Parse_State_AfterHead){
			switch (currentState) {
			case Parse_State_Begin:
			case Parse_State_Verb:
				parseVerb(buffer);
				break;
			case Parse_State_Path:
				parsePath(buffer);
				break;
			case Parse_State_Version:
				parseVersion(buffer);
				break;
			case Parse_State_HeaderName:
				parseHeaderName(buffer);
				break;
			case Parse_State_HeaderValue:
				parseHeaderValue(buffer);
				break;
			case Parse_State_AfterHead:
				return true;
			case Parse_State_Body:
				
				break;
			case Parse_State_Finish:
				
				return true;
	
			}
		}

		return false;
	}
	protected void parseVerb(ByteBuffer buffer) {
		int remaining = buffer.remaining();
		
	    int position = buffer.position();
	    int offset = 0;
	    
	    while(offset < remaining ){
	    	int c =buffer.get(position + offset);
	    	if (c == ' '|| c == '\t'){
	    		verb = stringBuilder.toString().trim();
	    		break;
	    	}
	    	else if (c == '\r' 
            		|| c == '\n') {
                throw new Exception("failed To Parse Http-Method");
            }
	    	stringBuilder.append((char)c);
	    	offset++;
	    }
	    buffer.position(position+offset+1);
	    
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
	protected void parsePath(ByteBuffer buffer) {
	    /*
	     *  /path?k=v&k=v
	     */
		boolean compelet = false;
	    while(buffer.hasRemaining()){
	    	int c =buffer.get();
	    	if (c == '\r' 
	    			|| c == '\n') {
	    		throw new Exception("failed To Parse Http-Method");
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
	protected void parseVersion(ByteBuffer buffer) {
		
		boolean comp=false;
		while(buffer.hasRemaining()){
			int c =buffer.get();
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

	protected void parseHeaderName(ByteBuffer buffer) {
		boolean comp=false;
		
		String header_str = null;
		while(buffer.hasRemaining()){
			int c =buffer.get();
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
	protected void parseHeaderValue(ByteBuffer buffer) {
		boolean comp=false;
		
		String value_str = null;
		while(buffer.hasRemaining()){
			int c =buffer.get();
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
	/***
	 * 
	 * @param buffer
	 * @param off
	 * @param length
	 * @return endOfHead
	 */
	private int parseProtocol(byte[] buffer,int off,int length){

		int index =off;
		while (index < length) {
			int ch=buffer[index];
			
			if (ch=='\n') {
				
				if (buffer[index+2]=='\n') {
					return (index-1);
				}
			}
			index++;
		}
		return 0;
	}
	
	public SimpleHttpRequest decode(Socket session,HttpProccesser proccesser) throws IOException{
		XBuffer buffer=proccesser.buffer;
		if (buffer==null) {
			buffer = new XBuffer(4096);
			buffer.readyWritingToBuffer();
		}
		int off=buffer.getPosition();
		int limit = buffer.getLimit();
		byte[] buff=buffer.getData();
		
		int readed=session.getInputStream().read(buff, off, limit-off);
		if (readed<=0) {
			return null;
		}
		
		
		int endOfHead=parseProtocol(buff, 0, off+readed);
		if (endOfHead==0) {
			buffer.setPosition(off+readed);
			return null;
		}
		
		buffer.setLimit(endOfHead);
		SimpleHttpRequest request = new SimpleHttpRequest(session);
		request.setHead(buffer);
		
		if (endOfHead + 4 < (off + readed)) {
			XBuffer body = buffer.cloneSelf();
			body.setPosition(endOfHead + 4);
			body.setLimit(off + readed);
			request.bodyBegin(body);
		}
		
		return request;
	}
	protected void base64Decode(){
		int decode =this.uri.indexOf('%');
		if (decode != -1) {
			sun.misc.BASE64Decoder decoder =new sun.misc.BASE64Decoder();
			byte[] bytes =decoder.decodeBuffer(this.uri);
			uri=new String(bytes,charset);
		}
	    
		
		
		for(Entry<String, String[]> entry:this.paramaters.entrySet()){
			String key =entry.getKey();
			String[] array=entry.getValue();
			
			for (int i = 0; i < array.length; i++) {
				String value = array[i];
				
				decode =value.indexOf('%');
				if (decode ==-1) {
					continue;
				}
				sun.misc.BASE64Decoder decoder =new sun.misc.BASE64Decoder();
				byte[] bytes =decoder.decodeBuffer(value);
				array[i]=new String(bytes,charset);
			}
		}
		
	}
	public void encode(Socket session,HttpResponse response) throws IOException {
		response.serialize(session.getOutputStream());
	}
	
}

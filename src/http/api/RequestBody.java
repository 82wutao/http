package http.api;

import java.io.IOException;

public interface RequestBody {

	String Part_File = "File";
	String Part_Paramater = "Param";
	String Part_OctetStream = "OctetStream";
	String Part_Json = "Json";
	String Part_Text = "Text";
	
	/**
		 * 
		 * @return -1 this part has more bytes,you should read it|0 no more part|1 has more part 
		 * @throws IOException
		 */
	int hasMorePart() throws IOException;
	String getPartType();

	int read(byte[] destBuffer,int off,int length) throws IOException;
	String[] getParameter(String name);
	String getFileName();
	String getString();
}
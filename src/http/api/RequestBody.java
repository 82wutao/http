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
	//	private String readline() throws IOException{
	//		StringBuilder builder = new StringBuilder();
	//		while (true){
	//			byte c = session.read();
	//			if (c ==-1) {
	//				session.readBytesFromChanel();				
	//				continue;
	//			}
	//			readed ++;
	//			if (c == '\r') {
	//				continue;
	//			}
	//			if (c == '\n') {
	//				break;
	//			}
	//			builder.append(c);
	//		}
	//		return builder.toString();
	//	}

	String getPartType();

	int read(byte[] destBuffer) throws IOException;

	String[] getParameter(String name);

	String getFileName();
	String getString();
}
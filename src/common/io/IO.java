package common.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class IO {
	public static IO instance = new IO();

	public IO() {
	}

	public static IO getInstance() {
		return instance;
	}

	public static void setInstance(IO obj) {
		instance = obj;
	}

	public String readMessageFromStream(InputStream stream,String encode) throws IOException {

		byte[] buffer = new byte[1024];
		XBuffer stringOutput = new XBuffer(1024 * 4);
		stringOutput.readyWritingToBuffer();

		for (int readed = stream.read(buffer); readed != -1; readed = stream
				.read(buffer)) {
			if (readed == 0) {
				continue;
			}
			stringOutput.writebytes(buffer, 0, readed);
		}

		stringOutput.readyReadingFromBuffer();
		int length = stringOutput.getLimit();
		if (length == 0) {
			return null;
		}
		String string = new String(stringOutput.getData(), 0, length,
				Charset.forName(encode));

		return string;
	}
	
	public void copy(InputStream inputStream,OutputStream outputStream,byte[] buffer,int size) throws IOException{
		int length = buffer.length;
		if (length > size) {
			length = size;
		}
		
		for(int readed =inputStream.read(buffer, 0, length);
				readed!=-1;
				readed=inputStream.read(buffer, 0, length)){
			
			outputStream.write(buffer, 0, readed);
			size -=readed;
			if (size==0) {
				break;
			}
			if (length > size) {
				length = size;
			}
		}
	}
}

package io;

import java.io.IOException;
import java.io.InputStream;
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
}

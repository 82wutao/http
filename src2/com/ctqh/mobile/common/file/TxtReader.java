package com.ctqh.mobile.common.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TxtReader {
	private TxtReader() {
	}

	private InputStream inputStream;
	private InputStreamReader streamReader;
	private BufferedReader reader;

	public static TxtReader newReader(String file, String charset)
			throws IOException {
		TxtReader txtReader = new TxtReader();
		txtReader.inputStream = new FileInputStream(file);
		txtReader.streamReader = new InputStreamReader(txtReader.inputStream,
				charset);
		txtReader.reader = new BufferedReader(txtReader.streamReader);

		return txtReader;
	}
	public static TxtReader newReader(File file, String charset)
			throws IOException {
		TxtReader txtReader = new TxtReader();
		txtReader.inputStream = new FileInputStream(file);
		txtReader.streamReader = new InputStreamReader(txtReader.inputStream,
				charset);
		txtReader.reader = new BufferedReader(txtReader.streamReader);

		return txtReader;
	}

	public void close() throws IOException {
		this.reader.close();
		this.streamReader.close();
		this.inputStream.close();
	}

	public String readLine() throws IOException {
		return this.reader.readLine();
	}
	public void nextLine() throws IOException {
		this.reader.readLine();
	}
}
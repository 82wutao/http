package com.ctqh.mobile.common.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class TxtWriter {
	private TxtWriter() {
	}

	private OutputStream outputStream;
	private OutputStreamWriter streamWriter;
	private BufferedWriter writer;

	public static TxtWriter newWriter(String file, String charset,boolean append)
			throws IOException {
		TxtWriter txtWriter = new TxtWriter();
		txtWriter.outputStream = new FileOutputStream(file,append);
		txtWriter.streamWriter = new OutputStreamWriter(txtWriter.outputStream,
				charset);
		txtWriter.writer = new BufferedWriter(txtWriter.streamWriter);

		return txtWriter;
	}
	public static TxtWriter newWriter(File file, String charset,boolean append)
			throws IOException {
		TxtWriter txtWriter = new TxtWriter();
		txtWriter.outputStream = new FileOutputStream(file,append);
		txtWriter.streamWriter = new OutputStreamWriter(txtWriter.outputStream,
				charset);
		txtWriter.writer = new BufferedWriter(txtWriter.streamWriter);

		return txtWriter;
	}
	public void close() throws IOException {
		this.writer.close();
		this.streamWriter.close();
		this.outputStream.close();
	}

	public void writeLine(String content) throws IOException {
		this.writer.write(content);
		this.writer.write('\n');
		this.writer.flush();
	}
	public void write(String content) throws IOException {
		this.writer.write(content);
		this.writer.flush();
	}
	public void writeLine(char content) throws IOException {
		this.writer.write(content);
		this.writer.write('\n');
		this.writer.flush();
	}
	public void write(char content) throws IOException {
		this.writer.write(content);
		this.writer.flush();
	}
	public void newLine() throws IOException {
		this.writer.write('\n');
		this.writer.flush();
	}
}
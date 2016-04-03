package model;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;


public class FileWriter {
	private PrintWriter pw;

	public FileWriter(String fileName) throws FileNotFoundException {
		FileOutputStream fos = new FileOutputStream(fileName);
		OutputStreamWriter osw = new OutputStreamWriter(fos, Charset.forName("UTF-8"));
		pw = new PrintWriter(osw);
	}

	public void println(Object x) {
		pw.println(x);
	}

	public void close() {
		pw.close();
	}
}
package http.api;

public class FileAppendix {
	private String name;

	public FileAppendix(String fileName) {
		name = fileName;
	}

	public String getFileName() {
		return name;
	}
}

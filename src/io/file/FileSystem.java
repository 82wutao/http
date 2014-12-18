package io.file;

import java.io.File;
import java.io.FilenameFilter;

public class FileSystem {
	public static FileSystem instance = new FileSystem();

	public FileSystem() {
	}

	public static FileSystem getInstance() {
		return instance;
	}

	public static void setInstance(FileSystem obj) {
		instance = obj;
	}

	public String[] listFiles(String dir, String regex) {
		File file = new File(dir);
		if (!file.isDirectory()) {
			return new String[] { file.getPath() };
		}

		final String _regex = regex;

		File[] files=file.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (_regex == null || _regex.equals("")) {
					return true;
				}
				if (name.matches(_regex)) {
					return true;
				}else {
					return false;
				}

			}
		});
		String[] names= new String[files.length];
		for(int i=0;i<files.length;i++){
			names[i]=files[i].getPath();
		}
		
		return names;
	}
}

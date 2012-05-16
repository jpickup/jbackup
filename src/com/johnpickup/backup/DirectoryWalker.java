package com.johnpickup.backup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DirectoryWalker {
	
	private List<Filter> filters = new ArrayList<Filter>();
	private BackupEventListener errorLogger=null;

	public List<File> listEntries(File file) {
		ArrayList<File> result = new ArrayList<File>();
		
		if (file.isFile() && filtersAccept(file)) {
			result.add(file);
		}
		else if (file.isDirectory()) {
			File[] contents = file.listFiles();
			if (contents != null) {
				for (File entry : contents) {
					result.addAll(listEntries(entry));
				}
			}
			else {
				logError(file, "Directory returns null listFiles()");
			}
		}
		else {
			logError(file, "Not a known file type (neither a file nor a directory)");
		}
		return result;
	}

	private void logError(File source, String error) {
		if (errorLogger != null) {
			errorLogger.onError(source, error);
		}
	}

	private boolean filtersAccept(File file) {
		boolean result = true;
		String filename;
		try {
			filename = file.getCanonicalPath();
			for (Filter filter : filters) {
				result = result && filter.accepts(filename);
			}
		} catch (IOException e) {
			result = false;
			logError(file, e.toString());
		}
		return result;		
	}

	public void addFilter(Filter filter) {
		filters.add(filter);
	}

	public static void main(String[] args) {
		try {
			Date start = new Date();
			DirectoryWalker walker = new DirectoryWalker();
			List<File> output;
			output = walker.listEntries(new File("c:/users/john"));
			for (File s : output) {
				System.out.println(s.getCanonicalPath());
			}
			System.out.println(output.size() + " entries");
			Date end = new Date();
			System.out.println("in " + (end.getTime() - start.getTime()) + "ms");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BackupEventListener getErrorLogger() {
		return errorLogger;
	}

	public void setErrorLogger(BackupEventListener errorLogger) {
		this.errorLogger = errorLogger;
	}

}

package com.johnpickup.backup;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BackupReport implements ErrorLogger {

	private List<String> errors = new ArrayList<String>();
	private List<BackupReportItem> items = new ArrayList<BackupReportItem>();
	private Date startTime;

	public void clear() {
		errors.clear();
		items.clear();
		startTime = new Date();
	}

	public void add(BackupReportItem backupReportItem) {
		items.add(backupReportItem);		
	}

	@Override
	public void logError(String error) {
		errors.add(error);
	}

	public long size() {
		return items.size();
	}

	public BackupReportItem get(int index) {
		return items.get(index);
	}

	public void outputTo(PrintStream output) {
		Date endTime = new Date();
		
		if (errors.size() > 0) {
			output.println("*** ERRORS OCCURRED DURING BACKUP ***");
			for (String error : errors) {
				output.println(error);
			}
			output.println("*************************************");
		}
		
		long totalSize = 0;
		if (items.size() > 0) {
			output.println("*** The following files were backed up ***");
			for (BackupReportItem item : items) {
				switch (item.getAction()) {
				case COPIED:
					output.println("Copied  " + item.getSourceFile().getAbsolutePath());
					totalSize += item.getSourceFile().length();
					break;
				case DELETED:
					output.println("Deleted " + item.getTargetFile().getAbsolutePath());
					break;
				}
			}
			output.println("******************************************");
		}
		
		output.print("Processed " + items.size()+ " files totalling ");
		output.print(totalSize/1024 + "kB in ");
		long seconds = (endTime.getTime() - startTime.getTime())/1000;
		output.print(seconds + "s");
		output.println(" (" + (totalSize/1024/seconds) + "kB/s)");
	}
}

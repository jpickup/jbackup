package com.johnpickup.backup;

import java.io.File;

public class BackupReportItem {
	public enum BackupAction {
		COPIED,
		DELETED
	}
	private File sourceFile;
	private File targetFile;
	private BackupAction action;
	
	public BackupReportItem(File sourceFile, File targetFile, BackupAction action) {
		super();
		this.sourceFile = sourceFile;
		this.targetFile = targetFile;
		this.action = action;
	}
	
	public File getSourceFile() {
		return sourceFile;
	}
	
	public File getTargetFile() {
		return targetFile;
	}

	public BackupAction getAction() {
		return action;
	}

	@Override
	public String toString() {
		return "BackupReportItem [sourceFile=" + sourceFile + ", targetFile="
				+ targetFile + ", action=" + action + "]";
	}

}

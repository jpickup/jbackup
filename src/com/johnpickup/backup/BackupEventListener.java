package com.johnpickup.backup;

import java.io.File;

public interface BackupEventListener {
	void onCopy(File source, File target);
	void onDelete(File target);
	void onError(File source, String error);
	void onStart();
	void onScanComplete();
	void onBackupComplete();
}

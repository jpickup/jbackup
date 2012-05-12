package com.johnpickup.backup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.beust.jcommander.JCommander;

public class Backup {
	private static final String CATALOG_FILE = "jbackup.cat";
	private String sourcePath;
	private String destinationPath;
	private List<String> exclusions = new ArrayList<String>();
	private BackupReport backupReport = new BackupReport();
	private boolean showProgress = true;
	int notificationFrequency = 100;
	
	public void performBackup() throws IOException {
		backupReport.clear();
		File destFile = new File(destinationPath);
		if (!destFile.exists()) {
			destFile.mkdirs();
		}
		RegexExclusionFilter catalogFilter = new RegexExclusionFilter();
		catalogFilter.addExclusions(exclusions);
		FileCatalog newCatalog = buildCatalog(sourcePath, catalogFilter);
		FileCatalog oldCatalog = loadCatalog(destinationPath);
		CatalogComparer differences = new CatalogComparer(oldCatalog, newCatalog);
		if (showProgress) {
			System.out.println("Copying " + differences.getAdded().size() + " new files, " + 
					differences.getChanged().size() + " changed files and removing " + 
					differences.getRemoved().size() + " deleted files.");
		}
		backupReport.doneCatalog();
		copyFiles(sourcePath, differences.getAdded(), destinationPath);
		copyFiles(sourcePath, differences.getChanged(), destinationPath);
		deleteFiles(differences.getRemoved(), destinationPath);
		saveCatalog(newCatalog, destinationPath);
	}

	private void deleteFiles(Set<String> removed, String targetPath) {
		int i = 0;
		for (String filename : removed) {
			File fileToDelete = new File(targetPath + File.separator + filename);
			try {
				FileUtils.forceDelete(fileToDelete);
				backupReport.add(new BackupReportItem(null, fileToDelete, BackupReportItem.BackupAction.DELETED));
			} catch (IOException e) {
				backupReport.logError("Failed to delete " + fileToDelete.getAbsolutePath() + " : " + e.toString());
			}
			if ((++i)%notificationFrequency == 0) {
				System.out.print(".");
			}
		}
	}

	private void copyFiles(String sourcePath, Set<String> added, String targetPath) {
		int i = 0;
		for (String filename : added) {
			File srcFile = new File(sourcePath + File.separator + filename);
			File destFile = new File(targetPath + File.separator + filename);
			try {
				FileUtils.copyFile(srcFile, destFile);
				backupReport.add(new BackupReportItem(srcFile, destFile, BackupReportItem.BackupAction.COPIED));
			} catch (IOException e) {
				backupReport.logError("Failed to copy " + srcFile.getAbsolutePath() + " : " + e.toString());
			}
			if ((++i)%notificationFrequency == 0) {
				System.out.print(".");
			}
		}
	}

	private FileCatalog loadCatalog(String catalogPath) throws IOException {
		FileCatalog catalog = new FileCatalog();
		String catalogFilename = catalogPath + File.separator + CATALOG_FILE;
		catalog.loadFromFile(catalogFilename);
		return catalog;
	}

	private void saveCatalog(FileCatalog catalog, String catalogPath) throws IOException {
		String catalogFilename = catalogPath + File.separator + CATALOG_FILE;
		catalog.saveToFile(catalogFilename);
	}

	private FileCatalog buildCatalog(String sourcePath,
			Filter filter) throws IOException {
		FileCatalog catalog = new FileCatalog();
		DirectoryWalker walker = new DirectoryWalker();
		walker.setErrorLogger(backupReport);
		walker.addFilter(filter);
		List<File> entries = walker.listEntries(new File(sourcePath));
		for (File file : entries) {
			String relativePath = ResourceUtils.getRelativePath(file.getAbsolutePath(), sourcePath, File.separator);
			catalog.add(relativePath, file.length(), file.lastModified());
		}
		
		return catalog;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public String getDestinationPath() {
		return destinationPath;
	}

	public void setDestinationPath(String destinationPath) {
		this.destinationPath = destinationPath;
	}

	public List<String> getExclusions() {
		return exclusions;
	}

	public void setExclusions(List<String> exclusions) {
		this.exclusions = exclusions;
	}

	public BackupReport getBackupReport() {
		return backupReport;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BackupArguments arguments = new BackupArguments();
		JCommander jcommander = new JCommander(arguments);
		try {
			jcommander.setProgramName("TestCommandLine");
			jcommander.parse(args);
			if (arguments.getDirectories().size() != 2) {
				throw new RuntimeException();
			}
			String sourcePath = arguments.getDirectories().get(0);
			String destinationPath = arguments.getDirectories().get(1);
			
			System.out.println("Backing up " +  sourcePath + " to " + destinationPath);
			System.out.println("Excluding " + arguments.getExlusions());

			Backup backup = new Backup();
			backup.setSourcePath(sourcePath);
			backup.setDestinationPath(destinationPath);
			backup.setExclusions(arguments.getExlusions());
			try {
				backup.performBackup();
			} catch (Exception e) {
				System.err.println("*** UNEXPECTED ERROR DURING BACKUP ***");
				System.err.print(e.toString());
				e.printStackTrace();
				
				System.exit(1);
			}
			if (!arguments.isNoreport()) {
				backup.getBackupReport().outputTo(System.out);
			}
		}
		catch (Exception e) {
			jcommander.usage();
		}

	}
}

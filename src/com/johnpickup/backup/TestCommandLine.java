package com.johnpickup.backup;

import com.beust.jcommander.JCommander;

public class TestCommandLine {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BackupArguments arguments = new BackupArguments();
		JCommander jcommander = new JCommander(arguments);
		try {
			jcommander.setProgramName("TestCommandLine");
			jcommander.parse(args);
			if (arguments.getDirectories().size() != 2) {
				throw new RuntimeException();
			}
			String sourceDir = arguments.getDirectories().get(0);
			String destDir = arguments.getDirectories().get(1);
			
			System.out.println("Backing up " +  sourceDir + " to " + destDir);
			System.out.println("Excluding " + arguments.getExlusions());
		}
		catch (Exception e) {
			jcommander.usage();
		}
		
	}

}

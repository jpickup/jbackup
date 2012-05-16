package com.johnpickup.backup;

public class Utils {
	public static String secondsToTimeString(long seconds) {
		long h = seconds / (60 * 60); 
		long m = (seconds/60) % 60;
		long s = seconds % 60;
		return String.format("%02dh%02dm%02ds", h, m, s);
	}
}

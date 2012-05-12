package com.johnpickup.backup;

public class StdoutErrorLogger implements ErrorLogger {

	@Override
	public void logError(String error) {
		System.err.println(error);
	}

}

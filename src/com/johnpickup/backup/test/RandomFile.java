package com.johnpickup.backup.test;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class RandomFile {

	/**
	 * Create a file with random contents of the given size
	 * @param name the name of the file to create (full path)
	 * @param size the size in units of 1kB 
	 * @throws IOException
	 */
	public RandomFile(String name, long size) throws IOException {
		FileOutputStream fos = new FileOutputStream(name);
		BufferedOutputStream s = new BufferedOutputStream(fos);
		byte[] b = new byte[1024];
		for (int i=0; i<1024;i++) {
			b[i]=(byte)(Math.random() * 256 - 128);
		}
		for (long n=0; n<size; n++) {
			s.write(b);
		}
		s.close();
		fos.close();
	}

}

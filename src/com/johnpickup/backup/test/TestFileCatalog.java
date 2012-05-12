package com.johnpickup.backup.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.johnpickup.backup.FileCatalog;

public class TestFileCatalog {

	@Test
	public void testEmptyPersist() throws IOException {
		FileCatalog cat = new FileCatalog();
		String filename = File.createTempFile("testEmpty", ".cat").getAbsolutePath();
		cat.saveToFile(filename);
		FileCatalog cat2 = new FileCatalog();
		cat2.loadFromFile(filename);
		assertEquals(cat, cat2);
	}
	
	@Test
	public void testPersist()  throws IOException {
		FileCatalog cat = new FileCatalog();
		String filename = File.createTempFile("test", ".cat").getAbsolutePath();
		for (int i=0; i<100; i++) {
			cat.add("test"+i, i, 1000+i);
		}
		cat.saveToFile(filename);
		FileCatalog cat2 = new FileCatalog();
		cat2.loadFromFile(filename);
		assertEquals(cat, cat2);
	}

}

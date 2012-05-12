package com.johnpickup.backup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FileCatalog implements Iterable<String> {
	private static final char NEWLINE = '\n';
	private static final char TAB = '\t';
	private Map<String, FileCharacteristics> files = new HashMap<String, FileCharacteristics>();
	
	public void add(String name, long size, long modified) {
		files.put(name, new FileCharacteristics(modified, size));
	}
	
	public long getSize(String name) {
		return files.get(name).getSize();
	}
	
	public long getModified(String name) {
		return files.get(name).getModified();
	}
	
	public FileCharacteristics getCharacteristics(String name) {
		return files.get(name);	
	}

	@Override
	public Iterator<String> iterator() {
		return files.keySet().iterator();
	}

	public boolean contains(String name) {
		return files.containsKey(name);
	}
	
	public void saveToFile(String filename) throws IOException {
		FileWriter writer = new FileWriter(filename);
		
		for (String name : files.keySet()) {
			StringBuilder buff = new StringBuilder();
			buff.append(name).append(TAB);
			FileCharacteristics fileCharacteristics = files.get(name);
			buff.append(fileCharacteristics.getSize()).append(TAB);
			buff.append(fileCharacteristics.getModified()).append(NEWLINE);
			writer.append(buff.toString());
		}
		writer.close();
	}

	public void loadFromFile(String filename) throws IOException {
		files.clear();
		if ((!new File(filename).exists())) return;
		
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line = reader.readLine();
		StringBuilder sb = new StringBuilder();
		sb.append(TAB);
		String delim = sb.toString();
		while (line != null) {
			String[] parts = line.split(delim);
			if (parts.length != 3) {
				throw new RuntimeException("Invalid entry in catalog file " + filename + " expected an entry but got " + line);
			}
			add(parts[0], Long.valueOf(parts[1]), Long.valueOf(parts[2]));
			line = reader.readLine();
		}
		reader.close();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((files == null) ? 0 : files.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileCatalog other = (FileCatalog) obj;
		if (files == null) {
			if (other.files != null)
				return false;
		} else if (!files.equals(other.files))
			return false;
		return true;
	}
}

package com.johnpickup.backup;

public class FileCharacteristics {
	private long modified;
	private long size;
	public long getModified() {
		return modified;
	}
	public long getSize() {
		return size;
	}
	public FileCharacteristics(long modified, long size) {
		super();
		this.modified = modified;
		this.size = size;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (modified ^ (modified >>> 32));
		result = prime * result + (int) (size ^ (size >>> 32));
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
		FileCharacteristics other = (FileCharacteristics) obj;
		if (modified != other.modified)
			return false;
		if (size != other.size)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "FileCharacteristics [modified=" + modified + ", size=" + size
				+ "]";
	}
	
}

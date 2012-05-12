package com.johnpickup.backup;

import java.util.HashSet;
import java.util.Set;

public class CatalogComparer {
	private FileCatalog from;
	private FileCatalog to;
	private Set<String> added = new HashSet<String>();
	private Set<String> removed = new HashSet<String>();
	private Set<String> changed = new HashSet<String>();

	public CatalogComparer(FileCatalog from, FileCatalog to) {
		this.from = from;
		this.to = to;
		compare();
	}
	
	private void compare() {
		for (String name : from) {
			if (to.contains(name)) {
				if (!from.getCharacteristics(name).equals(to.getCharacteristics(name))) {
					changed.add(name);
				}
			}
			else {
				removed.add(name);
			}
		}
		for (String name : to) {
			if (!from.contains(name)) {
				added.add(name);
			}
		}
	}

	public FileCatalog getFrom() {
		return from;
	}

	public FileCatalog getTo() {
		return to;
	}

	public Set<String> getAdded() {
		return added;
	}

	public Set<String> getRemoved() {
		return removed;
	}

	public Set<String> getChanged() {
		return changed;
	}

}

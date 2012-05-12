package com.johnpickup.backup;

import java.util.ArrayList;
import java.util.List;

public class RegexExclusionFilter implements Filter {
	private List<String> exclusions = new ArrayList<String>();

	public void addExclusion(String regex) {
		exclusions.add(regex);
	}

	public void addExclusions(List<String> exclusions) {
		this.exclusions.addAll(exclusions);
	}

	@Override
	public boolean accepts(String value) {
		boolean exclude = false;
		for (String regex : exclusions) {
			exclude |= value.matches(regex);
		}
		return !exclude;
	}
}

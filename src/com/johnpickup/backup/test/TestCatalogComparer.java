package com.johnpickup.backup.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.johnpickup.backup.CatalogComparer;
import com.johnpickup.backup.FileCatalog;

public class TestCatalogComparer {

	@Test
	public void testEmpty() {
		FileCatalog from = new FileCatalog();
		FileCatalog to = new FileCatalog();
		CatalogComparer comparer = new CatalogComparer(from, to);
		assertEquals(0, comparer.getAdded().size());
		assertEquals(0, comparer.getChanged().size());
		assertEquals(0, comparer.getRemoved().size());
	}

	@Test
	public void testAddition() {
		FileCatalog from = new FileCatalog();
		FileCatalog to = new FileCatalog();
		to.add("testAdd", 100L, 200L);
		CatalogComparer comparer = new CatalogComparer(from, to);
		assertEquals(1, comparer.getAdded().size());
		assertEquals(0, comparer.getChanged().size());
		assertEquals(0, comparer.getRemoved().size());
	}

	@Test
	public void testRemoval() {
		FileCatalog from = new FileCatalog();
		FileCatalog to = new FileCatalog();
		from.add("testRemoval", 100L, 200L);
		CatalogComparer comparer = new CatalogComparer(from, to);
		assertEquals(0, comparer.getAdded().size());
		assertEquals(0, comparer.getChanged().size());
		assertEquals(1, comparer.getRemoved().size());
	}

	@Test
	public void testChangeDate() {
		FileCatalog from = new FileCatalog();
		FileCatalog to = new FileCatalog();
		from.add("testChangeDate", 100L, 200L);
		to.add("testChangeDate", 100L, 201L);
		CatalogComparer comparer = new CatalogComparer(from, to);
		assertEquals(0, comparer.getAdded().size());
		assertEquals(1, comparer.getChanged().size());
		assertEquals(0, comparer.getRemoved().size());
	}

	@Test
	public void testChangeSize() {
		FileCatalog from = new FileCatalog();
		FileCatalog to = new FileCatalog();
		from.add("testChangeSize", 100L, 200L);
		to.add("testChangeSize", 101L, 200L);
		CatalogComparer comparer = new CatalogComparer(from, to);
		assertEquals(0, comparer.getAdded().size());
		assertEquals(1, comparer.getChanged().size());
		assertEquals(0, comparer.getRemoved().size());
	}
}

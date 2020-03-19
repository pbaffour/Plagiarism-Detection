import static org.junit.Assert.*;

import java.io.*;
import java.nio.*;
import java.util.*;

import org.junit.Test;

public class CharCounterTest {

	/**
	 * Tests the constructor in CharCounter class.
	 */
	@Test
	public void constructorTest() {
		CharCounter cc = new CharCounter();
		assertNotNull(cc);
	}

	CharCounter cc = new CharCounter();

	/**
	 * Tests getTable() method in CharCounter class.
	 */
	@Test
	public void getTableTest() {
		Map<Integer, Integer> output = cc.getTable();
		assertNotNull(output);
	}

	/**
	 * Tests getCount() method in CharCounter class. Tests for throw of Illegal
	 * Argument Exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getCountException() {
		int output = cc.getCount(8);
	}

	/**
	 * Tests getCount() method in CharCounter class.
	 */
	@Test
	public void getCountTest() {
		cc.getTable().put(10, 10);
		assertEquals(10, cc.getCount(10));
	}

	/**
	 * Tests set() method in CharCounter class.
	 */
	@Test
	public void setTest() {
		cc.set(50, 25);
		assertEquals(25, cc.getCount(50));

	}

	/**
	 * Tests add() method in CharCounter class.
	 */
	@Test
	public void addTest() {
		cc.add(100);
		assertEquals(1, cc.getCount(100));

		cc.add(100);
		assertEquals(2, cc.getCount(100));

		cc.add(150);
		assertEquals(1, cc.getCount(150));
		assertEquals(2, cc.getCount(100));

	}

	/**
	 * Tests clear() method in CharCounter class. Tests for throw of Illegal
	 * Argument Exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void clearException() {
		cc.add(10);
		cc.add(20);
		cc.clear();
		cc.getCount(10);
	}

	/**
	 * Tests clear() method in CharCounter class.
	 */
	@Test
	public void clearTest() {
		cc.add(10);
		cc.add(20);
		cc.add(30);
		cc.add(40);
		cc.clear();
		assertEquals(0, cc.getTable().size());
	}

	/**
	 * Tests countAll() method in CharCounter class.
	 */
	@Test
	public void countAllTest() throws IOException {

		String testString = "This is a test!";
		InputStream bis = new ByteArrayInputStream(testString.getBytes("UTF-8"));
		int output = cc.countAll(bis);
		assertEquals(testString.length(), output);

	}

}

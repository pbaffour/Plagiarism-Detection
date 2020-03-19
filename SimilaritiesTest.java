import static org.junit.Assert.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.junit.Test;

public class SimilaritiesTest {

	//~ CONSTRUCTOR	
	@Test
	public void TestSimilarities() {
		Similarities s = new Similarities("file1","file2");
		assertNotNull(s);
	}
	
	Similarities s = new Similarities("file1","file2");
	
	//~ PUBLIC FUNCTION: getFile1()
	@Test
	public void TestgetFile1() {
		String output = s.getFile1();
		assertTrue(output.equals("file1"));
	}
	
	//~ PUBLIC FUNCTION: getFile2()
	@Test
	public void TestgetFile2() {
		String output = s.getFile2();
		assertTrue(output.equals("file2"));
	}
	
	//~ PUBLIC FUNCTION: getCount()
	@Test
	public void TestgetCount() {
		assertEquals(1,s.getCount());
	}
	
	//~ PUBLIC FUNCTION: setCount()
	@Test
	public void TestsetCount() {
		s.setCount(100);
		assertEquals(100,s.getCount());
	}
	
	//~ PUBLIC FUNCTION: compareTo()
	@Test
	public void compareTo() {		
		Similarities t = new Similarities("file1","file3");
		assertTrue(s.compareTo(t)<0);
		
		t = new Similarities("file0","file1");
		assertTrue(s.compareTo(t)>0);
		
		s.setCount(100);
		assertTrue(s.compareTo(t)<0);
		
		
	}
		
}


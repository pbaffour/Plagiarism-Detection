import static org.junit.Assert.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.junit.Test;

public class TupleTest {

	//~ CONSTRUCTOR	
	@Test
	public void TestTuple() {
		Tuple<String,Integer> t = new Tuple<String,Integer>("file1",5);
		assertNotNull(t);
	}
	
	Tuple<String,Integer> t = new Tuple<String,Integer>("file1",5);
	
	//~ PUBLIC FUNCTION: getLeft()
	@Test
	public void TestgetLeft() {
		String output = t.getLeft();
		assertTrue(output.equals("file1"));
	}
	
	//~ PUBLIC FUNCTION: getRight()
	@Test
	public void TestgetRight() {
		int output = t.getRight();
		assertEquals(5,output);
	}
	
	//~ PUBLIC FUNCTION: setLeft()
	@Test
	public void TestsetLeft() {
		t.setLeft("file2");
		assertTrue(t.getLeft().equals("file2"));
	}
	
	//~ PUBLIC FUNCTION: getRight()
	@Test
	public void TestsetRight() {
		t.setRight(50);
		int output = t.getRight();
		assertEquals(50,output);
	}
		
}


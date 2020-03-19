import static org.junit.Assert.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.junit.Test;

public class DocumentIteratorTest {

	//~ CONSTRUCTOR
	
	@Test
	public void TestDocumentIterator() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\13023\\Downloads\\testDirectory\\pledge1.txt"));
			DocumentIterator DI = new DocumentIterator(br,3);
			assertNotNull(DI);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//~ HELPER FUNCTION: queueToString()
	@Test
	public void TestqueueToString() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\13023\\Downloads\\testDirectory\\pledge1.txt"));
			DocumentIterator DI = new DocumentIterator(br,3);
			
			// edge case: if null reference, returns null
			String output = DI.queueToString(null);
			assertNull(output);
			
			// edge case: if queue empty, returns null
			Queue<String> testQueue = new LinkedList<String>();
			output = DI.queueToString(testQueue);
			assertNull(output);
			
			// test: all characters convert to lower case
			testQueue.add("HI");
			testQueue.add("MY");
			testQueue.add("DEAR");
			output = DI.queueToString(testQueue);
			assertTrue(output.equals("himydear"));
			
			// test: queue of size 1 is properly converted
			testQueue.clear();
			testQueue.add("hello");
			output = DI.queueToString(testQueue);
			assertTrue(output.equals("hello"));
		
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//~ PUBLIC FUNCTION: hasNext()
	
	@Test
	public void TesthasNext() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\13023\\Documents\\emptyFile.txt"));
			DocumentIterator DI = new DocumentIterator(br,3);
			
			// edge case: empty file returns false for hasNext()
			assertFalse(DI.hasNext());
		
			// regular case
			BufferedReader tr = new BufferedReader(new FileReader("C:\\Users\\13023\\Documents\\testFile.txt"));
			DI = new DocumentIterator(tr,3);
			
			assertTrue(DI.hasNext());
			DI.next();
			assertFalse(DI.hasNext());
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//~ PUBLIC FUNCTION: next()
	
	@Test
	public void TestnextEmptyFile() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\13023\\Documents\\emptyFile.txt"));
			DocumentIterator DI = new DocumentIterator(br,3);
			
			// edge case: empty file 
			String output = DI.next();
			assertTrue(output.equals(""));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void Testnext() {
		try {
				
			// edge case: file's word count < n
			BufferedReader tr = new BufferedReader(new FileReader("C:\\Users\\13023\\Downloads\\testDirectory\\pledge2.txt"));
			DocumentIterator DI = new DocumentIterator(tr,9);
			
			String output = DI.next();
			assertTrue(output.equals(""));
			
			// regular case: file's word count >= n
			tr = new BufferedReader(new FileReader("C:\\Users\\13023\\Downloads\\testDirectory\\pledge1.txt"));
			DI = new DocumentIterator(tr,3);
			
			output = DI.next();
			assertTrue(output.equals("ipledgeallegiance"));
			output = DI.next();
			assertTrue(output.equals("pledgeallegianceto"));
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
		
}


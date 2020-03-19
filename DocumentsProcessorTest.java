import static org.junit.Assert.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.junit.Test;

public class DocumentsProcessorTest {

	//~ CONSTRUCTOR
	
	@Test
	public void TestDocumentsProcessor() {
		DocumentsProcessor DP = new DocumentsProcessor();
		assertNotNull(DP);
	}
	
	DocumentsProcessor DP = new DocumentsProcessor();
	
	//~ PROCESSING DOCUMENTS

	@Test(expected=NullPointerException.class)
	public void TestprocessDocuments_nullDirectory() {
		Map<String,List<String>> output = new HashMap<String,List<String>>();
		output = DP.processDocuments(null,5);
	}
	
	@Test
	public void TestprocessDocuments_invalidDirectory() {
		// if  invalid directory, returns null
		Map<String,List<String>> output = new HashMap<String,List<String>>();
				
		String directoryPath = "C:\\Users\\13023\\Downloads\\big_doc_se";
		output = DP.processDocuments(directoryPath, 5);
		assertNull(output);
		
		// if empty directory, returns null
		directoryPath = "C:\\Users\\13023\\Downloads\\emptyDirectory";
		output = DP.processDocuments(directoryPath, 5);
		assertNull(output);

	}
	
	@Test
	public void TestprocessDocuments() {
		Map<String,List<String>> output = new HashMap<String,List<String>>();
		output = DP.processDocuments("C:\\Users\\13023\\Downloads\\testDirectory", 5);
		
		// test: map not null
		assertNotNull(output);
				
		// test: map doesn't contain keys for files with word count < n
		assertFalse(output.containsKey("pledge2.txt"));
		
		// test: valid files have key in map
		assertEquals(6,output.keySet().size()); 
		
		// test: all keys contain correct names for files
		assertTrue(output.containsKey("pledge1.txt"));
		assertTrue(output.containsKey("pledge3.txt"));
		assertTrue(output.containsKey("pledge4.txt"));
		assertTrue(output.containsKey("repeated_sentence1.txt"));
		assertTrue(output.containsKey("repeated_sentence2.txt"));
		assertTrue(output.containsKey("star_spangled_banner.txt"));


		// test: each value for key entry contains all possible n-word sequences
		assertEquals(16,output.get("repeated_sentence1.txt").size());		
		
	}
	
	// <INSERT> Once processDocuments() works, create dummy map here (call "testMap")	
	Map<String,List<String>> testMap = DP.processDocuments("C:\\Users\\13023\\Downloads\\testDirectory", 5);
			
	//~ STORE N WORD SEQUENCES
	@Test 
	public void TeststoreNWordSequences() {
		List<Tuple<String,Integer>> outputTuples = new ArrayList<Tuple<String,Integer>>();
		
		// if null reference for map, returns null
		outputTuples = DP.storeNWordSequences(null,"C:\\Users\\13023\\Downloads\\outputFile\\outputFile.txt");
		assertNull(outputTuples);
		
		// if null reference for file path, returns null
		outputTuples = DP.storeNWordSequences(testMap,null);
		assertNull(outputTuples);
		
		// if null reference for map AND file path, returns null
		outputTuples = DP.storeNWordSequences(null,null);
		assertNull(outputTuples);
		
		// test: tuple list contains tuple for each file
		outputTuples = DP.storeNWordSequences(testMap,"C:\\Users\\13023\\Downloads\\outputFile\\outputFile.txt");
		assertEquals(testMap.entrySet().size(),outputTuples.size());
		
		// test: tuple's byte size for file is correct
		Tuple<String,Integer> testTuple = outputTuples.get(1);
		int byte_count = 0;
		for (String str : testMap.get(testTuple.getLeft())) {
			byte[] arr = str.getBytes();
			byte_count += arr.length + 1;
		}
		assertEquals(byte_count,(int) testTuple.getRight());
		
		// test: output file contains all n-word sequences from all files 
		byte_count = 0;
		for (Map.Entry<String,List<String>> entry : testMap.entrySet()) {
			for (String str : entry.getValue()) {
				byte[] arr = str.getBytes();
				byte_count += arr.length + 1;
			}
		}
			
		DP.storeNWordSequences(testMap,"C:\\Users\\13023\\Downloads\\outputFile\\outputFile.txt");
		try {
			byte[] outputBytes = Files.readAllBytes(Paths.get("C:\\Users\\13023\\Downloads\\outputFile\\outputFile.txt"));
			int output_size = outputBytes.length;
			assertEquals(byte_count,output_size);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		
	}
	
	// <INSERT> Once storeNWordSequences() works, create dummy tuple list here (call "testTuples")
	List<Tuple<String,Integer>> testTuples = DP.storeNWordSequences(testMap,"C:\\Users\\13023\\Downloads\\outputFile\\outputFile.txt");

	//~ COMPUTE SIMILARITIES
	
	@Test
	public void TestcomputeSimilarities() {
		Comparator<Similarities> comp =
	            new Comparator<Similarities>() {

					@Override
					public int compare(Similarities o1, Similarities o2) {
						int check = o1.getFile1().compareTo(o2.getFile1());
						if (check == 0) {
							return o1.getFile2().compareTo(o2.getFile2());
						}
						return check;
					}
	            };
	            
	    TreeSet<Similarities> output = new TreeSet<>(comp);
	    
	    output = DP.computeSimilarities("C:\\Users\\13023\\Downloads\\outputFile\\outputFile.txt",testTuples);
	
	   for (Similarities s: output) {
	    	//System.out.println(s.getFile1() + s.getFile2());
	    }
	    
	}
	
	//~ PROCESS AND STORE 
	
	@Test(expected=NullPointerException.class) 
	public void TestprocessAndStore_nullDirectory() {
		List<Tuple<String,Integer>> outputTuples = new ArrayList<Tuple<String,Integer>>();
		
		// if null reference for directory, returns null
		outputTuples = DP.processAndStore(null,"C:\\Users\\13023\\Downloads\\outputFile\\outputFile.txt",5);
	}

	@Test(expected=NullPointerException.class) 
	public void TestprocessAndStore_nullFilePath() {
		List<Tuple<String,Integer>> outputTuples = new ArrayList<Tuple<String,Integer>>();
		
		// if null reference for file path, returns null
		outputTuples = DP.processAndStore("C:\\Users\\13023\\Downloads\\testDirectory",null,5);
	}
	
	@Test(expected=NullPointerException.class) 
	public void TestprocessAndStore_nullBothParams() {
		List<Tuple<String,Integer>> outputTuples = new ArrayList<Tuple<String,Integer>>();
		
		// if null reference for directory AND file path, returns null
		outputTuples = DP.processAndStore(null,null,5);
	}
	
	
	@Test 
	public void TestprocessAndStore() {
		List<Tuple<String,Integer>> outputTuples = new ArrayList<Tuple<String,Integer>>();
	
		// invalid directory
		outputTuples = DP.processAndStore("asfjdhlsajf","C:\\Users\\13023\\Downloads\\outputFile\\outputFile2.txt",5);
		assertTrue(outputTuples==null);
		
		// test: tuple list contains tuple for each file
		outputTuples = DP.processAndStore("C:\\Users\\13023\\Downloads\\testDirectory","C:\\Users\\13023\\Downloads\\outputFile\\outputFile2.txt",5);
		assertEquals(testMap.entrySet().size(),outputTuples.size());
		
		// test: tuple's byte size for file is correct
		Tuple<String,Integer> testTuple = outputTuples.get(0);
		int byte_count = 0;
		for (String str : testMap.get(testTuple.getLeft())) {
			byte[] arr = str.getBytes();
			byte_count += arr.length + 1;
		}
		assertEquals(byte_count,(int) testTuple.getRight());
		
		// test: output file contains all n-word sequences from all files 
		byte_count = 0;
		for (Map.Entry<String,List<String>> entry : testMap.entrySet()) {
			for (String str : entry.getValue()) {
				byte[] arr = str.getBytes();
				byte_count += arr.length + 1;
			}
		}
			
		DP.processAndStore("C:\\Users\\13023\\Downloads\\testDirectory","C:\\Users\\13023\\Downloads\\outputFile\\outputFile2.txt",5);
		try {
			byte[] outputBytes = Files.readAllBytes(Paths.get("C:\\Users\\13023\\Downloads\\outputFile\\outputFile2.txt"));
			int output_size = outputBytes.length;
			assertEquals(byte_count,output_size);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
		
}


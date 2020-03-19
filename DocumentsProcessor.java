import java.io.*;
import java.util.*;

public class DocumentsProcessor implements IDocumentsProcessor {
	
	/**
	 * Constructor
	 */
	public DocumentsProcessor() {
		
	}
	
	@Override
	public Map<String, List<String>> processDocuments(String directoryPath, int n) {
				
		// init output
		HashMap<String,List<String>> output = new HashMap<String,List<String>>();
		
		// init directory iterator
		File dir = new File(directoryPath);
		
		if (dir.isDirectory()) {
			File[] dirList = dir.listFiles();
			
			// if empty directory, return null
			if (dirList.length==0) {
				return null;
			}
			
			try {
				for (File doc : dirList) {
					
					// init doc iterator
					BufferedReader br = new BufferedReader(new FileReader(doc));
					DocumentIterator iter = new DocumentIterator(br,n);	
					
					// generate list of n-word sequences
					List<String> sequenceList = new ArrayList<String>();
					
					String nextSequence = iter.next();					
					
					while (!nextSequence.equals("")) {
						sequenceList.add(nextSequence);
						nextSequence = iter.next();
					}

					if (sequenceList.size()<1) {
						continue;
					}
					
					output.put(doc.getName(),sequenceList);
					br.close();
				}
			}
			
			catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		    
		else {
		    return null;
		}
		
		return output;
	}


	@Override
	public List<Tuple<String, Integer>> storeNWordSequences(Map<String, List<String>> docs, String nwordFilePath) {
		
		if (docs == null || nwordFilePath == null) {
			return null;
		}
		
		// init output collection
		ArrayList<Tuple<String,Integer>> output = new ArrayList<Tuple<String,Integer>>();
		
		// iterate through documents (keys) in map
		try {
			
			FileOutputStream bw = new FileOutputStream(nwordFilePath);

			for (Map.Entry<String, List<String>> entry : docs.entrySet()) {
				int byte_count = 0; 
				
				// when you use a BufferedWriter, you may be using 2 bytes to store a char
				// when you use a ByteStream (FileOutputStream), you are storing a char in one byte 
				
				byte[] spaceToByte = " ".getBytes();
				
				// init n-word sequence iterator
				for (String str : entry.getValue()) {
					byte[] strToByte = str.getBytes();
					bw.write(strToByte);
					bw.write(spaceToByte);
					byte_count += strToByte.length + spaceToByte.length;
				}
				
				Tuple<String,Integer> nextTuple = new Tuple<String,Integer>(entry.getKey(),byte_count);
				output.add(nextTuple); // QUESTION: DOES THIS MAINTAIN ORDER??				
			}
			
			bw.close();
		}
		
		catch (IOException e) {
			e.printStackTrace();	
		}		

		return output;
	}

	@Override
	public TreeSet<Similarities> computeSimilarities(String nwordFilePath, List<Tuple<String, Integer>> fileindex) {
		
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
	    
		HashMap<String,ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
		HashMap<String,Similarities> pairs = new HashMap<String,Similarities>();
		
		try {
			FileInputStream input = new FileInputStream(nwordFilePath);
			
			for (Tuple<String,Integer> tuple : fileindex) {
				byte[] arrayOfBytes = new byte[(int) tuple.getRight()]; 
				input.read(arrayOfBytes);
								
				String allSequences = new String(arrayOfBytes); 
				
				String[] sequencesList = allSequences.split(" ");
				
				for (int i = 0; i < sequencesList.length; i++) {
					if (!map.containsKey(sequencesList[i])) {
						map.put(sequencesList[i],new ArrayList<String>());
						map.get(sequencesList[i]).add((String) tuple.getLeft());
					}
					else { // if key exists
						// if n-word sequence is duplicate in file, continue
						if ( map.get(sequencesList[i]).contains( (String) tuple.getLeft() ) )  {
							continue;
						}
						// otherwise, create/update Similarities object for other files stored in List<String> 
						for (String str : map.get(sequencesList[i])) {
							// create another hashmap, where the key is a concatenated string of two file names,
							// and the value is a reference to the Similarities object 
							String concatenatedPair = str + tuple.getLeft();
							if (pairs.containsKey(concatenatedPair)) {
								int newCount = pairs.get(concatenatedPair).getCount() + 1;
								pairs.get(concatenatedPair).setCount(newCount);
							}
							else {
								Similarities next = new Similarities(str,tuple.getLeft());
								pairs.put(concatenatedPair,next);
								output.add(next);
							}
						}
						
					}
				}		
			}
			input.close();
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return output;
	}

	@Override
	public void printSimilarities(TreeSet<Similarities> sims, int threshold) {
		Comparator<Similarities> comp =
	            new Comparator<Similarities>() {

					@Override
					public int compare(Similarities o1, Similarities o2) {
						int check = o2.getCount() - o1.getCount();
						if (check == 0) {
							check = o1.getFile1().compareTo(o2.getFile1());
								if (check == 0) {
									return o1.getFile2().compareTo(o2.getFile2());
								}
							
						}
						return check;
					}	
		};
		
		TreeSet<Similarities> simsCopy = new TreeSet<Similarities>(comp);
		simsCopy.addAll(sims);
		
		// Obtain the objects that satisfy the threshold requirement, and 
		// print them in the correct order.       
        
		for (Similarities s : simsCopy) {
			if (s.getCount() >= threshold) {
				System.out.print(s.getCount() + ": " + s.getFile1() + ", " + s.getFile2());
			}
		}

		return;
		
	}
	
	List<Tuple<String, Integer>> processAndStore (String directoryPath, String sequenceFile, int n) {
		// init output collection
		ArrayList<Tuple<String,Integer>> output = new ArrayList<Tuple<String,Integer>>();
		
		// init directory iterator
		File dir = new File(directoryPath);
		
		if (dir.isDirectory()) {
			
			try {		
				// init output stream
				FileOutputStream bw = new FileOutputStream(sequenceFile);
				
				// init directory iterator
				File[] dirList = dir.listFiles();
				for (File doc : dirList) {
				
					// init input stream
					BufferedReader br = new BufferedReader(new FileReader(doc));
					DocumentIterator iter = new DocumentIterator(br,n);	
					
					String nextSequence = iter.next();
					
					if (nextSequence.equals("")) {
						continue;
					}
					
					int byte_count = 0;
					byte[] spaceToByte = " ".getBytes();
					
					while (!nextSequence.equals("")) {
						byte_count += nextSequence.getBytes().length + 1;
						bw.write(nextSequence.getBytes());
						bw.write(spaceToByte);						
						nextSequence = iter.next();
					}
										
					Tuple<String,Integer> nextTuple = new Tuple<String,Integer>(doc.getName(),byte_count);
					output.add(nextTuple);
					br.close();
				}
				
				bw.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		else {
		    return null;
		}
				
		return output;
		
	}

}

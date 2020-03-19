import java.io.IOException;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class DocumentIterator
    implements Iterator<String>
{

    private Reader r;
    private int    c = -1;
    private int n = 1;
    private int loop;
    private Queue nSequenceQueue;


    public DocumentIterator(Reader r, int n)
    {
    	this.n = n;
        this.r = r;
        skipNonLetters();
        nSequenceQueue = new ArrayDeque<String>(n);
    }


    private void skipNonLetters()
    {
        try
        {
            this.c = this.r.read();
            while (!Character.isLetter(this.c) && this.c != -1)
            {
                this.c = this.r.read();
            }
        }
        catch (IOException e)
        {
            this.c = -1;
        }
    }


    @Override
    public boolean hasNext()
    {
    	
    	return c != -1;
    	
    }

    
    @Override
    public String next()
    {

        if (!hasNext())
        {
            return "";
        }
        
        String answer = "";

        try
        {        	
        	// if queue empty, push first 'n' words into queue
			if (nSequenceQueue.isEmpty()) {
				while (nSequenceQueue.size() < n && this.hasNext()) {
	        		answer = "";
					while (Character.isLetter(this.c))
	                {
	                    answer = answer + (char)this.c;
	                    this.c = this.r.read();
	                }
	                skipNonLetters();
					nSequenceQueue.add(new String(answer));
				}
				
				if (nSequenceQueue.size() < n) {
					return "";
				}
				return queueToString(nSequenceQueue);
			}
			
			// use queue to update n-sequence 
			if (this.hasNext()) {
				nSequenceQueue.remove();
				while (Character.isLetter(this.c))
                {
                    answer = answer + (char)this.c;
                    this.c = this.r.read();
                }
                skipNonLetters();
				nSequenceQueue.add(answer);	
				return queueToString(nSequenceQueue);
			}
        }
        
        catch (IOException e)
        {
            throw new NoSuchElementException();
        }
        return answer;
    }

    public String queueToString(Queue<String> queue) {
		if (queue == null || queue.isEmpty()) {
			return null;
		}
		
		String[] arr = new String[queue.size()];
		queue.toArray(arr);
		StringBuilder str = new StringBuilder();
		str.insert(0,arr[0]);
		for (int i = 1; i < arr.length; i++) {
			str.append(arr[i]);
		}
		
		return str.toString().toLowerCase();
	}
}

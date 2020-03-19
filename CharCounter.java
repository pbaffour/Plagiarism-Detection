import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class CharCounter implements ICharCounter, IHuffConstants {
	private HashMap<Integer, Integer> map;

	// CONSTRUCTOR
	public CharCounter() {
		map = new HashMap<>();
	}

	@Override
	public int getCount(int ch) {

		try {
			if (map.containsKey((int) ch)) {
				return map.get(ch);
			}
		}

		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public int countAll(InputStream stream) throws IOException {

		int counterForAll = 0;

		try {

			BitInputStream bis = new BitInputStream(stream);

			int bits = bis.read(BITS_PER_WORD);

			while (bits != -1) {
				add(bits);
				counterForAll++;
				bits = bis.read(BITS_PER_WORD);
			}

			bis.close();

		}

		catch (IOException e) {
			e.printStackTrace();
		}

		return counterForAll;
	}

	@Override
	public void add(int i) {

		if (!map.containsKey((int) i)) {
			map.put(i, 0);
		}

		map.put(i, map.get(i) + 1);

	}

	@Override
	public void set(int i, int value) {
		map.put(i, value);
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Map<Integer, Integer> getTable() {
		return (Map) this.map;
	}

}

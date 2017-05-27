import java.util.ArrayList;

/**
 * A class use to make searching easier
 * 
 * @author Elton Gladstone
 * 
 */
public class BufferPool {
	private ArrayList<Records> list; // records within the pool are kept in a
										// arraylist

	/**
	 * construct a new BufferPool of size 20
	 */
	public BufferPool() {
		list = new ArrayList<Records>(20);
	}

	/**
	 * check if the pool contain a record with the desired offset
	 * 
	 * @param offset
	 *            the location on database file
	 * @return the record if found, null otherwise
	 */
	public Records checkRecord(int offset) {
		for (int i = 0; i < list.size(); i++) {
			Records record = list.get(i);
			if (record.getOffset() == offset) {
				list.add(list.remove(i));
				return record;
			}
		}
		return null;
	}

	/**
	 * add a record into the pool if it not already in there
	 * 
	 * @param record
	 */
	public void add(Records record) {
		// remove a record if it's already at size 20
		if (list.size() >= 20) {
			list.remove(0);
		}
		list.add(record);
	}

	/**
	 * print the pool in a nice looking format
	 */
	public String toString() {
		String out = "MRU\n";
		for (int i = list.size() - 1; i >= 0; i--) {
			Records record = list.get(i);
			out += record.getOffset() + ":  " + record.getStrRecord() + "\n";
		}
		out += "LRU \n";
		return out;
	}
}

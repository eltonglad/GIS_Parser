import java.util.Vector;

/**
 * A data structure for storing with generic types K, and V. using quadratic
 * formula of ((n^2 + n) / 2)
 * 
 * @author Elton Gladstone
 * 
 * @param <K>
 *            key generic type
 * @param <V>
 *            value generic type
 */
public class HashTable<K, V> {

	private HashIndex<K, V>[] list;
	private int capacity;
	private int size = 0;
	private int longestProbe = 0;
	private int[] capacityList;
	private int capacityIndex = 0;

	/**
	 * constructor for this class
	 * 
	 * @param capacity
	 *            the capacities this table has
	 */
	@SuppressWarnings("unchecked")
	public HashTable(int capacity) {
		capacityList = new int[] { 1019, 2027, 4079, 8123, 16267, 32503, 65011,
				130027, 260111, 520279, 1040387, 2080763, 4161539, 8323151,
				16646323 };
		this.capacity = capacity;
		list = new HashIndex[capacity];
	}

	/**
	 * reset the probe to beginning
	 */
	public void resetProbe() {
		longestProbe = 0;
	}

	/**
	 * insert a new element into the table
	 * 
	 * @param key
	 *            the key
	 * @param element
	 *            the element to be inserted
	 */
	public void insert(K key, V element) {

		// find location to place element
		int index = quadProbe(key);

		// if the space exists
		if (list[index] != null) {
			list[index].addValue(element);
		}
		// if the space does not exist, increase capacity size
		else {
			list[index] = new HashIndex<K, V>(key, element);
			if (size++ > capacity * .7) {
				rehash();
			}

		}

	}

	/**
	 * adds the elements to a specific key
	 * 
	 * @param key
	 *            the key
	 * @param elements
	 *            the elements within the table
	 */
	public void reInsert(K key, Vector<V> elements) {

		int index = quadProbe(key);
		if (list[index] == null) {
			size++;
			list[index] = new HashIndex<K, V>(key, null);
			list[index].setValues(elements);
		}
	}

	/**
	 * search for the space the key can be placed
	 * 
	 * @param key
	 *            the key
	 * @return the index of space where key can be stored
	 */
	public int quadProbe(K key) {

		int index = key.hashCode() % capacity;
		int offset = 1;

		int count = 0;

		// keep looking if the space exists and the key is not already in there
		while (list[index] != null && !list[index].getKey().equals(key)) {

			index += offset;
			offset += 2;
			index %= capacity;
			count++;

		}
		// helper code for getting the length of time spending on find the
		// correct space
		if (count > longestProbe) {
			longestProbe = count;
		}
		return index;
	}

	/**
	 * searching for the specific key
	 * 
	 * @param key
	 *            the key to look for
	 * @return the values the key is matched with
	 */
	public Object find(K key) {
		HashIndex<K, V> entry = list[quadProbe(key)];
		if (entry != null) {
			return entry.getValues();
		}
		return null;
	}

	/**
	 * reorder the table after an increase of capacity
	 */
	@SuppressWarnings("unchecked")
	private void rehash() {
		size = 0;
		capacityIndex++;
		capacity = capacityList[capacityIndex];

		@SuppressWarnings("rawtypes")
		HashIndex[] old = list;
		list = new HashIndex[capacity];
		for (int i = 0; i < old.length; i++) {
			if (old[i] != null) {
				reInsert((K) old[i].getKey(), old[i].getValues());
			}
		}
	}

	/**
	 * get a hash code from a string (course notes)
	 * 
	 * @param toHash
	 * @return
	 */
	public static int elfHash(String str) {
		long hashCode = 0;
		for (int Pos = 0; Pos < str.length(); Pos++) { // use all elements

			hashCode = (hashCode << 4) + str.charAt(Pos); // shift/mix

			long hiBits = hashCode & 0xF0000000L; // get high nybble

			if (hiBits != 0)
				hashCode ^= hiBits >> 24; // xor high nybble with second nybble

			hashCode &= ~hiBits; // clear high nybble
		}

		return (int) (hashCode);
	}

	/**
	 * @return size of the table
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @return the number of times it took to find space
	 */
	public int longestProbe() {
		return longestProbe;
	}

	/**
	 * @return the current capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * @return a formated string
	 */
	public String toString() {
		String s = "";
		int i = 0;
		System.out.println("HashTable---current size: " + size + "	capacity: "
				+ capacity);
		while (i < capacity) {
			// Skip the null cells
			if (list[i] != null) {
				s += i + ": " + list[i].toString() + "\n";
			}
			i++;
		}
		return s;
	}
}
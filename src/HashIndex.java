import java.util.Vector;

public class HashIndex<K, V> {

	private K key; // the key
	private Vector<V> values; // the data

	/**
	 * Constructor 
	 * @param key the key
	 * @param value the data being stored
	 */
	public HashIndex(K key, V value) {

		this.key = key;
		values = new Vector<V>();
		values.add(value);
	}

	/**
	 * @return the key
	 */
	public K getKey() {
		return key;
	}

	/**
	 * @return the list of datas
	 */
	public Vector<V> getValues() {
		return values;
	}

	/**
	 * @param value the data to be added
	 */
	public void addValue(V val) {
		values.add(val);
	}
	
	/**
	 * @param vals the list of data to be added
	 */
	public void setValues(Vector<V> vals){
		values = vals;
	}

	/**
	 * @return a formated string
	 */
	public String toString() {

		String s = "[";
		s += this.getKey() + "," + this.getValues() + "]";
		return s;
	}

}
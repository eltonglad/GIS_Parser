
/**
 * Container for holding a name and state for easy HashTable handling
 * @author Elton Gladstone
 *
 */
public class NameIndex {
	private final String fName; // the name
	private final String state; //the state

	/**
	 * Constructor
	 * @param fName the feature name
	 * @param state the state
	 */
	public NameIndex(String fName, String state) {
		this.fName = fName;
		this.state = state;
	}

	/**
	 * @return the feature name
	 */
	public String getName() {
		return fName;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 *@return a hashcode for this specific container
	 */
	public int hashCode() {
		return HashTable.elfHash(fName + ":" + state);
	}

	/**
	 *@return whether the 2 objects are equal
	 */
	public boolean equals(Object other) {
		if (other.getClass().equals(NameIndex.class)) {
			NameIndex n = (NameIndex) other;
			return (n.getName().equals(fName) && n.getState().equals(state));
		}
		return false;
	}

	/**
	 *@return a formated string
	 */
	public String toString() {
		return fName + ":" + state;
	}
}

/**
 * @author Elton data structure for holding the coordinates.
 */
public class Coords {

	private int degree;
	private int minute;
	private int second;
	private String direction;

	/**
	 * 
	 * @param d
	 *            the degree
	 * @param m
	 *            the minute
	 * @param s
	 *            the second
	 * @param dir
	 *            the direction
	 */
	public Coords(int d, int m, int s, String dir) {
		degree = d;
		minute = m;
		second = s;
		if (dir.equals("N")) {
			direction = "North";
		} else if (dir.equals("W")) {
			direction = "West";
		} else if (dir.equals("S")) {
			direction = "South";
		} else if (dir.equals("E")) {
			direction = "East";
		}
	}

	/**
	 * @return the degree
	 */
	public int getDegree() {
		return degree;
	}

	/**
	 * @return the minute
	 */
	public int getMinute() {
		return minute;
	}

	/**
	 * @return the second
	 */
	public int getSecond() {
		return second;
	}

	/**
	 * @return the direction
	 */
	public String getDirection() {
		return direction;
	}

	public int getTotalSeconds() {
		// positive seconds
		if (direction == "North" || direction == "East")
			return degree * 3600 + minute * 60 + second;

		// negative seconds
		return (degree * 3600 + minute * 60 + second) * -1;
	}

	/**
	 * @return everything this class contains in a correct format
	 */
	public String toString() {
		String out = "";
		if (degree < 10) {
			out += "0" + degree;
		} else
			out += degree;

		if (minute < 10) {
			out += "0" + minute;
		} else
			out += "" + minute;

		if (second < 10) {
			out += "0" + second;
		} else
			out += "" + second;
		out += direction;
		return out;
	}
}

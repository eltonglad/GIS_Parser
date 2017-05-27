/**
 * A container class for holding 2 Coords class objects
 * 
 * @author Elton Gladstone
 * 
 */
public class GeoCoords {
	private Coords latitude; // the latitude container
	private Coords longitude;// the longitude container

	/**
	 * construct a new class using 2 other classes
	 * 
	 * @param lat the latitude
	 * @param long the longitude
	 */
	public GeoCoords(Coords lat, Coords longi) {
		latitude = lat;
		longitude = longi;
	}

	/**
	 * @return the latitude holder
	 */
	public Coords getLat() {
		return latitude;
	}

	/**
	 * @return the longitude holder
	 */
	public Coords getLong() {
		return longitude;
	}

	/**
	 * print format for this class
	 */
	public String toString() {
		return latitude + " " + longitude;
	}

	/**
	 * check if 2 objects are the same
	 */
	public boolean equals(Object other) {
		if (other.getClass().equals(GeoCoords.class)) {
			GeoCoords o = (GeoCoords) other;
			return (o.getLat().equals(latitude) && o.getLong()
					.equals(longitude));
		}
		return false;
	}
}
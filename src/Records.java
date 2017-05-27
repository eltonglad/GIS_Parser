/**
 * @author Elton Gladstone
 * A data structure for holding the informations of a GIS record
 *         that going to be returned
 */
public class Records {
	private int FID; //the ID
	private String fName; //the name
	private String fClass; //the class
	private String fState; //the state
	private int stateNum; //the state code
	private String county; //the county
	private int countyNum; //the county code
	private Coords pLat; //the primary latitude
	private Coords pLong; //the primary longitude
	private String sLat; //the source latitude
	private String sLong; //the source longitude
	private int elevFt; //the elevation in ft
	private String map; //the map name
	private String created; //the date created
	private String edited; //the date edited
	private int offset; //the offset
	private String strRecord;//the whole string

	/**
	 * 
	 * @param FID the ID
	 * @param fName the name
	 * @param fClass the class
	 * @param fState the state
	 * @param stateNum the state code
	 * @param county the county
	 * @param countyNum the county code
	 * @param pLat the primary latitude
	 * @param pLong the primary longitude
	 * @param sLat the source latitude
	 * @param sLong the source longitude
	 * @param elevFt the elevation in ft
	 * @param map the map name
	 * @param created the date created
	 * @param edited the date edited
	 * @param offset the offset
	 * @param strRecord the whole string
	 */
	public Records(int FID, String fName, String fClass, String fState,
			int stateNum, String county, int countyNum, Coords pLat,
			Coords pLong, String sLat, String sLong, int elevFt, String map, String created,
			String edited, int offset, String strRecord) {
		this.FID = FID;
		this.fName = fName;
		this.fClass = fClass;
		this.fState = fState;
		this.stateNum = stateNum;
		this.county = county;
		this.countyNum = countyNum;
		this.pLat = pLat;
		this.pLong = pLong;
		this.sLat = sLat;
		this.sLong = sLong;
		this.elevFt = elevFt;
		this.map = map;
		this.created = created;
		this.edited = edited;
		this.offset = offset;
		this.strRecord = strRecord;

	}

	/**
	 * @return the ID
	 */
	public int getFID() {
		return FID;
	}

	/**
	 * @return the name
	 */
	public String getfName() {
		return fName;
	}
	
	/**
	 * @return the class
	 */
	public String getfClass() {
		return fClass;
	}

	/**
	 * @return the state
	 */
	public String getfState() {
		return fState;
	}

	/**
	 * @return the state code
	 */
	public int getStateNum() {
		return stateNum;
	}

	/**
	 * @return the county
	 */
	public String getCounty() {
		return county;
	}

	/**
	 * @return the county code
	 */
	public int getCountyNum() {
		return countyNum;
	}

	/**
	 * @return the primary latitude
	 */
	public Coords getpLat() {
		return pLat;
	}

	/**
	 * @return the primary longitude
	 */
	public Coords getpLong() {
		return pLong;
	}
	
	/**
	 * @return the source latitude
	 */
	public String getsLat() {
		return sLat;
	}

	/**
	 * @return the source longitude
	 */
	public String getsLong() {
		return sLong;
	}

	/**
	 * @return the elevation in ft
	 */
	public int getElevFt() {
		return elevFt;
	}

	/**
	 * @return the map
	 */
	public String getMap() {
		return map;
	}

	/**
	 * @return the date created
	 */
	public String getCreated() {
		return created;
	}

	/**
	 * @return the date edited
	 */
	public String getEdited() {
		return edited;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @return the whole string
	 */
	public String getStrRecord() {
		return strRecord;
	}

	/**
	 * @return everything this class contains in a correct format
	 */
	public String toString() {
		return strRecord;
	}

}

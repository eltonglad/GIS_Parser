import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * The class handles the formating of the log files
 * 
 * @author Elton Gladstone 
 */
public class Processor {
	private File dataFile; // the data file
	private File commandFile; // the command file
	private File logFile; // the log file
	DateFormat dateFormat; // format of the display date
	Date date; // get initial time
	String out = ""; // the printout string

	/**
	 * The constructor, sets up the processor and date/time
	 * 
	 * @param data
	 *            the data file
	 * @param command
	 *            the command file
	 * @param log
	 *            the log file
	 */
	public Processor(File data, File command, File log) {
		dataFile = data;
		commandFile = command;
		logFile = log;
		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		date = new Date();
	}

	/**
	 * used to print the title for the log file
	 * 
	 * @param x1
	 *            the minimal longitude
	 * @param x2
	 *            the maximal longitude
	 * @param y1
	 *            the minimal latitude
	 * @param y2
	 *            the maximum latitude
	 * @return the string for the title
	 */
	public String title(int x1, int x2, int y1, int y2) {
		out = "";
		out += "GIS Program\n";
		out += "dbFile:\t" + dataFile.getName() + "\n";
		out += "script:\t" + commandFile.getName() + "\n";
		out += "log   :\t" + logFile.getName() + "\n";
		out += "Start time: " + dateFormat.format(date) + "\n";
		out += "Quadtree children are printed in the order SW SE NE NW\n";
		out += "--------------------------------------------------------------------------------\n\n";
		out += "Latitude/longitude values in index entries are shown as signed integers, in total seconds.\n\n";
		out += "World boundaries are set to:\n";
		out += "\t\t\t" + y2 + "\n";
		out += "\t" + x1 + "\t\t\t" + x2 + "\n";
		out += "\t\t\t" + y1 + "\n";
		out += "--------------------------------------------------------------------------------\n\n";
		return out;
	}

	/**
	 * used to call the import function and format string to print into log File
	 * 
	 * @param data
	 *            The original GIS file
	 * @return the string to print to log File
	 * @throws IOException
	 * @throws Exception
	 */
	public String importGIS(File data) throws IOException, Exception {
		out = "";
		int[] result = Controller.getInstance().importRecord(data);
		out += "Imported Features by name: " + result[0] + "\n";
		out += "Longest probe sequence:    " + result[1] + "\n";
		out += "Imported Locations:        " + result[0] + "\n";
		out += "--------------------------------------------------------------------------------\n";
		return out;
	}

	/**
	 * used to call the what is at function and format string to print into log
	 * File
	 * 
	 * @param la
	 *            the latitude in string
	 * @param lo
	 *            the longitude int string
	 * @return the string to print to log
	 * @throws Exception
	 */
	public String whatIsAt(String la, String lo) throws Exception {
		// create new Coords class for latitude and longitude
		Coords lat = new Coords(Integer.parseInt(la.substring(0, 2)),
				Integer.parseInt(la.substring(2, 4)), Integer.parseInt(la
						.substring(4, 6)), la.substring(6));
		Coords lon = new Coords(Integer.parseInt(lo.substring(0, 3)),
				Integer.parseInt(lo.substring(3, 5)), Integer.parseInt(lo
						.substring(5, 7)), lo.substring(7));
		// create GeoCoords class using both Coords class
		GeoCoords geoC = new GeoCoords(lat, lon);
		out = "";

		Vector<Records> records = Controller.getInstance().whatIsAt(geoC);

		if (records.size() == 0) {
			out = "\tNothing was found at " + lo + "	" + la + "\n";
		} else {
			out = "\tThe following features were found at " + lo + "	" + la
					+ ":\n";

			for (int i = 0; i < records.size(); i++) {
				Records rec = records.get(i);

				out += rec.getOffset() + ":  " + rec.getfName() + "  "
						+ rec.getCounty() + "  " + rec.getfState() + "\n";
			}
		}
		out += "--------------------------------------------------------------------------------\n";
		return out;
	}

	/**
	 * used to call the what is function and format string to print into log
	 * File
	 * 
	 * @param fName
	 *            the feature name
	 * @param fState
	 *            the feature state
	 * @return the string to print to log
	 * @throws Exception
	 */
	public String whatIs(String fName, String fState) throws Exception {
		out = "";
		NameIndex nameIn = new NameIndex(fName, fState);
		Vector<Records> records = Controller.getInstance().whatIs(nameIn);
		if (records.size() == 0) {
			out = "No records match " + fName + " and " + fState + "\n";
		} else {
			for (int i = 0; i < records.size(); i++) {
				Records rec = records.get(i);
				out += rec.getOffset() + ":  " + rec.getCounty() + " "
						+ rec.getpLong() + " " + rec.getpLat() + "\n";
			}
		}
		out += "--------------------------------------------------------------------------------\n";
		return out;
	}

	/**
	 * used to call the what is in function and format string to print into log
	 * File
	 * 
	 * @param switchNum
	 *            the integer representation of the switch
	 * @param la
	 *            the latitude in string
	 * @param lo
	 *            the longitude in string
	 * @param y
	 *            the half height
	 * @param x
	 *            the half width
	 * @return the string to print to log
	 * @throws Exception
	 */
	public String whatIsIn(int switchNum, String la, String lo, int y, int x)
			throws Exception {

		// create new Coords class using the string
		Coords lat = new Coords(Integer.parseInt(la.substring(0, 2)),
				Integer.parseInt(la.substring(2, 4)), Integer.parseInt(la
						.substring(4, 6)), la.substring(6));
		Coords lon = new Coords(Integer.parseInt(lo.substring(0, 3)),
				Integer.parseInt(lo.substring(3, 5)), Integer.parseInt(lo
						.substring(5, 7)), lo.substring(7));
		// create new GeoCoords class using both the newly created latitude and
		// longitude coord
		GeoCoords geoC = new GeoCoords(lat, lon);
		out = "";
		Vector<Records> records = Controller.getInstance().whatIsIn(geoC, y, x);

		if (records.size() == 0) {
			out += "\tNothing was found in (" + lo + " +/- " + x + ", " + la
					+ " +/- " + y + ")\n";
		} else {
			// "-l" switch, print all non-empty information on matched records
			if (switchNum == 1) {
				out += "\tThe following " + records.size()
						+ " features were found in (" + lo + " +/- " + x + ", "
						+ la + " +/- " + y + ")\n";
				for (int i = 0; i < records.size(); i++) {
					Records rec = records.get(i);

					out += "  Feature ID   : " + rec.getFID() + "\n";
					out += "  Feature name : " + rec.getfName() + "\n";
					out += "  Feature Cat  : " + rec.getfClass() + "\n";
					out += "  State        : " + rec.getfState() + "\n";
					out += "  County       : " + rec.getCounty() + "\n";
					out += "  Latitude     : " + rec.getpLat() + "\n";
					out += "  Longitude    : " + rec.getpLong() + "\n";

					// The following fields might be empty, so only print if
					// they are not
					if (!rec.getsLat().equals("")) {
						out += "  Src Lat      : " + rec.getsLat() + "\n";
					}
					if (!rec.getsLong().equals("")) {
						out += "  Src Long     : " + rec.getsLong() + "\n";
					}
					if (rec.getElevFt() != -1) {
						out += "  Elev in ft   : " + rec.getElevFt() + "\n";
					}
					if (!rec.getMap().equals("")) {
						out += "  USGS Quad    : " + rec.getMap() + "\n";
					}
					if (!rec.getCreated().equals("")) {
						out += "  Date created : " + rec.getCreated() + "\n";
					}
					if (!rec.getEdited().equals("")) {
						out += "  Date mod     : " + rec.getEdited() + "\n";
					}
					out += "\n";
				}
			}
			// "-c" switch, print number of records that matches
			else if (switchNum == 2) {
				out += "\t" + records.size() + " features were found in (" + lo
						+ " +/- " + x + ", " + la + " +/- " + y + ")\n";
			}
			// no switch, print only the offset, name, state, latitude, and
			// longitude
			else if (switchNum == 3) {
				out += "\tThe following " + records.size()
						+ " features were found in (" + lo + " +/- " + x + ", "
						+ la + " +/- " + y + ")\n";
				for (int i = 0; i < records.size(); i++) {
					Records rec = records.get(i);
					out += rec.getOffset() + ":  " + rec.getfName() + " "
							+ rec.getfState() + "  " + rec.getpLat() + "  "
							+ rec.getpLong() + "\n";
				}
			}
		}
		out += "--------------------------------------------------------------------------------\n";
		return out;
	}

	/**
	 * used to call the print options of PRQuadTree, HashTable or BufferPool and
	 * format string to print into log File
	 * 
	 * @param com
	 *            the parameter of the command
	 * @return the string to print to log
	 */
	public String debug(String com) {
		out = "\n";
		// if the parameter was quad, print PRQuadTree
		if (com.equals("quad")) {
			out += Controller.getInstance().tree.printTree();
		}
		// if the parameter was hash, print the HashTable
		else if (com.equals("hash")) {
			out += "Format of display is\n";
			out += "Slot number: data record\n";
			out += "Current table size is "
					+ Controller.getInstance().table.getCapacity() + "\n";
			out += "Number of elements in table is "
					+ Controller.getInstance().table.getSize() + "\n\n";
			out += Controller.getInstance().table.toString();
		}
		// if the parameter was pool, print the BufferPool
		else if (com.equals("pool")) {
			out = Controller.getInstance().pool.toString();
		}
		out += "--------------------------------------------------------------------------------\n\n";
		return out;
	}

	/**
	 * format final string and give an end time
	 * 
	 * @return the string to print to log
	 */
	public String exit() {
		out = "";
		out += "Terminating execution of commands." + "\n";
		date = new Date();
		out += "End time: " + dateFormat.format(date) + "\n";
		return out;
	}

}

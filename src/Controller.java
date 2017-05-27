import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Vector;

/**
 * This the the controller class for the programs, it handles all interactions
 * between classes.
 * 
 * @author Elton Gladstone
 * 
 */
public class Controller {
	private static final Controller instance = new Controller(); // instance of
																	// this
																	// class
	private File dataFile; // the database File name
	private File commandFile; // the command File name
	private File logFile; // the log File name
	private String logString = ""; // the string that's going to be stored into
									// log
	private CommandParser cPar; // parser for reading in the command file
	private GISParser gPar; // parser for reading in the GIS records
	private Processor process; // the processor that handles the formating of
								// outputs
	public HashTable<NameIndex, Integer> table; // the HashTable
	public BufferPool pool; // the buffer pool
	public prQuadtree<Point> tree; // the quad-tree
	private long currentOffset = 0; // current offset location within data base
									// file
	private int x1, x2, y1, y2; // size of the world boundary
	private boolean firstImport = true;
	BufferedWriter logWriter;

	/**
	 * Constructor for this class, empty
	 */
	private Controller() {
	}

	/**
	 * This starts the entire program, called by Main
	 * 
	 * @param data
	 *            the database file
	 * @param command
	 *            the command file
	 * @param log
	 *            the log file
	 * @throws Exception
	 *             FILE-NOT-FOUND-EXCEPTION
	 */
	public void start(File data, File command, File log) throws Exception {

		// set up files
		commandFile = command;
		dataFile = data;
		logFile = log;

		// create a new database File
		try {
			PrintWriter writer = new PrintWriter(dataFile);
			writer.print("");
			writer.close();

			dataFile.createNewFile();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// initialize
		table = new HashTable<NameIndex, Integer>(1019);
		pool = new BufferPool();
		cPar = new CommandParser(commandFile);
		process = new Processor(data, command, log);

		// set up log File
		try {
			logFile.createNewFile();
			FileWriter fw = new FileWriter(logFile.getAbsoluteFile());
			logWriter = new BufferedWriter(fw);

			// does all the calculations
			run();

			// close file
			logWriter.close();

		} catch (Exception e) {
			// Catch exception
			System.err.println("Error: " + e.getMessage());
		}

	}

	/**
	 * print the string into the log file
	 * 
	 * @throws IOException
	 */
	public void printToLog() throws IOException {
		logWriter.write(logString);
	}

	/**
	 * @return the instance of this class
	 */
	public static Controller getInstance() {
		return instance;
	}

	/**
	 * The function that handles all interaction
	 * 
	 * @throws Exception
	 *             FILE-NOT-FOUND-EXCEPTION
	 */
	public void run() throws Exception {
		int commandCount = 0; // number of command currently on
		// read in the commands
		for (int i = 0; i < cPar.getLine(); i++) {
			String command = cPar.readCommand();
			// splits up the strings
			String[] comm = command.split("\\s+");

			// if the command was an comment
			if (command.startsWith(";")) {
				logString = command + "\n";
				printToLog();
			}

			// if the command was to create a new world
			else if (comm[0].equals("world")) {
				System.out.println("Building world with: " + comm[1] + " "
						+ comm[2] + " " + comm[3] + " " + comm[4]);
				logString = command + "\n" + "\n";
				// convert string to Coord then to total seconds
				x1 = getCoords(comm[1]).getTotalSeconds();
				x2 = getCoords(comm[2]).getTotalSeconds();
				y1 = getCoords(comm[3]).getTotalSeconds();
				y2 = getCoords(comm[4]).getTotalSeconds();
				// create new tree with boundary set by the total seconds
				tree = new prQuadtree<Point>(x1, x2, y1, y2);
				// add string into printout string
				logString += process.title(x1, x2, y1, y2);
				printToLog();
			}
			// if the command was to import new GIS file
			else if (comm[0].equals("import")) {
				System.out
						.println("Importing records from GIS to database file...");
				System.out.println("Building HashTable and PRQuadTree.....");
				commandCount++;
				logString = "Command " + commandCount + ":	" + command + "\n"
						+ "\n";
				// copy over records from GIS into dataFile
				File gis = new File(comm[1]);
				copyOver(gis);
				// import records from dataFile into PRQuadTree, HashTable, and
				// BufferPool.
				logString += process.importGIS(dataFile);
				printToLog();

			}
			// if the command was what_is_at a location
			else if (comm[0].equals("what_is_at")) {
				System.out.println("Getting: " + command);
				commandCount++;
				logString = "Command " + commandCount + ":	" + command + "\n"
						+ "\n";
				// call the processor
				logString += process.whatIsAt(comm[1], comm[2]);
				printToLog();
			}

			// if the command was what_is
			else if (comm[0].equals("what_is")) {
				System.out.println("Getting: " + command);
				commandCount++;
				logString = "Command " + commandCount + ":	" + command + "\n"
						+ "\n";
				// If the fName has multiple words, then add the strings from
				// 2nd word till the 2nd from last word
				int length = comm.length;
				String newName = comm[1];
				for (int n = 2; n < length - 1; n++) {
					newName += " " + comm[n];
				}
				// call the processor
				logString += process.whatIs(newName, comm[length - 1]);
				printToLog();
			}
			// if the command was what_is_in
			else if (comm[0].equals("what_is_in")) {
				System.out.println("Getting: " + command);
				commandCount++;
				logString = "Command " + commandCount + ":	" + command + "\n"
						+ "\n";
				// check what switch it is
				if (comm[1].equals("-l")) {
					// make the switch 1
					logString += process.whatIsIn(1, comm[2], comm[3],
							Integer.parseInt(comm[4]),
							Integer.parseInt(comm[5]));
				} else if (comm[1].equals("-c")) {
					// make the switch 2
					logString += process.whatIsIn(2, comm[2], comm[3],
							Integer.parseInt(comm[4]),
							Integer.parseInt(comm[5]));
				} else {
					// make the switch 3
					logString += process.whatIsIn(3, comm[1], comm[2],
							Integer.parseInt(comm[3]),
							Integer.parseInt(comm[4]));
				}
				printToLog();

			}
			// if the command was to debug, print
			else if (comm[0].equals("debug")) {
				System.out.println("Getting: " + command);
				commandCount++;
				logString = "Command " + commandCount + ":	" + command + "\n"
						+ "\n";
				// pass the parameter to the processor
				logString += process.debug(comm[1]);
				printToLog();
			}
			// if the command was quit
			else if (comm[0].equals("quit")) {
				System.out.println("Getting: " + command);
				commandCount++;
				logString = "Command " + commandCount + ":	" + command + "\n"
						+ "\n";
				// make final touches to printout string and exit
				logString += process.exit();
				System.out.println("Program complete.");
				printToLog();
				break;
			}
		}

	}

	/**
	 * converts a string into a Coords class
	 * 
	 * @param str
	 *            the string that represent a DMS
	 * @return a Coords class representation of the DMS
	 */
	public Coords getCoords(String str) {
		Coords coord;
		// if the string only has 7 digits, such as the latitude
		if (str.length() == 7) {
			coord = new Coords(Integer.parseInt(str.substring(0, 2)),
					Integer.parseInt(str.substring(2, 4)), Integer.parseInt(str
							.substring(4, 6)), str.substring(6));
		}
		// if the string has 8 digits, such as the longitude
		else {
			coord = new Coords(Integer.parseInt(str.substring(0, 3)),
					Integer.parseInt(str.substring(3, 5)), Integer.parseInt(str
							.substring(5, 7)), str.substring(7));
		}
		return coord;
	}

	/**
	 * copy all records in GIS file and then paste into the database File
	 * 
	 * @param GISFile
	 *            the import file
	 * @throws IOException
	 *             INPUT-OUTPUT-EXCEPTION
	 */
	public void copyOver(File GISFile) throws IOException {
		String str = "";
		gPar = new GISParser(GISFile);

		// copy and paste all the records into the database File
		try {

			RandomAccessFile writer = new RandomAccessFile(dataFile, "rw");
			// store a current offset for future use
			currentOffset = writer.length();
			writer.seek(currentOffset);

			// If this is the first time copy records from GIS to database, add
			// title
			if (firstImport == true) {
				str = "FEATURE_ID|FEATURE_NAME|FEATURE_CLASS|STATE_ALPHA|STATE_NUMERIC|COUNTY_NAME|COUNTY_NUMERIC|PRIMARY_LAT_DMS|PRIM_LONG_DMS|PRIM_LAT_DEC|PRIM_LONG_DEC|SOURCE_LAT_DMS|SOURCE_LONG_DMS|SOURCE_LAT_DEC|SOURCE_LONG_DEC|ELEV_IN_M|ELEV_IN_FT|MAP_NAME|DATE_CREATED|DATE_EDITED\n";
				writer.write(str.getBytes());
			}

			int count = 0;
			int skip = gPar.getLineNum() / 20;
			// skipping the 1st line from GIS file
			gPar.readLine(-1);
			while (!gPar.end()) {
				// copy and paste one line to database File
				writer.write((gPar.readLine(-1) + "\n").getBytes());

				// prints at 5% intervals
				count++;
				if (count % skip == 0) {
					System.out.println(count + " records copied");
				}
			}

			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// 1st time import is done
		firstImport = false;
		System.out.println("File copied");
	}

	/**
	 * Add the records into the PRQuadTree, and HashTable
	 * 
	 * @param dataFile
	 *            the database File
	 * @return information about the adding
	 * @throws IOException
	 *             INPUT-OUTPUT-EXCEPTION
	 * @throws Exception
	 *             FILE-NOT-FOUND-EXCEPTION
	 */
	public int[] importRecord(File dataFile) throws IOException, Exception {
		int[] out = new int[] { 0, 0 };
		gPar = new GISParser(dataFile);
		gPar.rePosition(currentOffset);
		table.resetProbe();
		int count = 0;
		int skip = gPar.getLineNum() / 20;

		// keep reading from the dataFile till the end is reached
		while (!gPar.end()) {
			Records rec = gPar.getRecord(-1);
			if (rec != null) {

				// create NameIndex and Point for storing
				NameIndex index = new NameIndex(rec.getfName(), rec.getfState());
				Point p = new Point(rec.getpLong().getTotalSeconds(), rec
						.getpLat().getTotalSeconds(), rec.getOffset());

				// make sure the point is within bounds
				if (p.inBox(x1, x2, y1, y2)) {
					// Build HashTable
					table.insert(index, rec.getOffset());
					// Build PRQuadTree
					tree.insert(p);
					out[0]++;

					// prints at 5% intervals
					count++;
					if (count % skip == 0) {
						System.out.println(count + " records inserted");
					}
				}

			} else {
				System.out.println("Insert Failed.");
			}
		}
		out[1] = table.longestProbe();
		return out;
	}

	/**
	 * Find record based on a latitude and a longitude
	 * 
	 * @param coord
	 *            the geoCoord class for holding a latitude and longitude
	 * @return records that matches the conditions
	 * @throws Exception
	 */
	public Vector<Records> whatIsAt(GeoCoords coord) throws Exception {
		Vector<Records> records = new Vector<Records>(0);

		// Find the coord in the tree
		Point p = tree.find(new Point(coord.getLong().getTotalSeconds(), coord
				.getLat().getTotalSeconds(), -1));

		// check if the point exists
		if (p != null) {
			Vector<Integer> offsets = p.getOffsets();
			gPar = new GISParser(dataFile);

			records = poolOffset(offsets);
		}
		return records;

	}

	/**
	 * 
	 * @param nameIn
	 *            the NameIndex class thats holds the record's name and state
	 * @return records that matches the conditions
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Vector<Records> whatIs(NameIndex nameIn) throws Exception {
		Vector<Records> records = new Vector<Records>(0);

		// find the record within the HashTable
		Vector<Integer> offsets = (Vector<Integer>) table.find(nameIn);

		gPar = new GISParser(dataFile);
		// check if the offset exists
		if (offsets != null) {
			records = poolOffset(offsets);
		}
		return records;

	}

	/**
	 * given a location and 2 ranges. find all records thats inside the bound
	 * 
	 * @param coord
	 *            the middle location
	 * @param height
	 *            the range from bottom to top
	 * @param width
	 *            the range from left to right
	 * @return records that matches the conditions
	 * @throws Exception
	 */
	public Vector<Records> whatIsIn(GeoCoords coord, int height, int width)
			throws Exception {
		Vector<Records> records = new Vector<Records>(0);
		Vector<Integer> offsets = new Vector<Integer>(0);
		// create new holder class for storing
		Point p = new Point(coord.getLong().getTotalSeconds(), coord.getLat()
				.getTotalSeconds(), -1);
		// call the find function from PRQuadTree, finding all records within
		// the boundary
		Vector<Point> points = tree.find(p.getX() - width, p.getX() + width,
				p.getY() - height, p.getY() + height);

		if (points != null) {
			// get all the offset of those records
			for (int i = 0; i < points.size(); i++) {
				offsets.addAll(points.get(i).getOffsets());
			}

			gPar = new GISParser(dataFile);
			records = poolOffset(offsets);
		}
		return records;

	}

	/**
	 * Check if the record is inside the BufferPool, if not add it to the pool
	 * 
	 * @param offsets
	 *            the list of offsets to find
	 * @return the record the offset points to
	 * @throws Exception
	 */
	private Vector<Records> poolOffset(Vector<Integer> offsets)
			throws Exception {
		Vector<Records> records = new Vector<Records>(0);

		for (int i = 0; i < offsets.size(); i++) {
			int currentOffset = offsets.get(i);

			// Check buffer pool for record
			Records poolRec = Controller.getInstance().pool
					.checkRecord(currentOffset);
			if (poolRec != null) {
				// found record within pool
				records.add(poolRec);
			} else {
				// add record to pool is its not already there
				Records dataRec = gPar.getRecord(currentOffset);
				records.add(dataRec);
				Controller.getInstance().pool.add(dataRec);
			}
		}
		return records;
	}

}

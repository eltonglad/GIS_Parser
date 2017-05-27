import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

/**
 * @author Elton Gladstone This class parse the GIS input  
 */
public class GISParser {

	RandomAccessFile raf;
	Scanner scan;
	private Records record;
	private int length = 0;
	private int line = 0;

	/**
	 * @param f
	 *            the input GIS record file
	 */
	public GISParser(File f) {

		try {
			raf = new RandomAccessFile(f, "r");
			scan = new Scanner(f);
			length = (int) raf.length();

			// get total number of lines/record in the GIS record file
			while (scan.hasNextLine()) {
				line++;
				scan.nextLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param offSet
	 *            the location of record wanted
	 * @return the record at the specific offset
	 * @throws Exception
	 *             FileNotFound exception
	 */
	public Records getRecord(int offSet) throws Exception {
		record = null;

		if (offSet == -1) {
			offSet = (int) raf.getFilePointer();
		}

		raf.seek(offSet);

		String com = raf.readLine();
		String[] str = com.split("\\|");

		// construct new Coords class and use for construction of new record
		// class
		Coords lat = new Coords(convertInt(str[7].substring(0, 2)),
				convertInt(str[7].substring(2, 4)),
				convertInt(str[7].substring(4, 6)), str[7].substring(6));

		Coords lon = new Coords(convertInt(str[8].substring(0, 3)),
				convertInt(str[8].substring(3, 5)),
				convertInt(str[8].substring(5, 7)), str[8].substring(7));

		// if the data_edited is empty
		if (str.length == 19) {
			record = new Records(convertInt(str[0]), str[1], str[2], str[3],
					convertInt(str[4]), str[5], convertInt(str[6]), lat, lon,
					str[11], str[12], convertInt(str[16]), str[17], str[18],
					"", offSet, com);

		}
		// if the record has date_edited as last entry
		else {
			record = new Records(convertInt(str[0]), str[1], str[2], str[3],
					convertInt(str[4]), str[5], convertInt(str[6]), lat, lon,
					str[11], str[12], convertInt(str[16]), str[17], str[18],
					str[19], offSet, com);
		}

		return record;
	}

	/**
	 * convert a string to integer
	 * 
	 * @param str
	 *            the integer string
	 * @return theinteger
	 */
	public int convertInt(String str) {
		if (str.equals("")) {
			return -1;
		}
		return Integer.parseInt(str);
	}

	/**
	 * @return the total number of offsets in the file
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @return the total number of line/records in file
	 */
	public int getLineNum() {
		return line;
	}

	/**
	 * @param num
	 *            the lineNumber
	 * @return the offset location of a specific line number
	 * @throws IOException
	 *             FileNotFound exception
	 */
	public int getOffset(int num) throws IOException {
		raf.seek(0);
		for (int i = 0; i < num; i++) {
			raf.readLine();
		}
		return (int) raf.getFilePointer();
	}

	/**
	 * @param offset
	 *            the location of the record info wanted
	 * @return the feature ID at that specific offset
	 * @throws IOException
	 *             FileNotFound exception
	 */
	public int getID(int offset) throws IOException {
		raf.seek(offset);
		String[] str = raf.readLine().split("\\|");
		return convertInt(str[0]);

	}

	/**
	 * @param num
	 *            the line number
	 * @return the string at that line
	 * @throws IOException
	 *             FileNotFound exception
	 */
	public String readLine(int num) throws IOException {
		String str = "";
		if (num == -1) {
			str = raf.readLine();
		} else {
			raf.seek(0);
			for (int i = 0; i < num; i++) {
				str = raf.readLine();
			}
		}
		return str;

	}

	/**
	 * @param offset
	 *            the location in the file
	 * @return whether if the offset is at beginning of record or not
	 * @throws IOException
	 *             FileNotFound exception
	 */
	public boolean atStart(int offset) throws IOException {
		raf.seek(offset - 1);
		raf.readLine();
		int LineStart = (int) raf.getFilePointer();
		if (offset == LineStart) {
			return true;
		}
		return false;
	}

	/**
	 * reposition the offset in the right location so new imports into database
	 * would not overwrites current ones
	 * 
	 * @param l
	 *            the offset position
	 */
	public void rePosition(long l) {
		try {
			// reposition the offset so new imports can be read
			if (l - 1 >= 0) {
				raf.seek(l - 1);
			}
			// if the position is at beginning of file, skip the title line
			else {
				raf.seek(l);
			}
			raf.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * check if the end of file have been reached
	 * 
	 * @return whether if the end of file have been reached
	 * @throws IOException
	 */
	public boolean end() throws IOException {
		if (raf.getFilePointer() >= raf.length()) {
			return true;
		}
		return false;
	}

}

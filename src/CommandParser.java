import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

/**
 * Parser for reading in the command file
 * 
 * @author Elton Gladstone
 * 
 */
public class CommandParser {

	RandomAccessFile raf;
	Scanner scan;
	private int line = 0;

	/**
	 * constructor for the class
	 * 
	 * @param f
	 *            the input GIS record file
	 */
	public CommandParser(File f) {

		try {
			raf = new RandomAccessFile(f, "r");
			scan = new Scanner(f);

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
	 * @return the total line of commands in the file
	 */
	public int getLine() {
		return line;
	}

	/**
	 * reads and store the command from file
	 * 
	 * @return the string representing the command
	 * @throws IOException
	 */
	public String readCommand() throws IOException {
		String str = raf.readLine();
		return str;
	}

}

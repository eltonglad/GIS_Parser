import java.io.File;

/**
 * The program imports GIS files and use the data gathered to create a
 * quad-tree, hashtable, and a bufferPool.
 * 
 * @author Elton Gladstone 
 */
public class GIS {

	/**
	 * @param args
	 *            the input files
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		File dataFile = new File(args[0]);
		File commandFile = new File(args[1]);
		File logFile = new File(args[2]);
		Controller.getInstance().start(dataFile, commandFile, logFile);

	}

}
// On my honor:
//
// - I have not discussed the Java language code in my program with
// anyone other than my instructor or the teaching assistants
// assigned to this course.
//
// - I have not used Java language code obtained from another student,
// or any other unauthorized source, either modified or unmodified.
//
// - If any Java language code or documentation used in my program
// was obtained from another source, such as a text book or course
// notes, that has been clearly noted with a proper citation in
// the comments of my program.
//
// - I have not designed this program in such a way as to defeat or
// interfere with the normal operation of the Automated Grader.
//
// Pledge: On my honor, I have neither given nor received unauthorized
// aid on this assignment.
//
// <Elton Gladstone>
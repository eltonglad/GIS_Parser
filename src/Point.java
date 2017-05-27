import java.util.Vector;

/**
 * A container class for holding 2 values and an offset
 * @author Elton Gladstone
 *
 */
public class Point implements Compare2D<Point> {

	private float xcoord;
	private float ycoord;
	private Vector<Integer> offsets;
	
	/**
	 * empty constructor
	 */
	public Point() {
		xcoord = 0;
		ycoord = 0;
	}

	/**
	 * constructor a new Point class
	 * @param x the longitude
	 * @param y the latitude
	 * @param o the offset
	 */
	public Point(float x, float y, int o) {
		xcoord = x;
		ycoord = y;
		offsets = new Vector<Integer>(0);
		if (o != -1) {
			offsets.add(o);
		}
	}

	/**
	 * @return the longitude coordinate
	 */
	public float getX() {
		return xcoord;
	}

	/**
	 * @return the latitude coordinate
	 */
	public float getY() {
		return ycoord;
	}

	/**
	 * insert a list of offsets into the container
	 */
	public void addOffsets(Vector<Integer> offs) {
		offsets.addAll(offs);
	}

	/**
	 * @return the list of offsets
	 */
	public Vector<Integer> getOffsets() {
		return offsets;
	}
	
	/**
	 * get direction from current position to desired position
	 */
	public Direction directionFrom(float X, float Y) { 
		if ( xcoord <= X && ycoord > Y){
			return Direction.NW;
		}
		else if(xcoord > X && ycoord >= Y){
			return Direction.NE;
		}
		else if(xcoord < X && ycoord <= Y){
			return Direction.SW;
		}
		else if(xcoord >= X && ycoord < Y){
			return Direction.SE;
		}
        return Direction.NOQUADRANT;
   }
	
	/**
	 * determine which quadrant is the current position in
	 */
	public Direction inQuadrant(float xLo, float xHi, 
                               float yLo, float yHi) { 
		//if xcoord or ycoord is outside of the rectangle
		if ( !inBox(xLo, xHi, yLo, yHi)){
			return Direction.NOQUADRANT;
		}
		float xCenter = ((xLo + xHi)/2);
		float yCenter = ((yLo + yHi)/2);
		
		Direction dir = directionFrom(xCenter, yCenter);
		
		if(dir == Direction.NOQUADRANT){
			return Direction.NE;
		}
		return dir;
   }
	/**
	 * @return whether if current position is within a quadrant
	 */
	public boolean inBox(float xLo, float xHi, 
                          float yLo, float yHi) { 
		if ( xcoord < xLo || xcoord > xHi || ycoord < yLo || ycoord > yHi){
			return false;
		}
		return true;
   }

	/**
	 * @return a formated string
	 */
	public String toString() {
      return "[(" + xcoord + "," +  ycoord + ")," + offsets.toString() + "] ";
   }
	/**
	 * @return whether is the 2 objects are equal
	 */
	public boolean equals(Object o) {
		// check if the Object o is an Point class
		if ( !o.getClass().equals(this.getClass())){
    		return false;
    	}
		// it is equal if the directionFrom returns an NOQUADRANT
		if (this.getClass().cast(o).directionFrom(xcoord, ycoord) == Direction.NOQUADRANT){
			return true;
		}
		return false;
   }
}

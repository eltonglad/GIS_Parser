import java.util.Vector;

// The test harness will belong to the following package; the quadtree
// implementation must belong to it as well.  In addition, the quadtree
// implementation must specify package access for the node types and tree
// members so that the test harness may have access to it.
//

public class prQuadtree<T extends Compare2D<? super T>> {

	// You must use a hierarchy of node types with an abstract base
	// class. You may use different names for the node types if
	// you like (change displayHelper() accordingly).
	/**
	 * The parent class of the leafNode and internalNode
	 * 
	 * @author Elton Gladstone
	 * 
	 */
	abstract class prQuadNode {
		public abstract boolean isLeaf();
	}

	/**
	 * the leaf node for holding a data field
	 * 
	 * @author Elton Gladstone
	 * 
	 */
	class prQuadLeaf extends prQuadNode {
		Vector<T> Elements = new Vector<T>();

		/**
		 * constructor for the leaf node
		 * 
		 * @param ele
		 *            the element the node holds
		 */
		public prQuadLeaf(T ele) {
			Elements.add(ele);
		}

		/**
		 * @return the ith element in the vector
		 */
		public T getElement(int i) {
			return Elements.get(i);
		}

		/**
		 * @return whether if the node is a leaf node
		 */
		public boolean isLeaf() {
			return true;
		}

		/**
		 * @param ele
		 *            the element to be added into the vector
		 */
		public void addElement(T ele) {
			Elements.add(ele);
		}

		/**
		 * @param ele
		 *            the element to be removed from the vector
		 * @return true if element has been removed false otherwise
		 */
		public boolean removeElement(T ele) {
			return Elements.remove(ele);
		}

		/**
		 * @return the number of elements the vector holds
		 */
		public int numElements() {
			return Elements.size();
		}
	}

	/**
	 * the internal node which holds 4 pointer to another node, of the parent
	 * class
	 * 
	 * @author Elton Gladstone
	 * 
	 */
	class prQuadInternal extends prQuadNode {
		prQuadNode NW, NE, SE, SW;

		/**
		 * @return whether if the node is a leaf node
		 */
		public boolean isLeaf() {
			return false;
		}

		/**
		 * @return number of non-null pointer it has
		 */
		public int numLeaf() {
			int i = 0;
			if (NW != null && NW.getClass().equals(prQuadLeaf.class)) {
				i++;
			}
			if (NE != null && NE.getClass().equals(prQuadLeaf.class)) {
				i++;
			}
			if (SE != null && SE.getClass().equals(prQuadLeaf.class)) {
				i++;
			}
			if (SW != null && SW.getClass().equals(prQuadLeaf.class)) {
				i++;
			}
			return i;
		}

		/**
		 * @return number of non-null pointer it has
		 */
		public int numInternal() {
			int i = 0;
			if (NW != null && NW.getClass().equals(prQuadInternal.class)) {
				i++;
			}
			if (NE != null && NE.getClass().equals(prQuadInternal.class)) {
				i++;
			}
			if (SE != null && SE.getClass().equals(prQuadInternal.class)) {
				i++;
			}
			if (SW != null && SW.getClass().equals(prQuadInternal.class)) {
				i++;
			}
			return i;
		}
	}

	/**
	 * a helper class for holding dimensions of the region
	 * 
	 * @author Elton Gladstone
	 * 
	 */
	class rectangle {
		float x1, x2, y1, y2;

		/**
		 * constructor for the helper class
		 * 
		 * @param xLo
		 *            the minimum x
		 * @param xHi
		 *            the maximum x
		 * @param yLo
		 *            the minimum y
		 * @param yHi
		 *            the maximum y
		 */
		private rectangle(float xLo, float xHi, float yLo, float yHi) {
			x1 = xLo;
			x2 = xHi;
			y1 = yLo;
			y2 = yHi;
		}

		// ///////// split the region into 4 quadrants and return the one
		// needed///////////
		/**
		 * @return the north-east quadrant
		 */
		private rectangle getNE() {
			return new rectangle(((x1 + x2) / 2), x2, ((y1 + y2) / 2), y2);
		}

		/**
		 * @return the north-west quadrant
		 */
		private rectangle getNW() {
			return new rectangle(x1, ((x1 + x2) / 2), ((y2 + y1) / 2), y2);
		}

		/**
		 * @return the south-west quadrant
		 */
		private rectangle getSW() {
			return new rectangle(x1, ((x1 + x2) / 2), y1, ((y1 + y2) / 2));
		}

		/**
		 * @return the south-east quadrant
		 */
		private rectangle getSE() {
			return new rectangle(((x1 + x2) / 2), x2, y1, ((y1 + y2) / 2));
		}

		/**
		 * check if this region and the given region share any points
		 * 
		 * @param rec
		 *            the region being compared to
		 * @return whether if they share any points
		 */
		private boolean sharePoint(rectangle rec) {

			if (x1 > rec.x2 || x2 < rec.x1 || y1 > rec.y2 || y2 < rec.y1) {
				return false;
			}
			return true;
		}
	}

	prQuadNode root; // the root of the quad-tree, the top node
	long xMin, xMax, yMin, yMax; // the dimension of the tree
	rectangle world; // the helper class for holding the dimension of the tree
	boolean success; // the helper boolean used in remove
	int bucketSize = 4; // the size of the bucket
	String Out = ""; // used for outprinting the tree
	int count = 0; // used to keep count of how many leaf been printed

	/**
	 * initialize quadtree to empty state, representing the specified region
	 * 
	 * @param xMin
	 *            the minimum x
	 * @param xMax
	 *            the maximum x
	 * @param yMin
	 *            the minimum y
	 * @param yMax
	 *            the maximum y
	 */
	public prQuadtree(long xMin, long xMax, long yMin, long yMax) {
		root = null;
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		world = new rectangle(xMin, xMax, yMin, yMax);

	}

	/**
	 * insert a element into the quad-tree Pre: elem != null Post: If elem lies
	 * within the tree's region, and elem is not already present in the tree,
	 * elem has been inserted into the tree.
	 * 
	 * @param elem
	 *            the element to be inserted
	 * @return true iff elem is inserted into the tree.
	 */
	public boolean insert(T elem) {
		// checks if it is inside region or if it is already inside the tree
		if (!elem.inBox(xMin, xMax, yMin, yMax)) {
			return false;
		}

		// call the recursive method to insert
		root = recInsert(root, elem, world);

		return true;
	}

	/**
	 * recursive method to insert a element into the quad-tree
	 * 
	 * @param node
	 *            the node that roots the subtree
	 * @param elem
	 *            the element to be inserted
	 * @param rec
	 *            the region
	 * @return the new node
	 */
	@SuppressWarnings("unchecked")
	private prQuadNode recInsert(prQuadNode node, T elem, rectangle rec) {

		// if node if empty, make it a new leaf node with element elem
		if (node == null) {
			return new prQuadLeaf(elem);
		}

		// check if the node is a leaf
		if (node.isLeaf()) {
			prQuadLeaf leaf = (prQuadLeaf) node;

			// see if the leaf is full
			if (leaf.numElements() < bucketSize) {

				// check for repeats
				boolean same = false;
				for (int i = 0; i < leaf.numElements(); i++) {
					// if the location exists
					if (leaf.getElement(i).equals(elem)) {
						same = true;
						leaf.getElement(i).addOffsets(elem.getOffsets());
					}
				}
				// add the new element into leaf if its not full
				if (!same) {
					leaf.addElement(elem);
				}
				return leaf;
			}
			// leaf is full

			// create a new internal node
			prQuadNode internal = new prQuadInternal();

			for (int i = 0; i < leaf.numElements(); i++) {

				// add the original leaf node into the internal node
				internal = recInsert(internal, leaf.getElement(i), rec);

			}
			// add the new leaf node with element elem into the internal node
			internal = recInsert(internal, elem, rec);

			return internal;
		}

		// if the node is an internal node, find which quadrant to insert new
		// node
		prQuadInternal internal = (prQuadInternal) node;
		Direction quadrant = elem.inQuadrant(rec.x1, rec.x2, rec.y1, rec.y2);

		// insert into NE
		if (quadrant == Direction.NE) {
			internal.NE = recInsert(internal.NE, elem, rec.getNE());
			return internal;
		}

		// insert into NW
		else if (quadrant == Direction.NW) {
			internal.NW = recInsert(internal.NW, elem, rec.getNW());
			return internal;
		}

		// insert into SW
		else if (quadrant == Direction.SW) {
			internal.SW = recInsert(internal.SW, elem, rec.getSW());
			return internal;
		}

		// insert into SE
		internal.SE = recInsert(internal.SE, elem, rec.getSE());
		return internal;

	}

	/**
	 * delete a element from the quad-tree Pre: elem != null Post: If elem lies
	 * in the tree's region, and a matching element occurs in the tree, then
	 * that element has been removed.
	 * 
	 * @param Elem
	 *            the element to be removed
	 * @return true iff a matching element has been removed from the tree.
	 */
	public boolean delete(T Elem) {
		// checks if it is inside region or if it is already inside the tree
		if (!Elem.inBox(xMin, xMax, yMin, yMax)) {
			return false;
		}
		success = true;
		// call the recursive method to delete
		prQuadNode n = recDelete(root, Elem, world);
		// if the delete fails, return false
		if (success == false) {
			return false;
		}
		root = n;
		return true;
	}

	/**
	 * recursive method to delete a element from the quad-tree and contract if
	 * needed
	 * 
	 * @param node
	 *            the node that roots the subtree
	 * @param elem
	 *            the element to be inserted
	 * @param rec
	 *            the region
	 * @return the new node
	 */
	@SuppressWarnings("unchecked")
	private prQuadNode recDelete(prQuadNode node, T elem, rectangle rec) {
		// if the node is null, delete fails and return null
		if (node == null) {
			success = false;
			return null;
		}

		// check if the node is an leaf
		if (node.isLeaf()) {

			prQuadLeaf leaf = (prQuadLeaf) node;

			// if the leaf only has one element
			if (leaf.numElements() == 1) {

				// check if the element is the right one
				if (leaf.getElement(0).equals(elem)) {
					// found element to be deleted, return null
					return null;
				}

				// element not found, delete fails and return null
				success = false;
				return null;
			}

			// leaf has multiple elements

			boolean removed = leaf.removeElement(elem);
			// an element has been removed
			if (removed) {
				return leaf;
			}

			// element not found, delete fails and return null
			success = false;
			return null;
		}

		// if the node is an internal node, find which quadrant to search for
		// next
		prQuadInternal internal = (prQuadInternal) node;
		Direction quadrant = elem.inQuadrant(rec.x1, rec.x2, rec.y1, rec.y2);

		// delete from NE
		if (quadrant == Direction.NE) {
			internal.NE = recDelete(internal.NE, elem, rec.getNE());
		}

		// delete from NW
		else if (quadrant == Direction.NW) {
			internal.NW = recDelete(internal.NW, elem, rec.getNW());
		}

		// delete from SW
		else if (quadrant == Direction.SW) {
			internal.SW = recDelete(internal.SW, elem, rec.getSW());
		}
		// delete from SE
		else {
			internal.SE = recDelete(internal.SE, elem, rec.getSE());
		}

		// ////clean up the quad-tree//////

		// if a node was successfully removed, the internal node has at most 1
		// child leaf-node and no internal-node, then contract the tree from
		// bottom up
		if (success && internal.numLeaf() <= 1 && internal.numInternal() == 0) {
			// move up the NE leaf-node
			if (internal.NE != null) {
				return internal.NE;
			}
			// move up the NW leaf-node
			else if (internal.NW != null) {
				return internal.NW;
			}
			// move up the SW leaf-node
			else if (internal.SW != null) {
				return internal.SW;
			}
			// move up the SE leaf-node
			return internal.SE;
		}

		// if the internal has more than 1 child leaf-nodes or internal nodes
		return internal;
	}

	/**
	 * find a element within quad-tree Pre: elem != null
	 * 
	 * @param Elem
	 *            the element to be found
	 * @return Returns reference to an element x within the tree such that
	 *         elem.equals(x)is true, provided such a matching element occurs
	 *         within the tree; returns null otherwise.
	 */
	public T find(T Elem) {
		// calls the recursive method to find Elem
		return recFind(root, Elem, world);
	}

	/**
	 * recursive method to find an element within the quad-tree
	 * 
	 * @param node
	 *            the node that roots the subtree
	 * @param elem
	 *            the element to be found
	 * @param rec
	 *            the region
	 * @return null if not found, and the element if found
	 */
	@SuppressWarnings("unchecked")
	private T recFind(prQuadNode node, T elem, rectangle rec) {
		// check for empty tree
		if (node == null) {
			return null;
		}

		// check if the node is an leaf
		if (node.isLeaf()) {

			prQuadLeaf leaf = (prQuadLeaf) node;
			// loops through all elements in leaf
			for (int i = 0; i < leaf.numElements(); i++) {
				// check if the element is the right one
				if (leaf.getElement(i).equals(elem)) {
					// return the element if found
					return leaf.getElement(i);
				}
			}
			// not found
			return null;
		}

		// if the node is an internal node, find which quadrant to look for next
		prQuadInternal internal = (prQuadInternal) node;
		Direction quadrant = elem.inQuadrant(rec.x1, rec.x2, rec.y1, rec.y2);

		// look at the NE quadrant
		if (quadrant == Direction.NE) {
			return recFind(internal.NE, elem, rec.getNE());
		}

		// look at the NW quadrant
		else if (quadrant == Direction.NW) {
			return recFind(internal.NW, elem, rec.getNW());
		}

		// look at the SW quadrant
		else if (quadrant == Direction.SW) {
			return recFind(internal.SW, elem, rec.getSW());
		}

		// look at the SE quadrant
		return recFind(internal.SE, elem, rec.getSE());

	}

	/**
	 * find all elements within quad-tree that's within a defined region Pre:
	 * xLo, xHi, yLo and yHi define a rectangular region
	 * 
	 * @param xLo
	 *            the minimum x of the defined region
	 * @param xHi
	 *            the maximum x of the defined region
	 * @param yLo
	 *            the minimum y of the defined region
	 * @param yHi
	 *            the minimum y of the defined region
	 * @return a collection of (references to) all elements x such that x is in
	 *         the tree and x lies at coordinates within the defined rectangular
	 *         region, including the boundary of the region.
	 */
	public Vector<T> find(float xLo, float xHi, float yLo, float yHi) {
		// calls the recursive method to look for all points within the defined
		// region
		return recFindAll(root, new rectangle(xLo, xHi, yLo, yHi), world);
	}

	/**
	 * recursive method to find all points within a specified region
	 * 
	 * @param node
	 *            the node that roots the subtree
	 * @param region
	 *            the defined region
	 * @param rec
	 *            the overall region
	 * @return the vector containing all points found within the defined region
	 */
	@SuppressWarnings("unchecked")
	private Vector<T> recFindAll(prQuadNode node, rectangle region,
			rectangle rec) {
		// create a new vector to contain the elements found
		Vector<T> result = new Vector<T>();

		// if node is null, return current vector
		if (node == null) {
			return result;
		}

		// check if the node is an leaf
		if (node.isLeaf()) {

			prQuadLeaf leaf = (prQuadLeaf) node;
			for (int i = 0; i < leaf.numElements(); i++) {

				// check if elem is within the region wanted
				if (leaf.getElement(i).inBox(region.x1, region.x2, region.y1,
						region.y2)) {
					result.add(leaf.getElement(i));
				}
			}
			// return the result vector
			return result;
		}

		// if the node is an internal node, get the 4 quadrants and search them
		// if possible solution exists
		prQuadInternal internal = (prQuadInternal) node;
		rectangle ne = rec.getNE();
		rectangle nw = rec.getNW();
		rectangle sw = rec.getSW();
		rectangle se = rec.getSE();

		// if the north-east region share points with the defined rectangle,
		// search it.
		if (region.sharePoint(ne)) {
			// add all of the results from the recursive findAll
			result.addAll(recFindAll(internal.NE, region, ne));
		}

		// if the north-west region share points with the defined rectangle,
		// search it.
		if (region.sharePoint(nw)) {
			// add all of the results from the recursive findAll
			result.addAll(recFindAll(internal.NW, region, nw));
		}

		// if the south-west region share points with the defined rectangle,
		// search it.
		if (region.sharePoint(sw)) {
			// add all of the results from the recursive findAll
			result.addAll(recFindAll(internal.SW, region, sw));
		}

		// if the south-east region share points with the defined rectangle,
		// search it.
		if (region.sharePoint(se)) {
			// add all of the results from the recursive findAll
			result.addAll(recFindAll(internal.SE, region, se));
		}
		return result;
	}

	/**
	 * @param Out
	 *            the initialized empty string
	 * @param root
	 *            the root of the tree
	 */
	public String printTree() {
		count = 0;
		if (root == null)
			Out += ("  Empty tree.\n");
		else
			printTreeHelper(root, "");

		return Out;
	}

	/**
	 * @param Out
	 *            The output string
	 * @param sRoot
	 *            the subroot of the tree
	 * @param Padding
	 *            the empty space used for better viewing
	 */
	@SuppressWarnings("unchecked")
	public String printTreeHelper(prQuadNode sRoot, String Padding) {

		// Check for empty leaf
		if (sRoot == null) {
			Out += (" " + Padding + "*L*\n");
			return Out;
		}
		// Check for and process SW and SE subtrees
		if (!sRoot.isLeaf()) {
			prQuadInternal p = (prQuadInternal) sRoot;
			printTreeHelper(p.SW, Padding + "---");
			printTreeHelper(p.SE, Padding + "---");
		}

		// Determine if at leaf or internal and display accordingly
		if (sRoot.isLeaf()) {
			prQuadLeaf p = (prQuadLeaf) sRoot;
			Out += Padding;
			for (int pos = 0; pos < p.Elements.size(); pos++) {
				Out += p.Elements.get(pos);

				// prints an output every 100 leaf found
				count++;
				if (count % 100 == 0) {
					System.out.println(count + " records added to tree");
				}
			}
			Out += "\n";

		} else if (!sRoot.isLeaf())
			Out += (Padding + "@I@\n");
		else
			Out += (sRoot.getClass().getName() + "#\n");

		// Check for and process NE and NW subtrees
		if (!sRoot.isLeaf()) {
			prQuadInternal p = (prQuadInternal) sRoot;
			printTreeHelper(p.NE, Padding + "---");
			printTreeHelper(p.NW, Padding + "---");
		}
		return Out;
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
//<Elton Gladstone>

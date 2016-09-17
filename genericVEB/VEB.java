/******************************************************
 *Program:
 *      VEB.java
 *Purpose:
 *      To demonstrate Van Emde Boas trees using integer keys.
 *      valid keys range from 0 to n-1, where n is universe size.
 *      NOTE: a key value of -1 indicates an empty field..(i.e. null)
 *Programmer:
 *       Armando Diaz T.
 *Last Modified:
 *          March 7, 2010
 ******************************************************/

package genericveb;
import java.lang.Math.*;

public class VEB {

	// DATA MEMBERS ------------------------------------------
	private int u; //size of tree
	private int max, min; // the minimum & maximum values
        private int upperRoot, lowerRoot;//if u is an odd power of 2...
                //...upperRoot = 2*lowerRoot, upperRoot * lowerRoot = u

	//cluster & summary will be null for u = 2
	private VEB summary = null; //summary tree
	private VEB[] cluster = null; // pointers to subtree


        //CONSTRUCTOR ----------------------------------------------
        public VEB(int size) {

		u = hiPowerOfTwo(size);//in case u isn't a pwr of 2

		//calculate the high & low roots
                double sqRoot = Math.sqrt(u);
		upperRoot = hiPowerOfTwo(sqRoot);
		lowerRoot= lowPowerOfTwo(sqRoot);


		if(u > 2) {//recursivley create subtrees
			summary = new VEB(upperRoot);
			cluster = new VEB[upperRoot];
			for(int i=0; i<upperRoot; i++)
				cluster[i] = new VEB(lowerRoot);//loop creates structures
		}

		max = min = -1; // -1 means null
	}

        //HELPER METHODS ------------------------------------------------------

        /* hiPowerOfTwo()
         *  helper method, returns a power of two that is @ least as
         *  large as given input
         */
         private static int hiPowerOfTwo(double num){

            double power = 1;
            while(power < num)
                power *= 2;
            return (int)power;//this ensures value is @ least as large as num...
                    //...and is a power of two
        }
         /* lowPowerOfTwo()
          *  helper method, returns a power of 2 that is one power less than the
          *  value hiPowerOfTwo() would return
          */
        private static int lowPowerOfTwo(double num){
            double power = 1;
            while(power *2 <= num)
                power *= 2;
            return (int)power;
        }


	/*
	 *	low() is a helper method that returns the lower bits of x
	 *	the lower bits determine position of x within a cluster(i.e.
	 *	sub tree
	*/
	private int low(int x) {

		return (int)x%lowerRoot;
	}

	/*
	 *	high() is a helper method that returns the higher bits of x
	 *	the higher bits determine the cluster x belongs in within the
	 *	sub tree array
	*/
	private int high(int x) {

		return ((int)x/lowerRoot);
	}

	/*
	 *	buildIndex() is a helper method that reconstructs the value of a key within
	 *	the universe size of the current VEB structure. x is the cluster index of
	 *	the key & y is it's position within that cluster
	*/
	private int buildIndex(int x, int y) {

		return (x*lowerRoot+y);
	}
        // PUBLIC METHODS -----------------------------------------------

	/*
	 *	minimum() returns the smallest value in the argument tree
	*/
	public static int minimum(VEB tree) {
		return tree.min;
	}

	/*
	 *	maximum() returns largest value in argument tree
	*/
	public static int maximum(VEB tree) {
		return tree.max;
	}

	/*
	 *	memberOf(int x) returns true if argument is member
	 *	of the argument VEB tree
	*/
	public static boolean memberOf(VEB tree, int x) {

		// base cases, x equals min or max, or doesn't equal
		// either & we're @ the base size of 2
		if( x == tree.min | x == tree.max)
			return true;
		else if(tree.u == 2)
			return false;
		else {
			// recursive case, look @ next level down
			int recIndex = tree.high(x);
			return memberOf(tree.cluster[recIndex], tree.low(x));
		}
	}

	/* successor()
	 *  returns value of successor to argument integer.
	 *  -1 is returned if no such element exists. If not base case
         *  then must recurse down current cluster (if x < cluster's max)
         *   or succeeding cluster (if it exists).
         *
	*/
	public static int successor(VEB tree, int x) {
		if(tree.u == 2) {//base case
			if( x == 0 && tree.max == 1)
				return 1;
			else
				return -1;
		}
                //for case when successor isn't in same cluster...
		else if(tree.min != -1 && x < tree.min)
			return tree.min;
		else {
			//check if current cluster's max > x
			int index = tree.high(x);
			int recX = tree.low(x);// value of x in lower cluster
			int maxVal = maximum(tree.cluster[index]);
                        //x's successor is in current cluster...
			if (maxVal != -1 && recX < maxVal){
				int offset = successor(tree.cluster[index], recX);
				return tree.buildIndex(index, offset);// reconstruct x value
			}
			else {
				//successor must be in another cluster
				int cIndex = successor(tree.summary, index);//find succeeding cluster
				if(cIndex == -1)
					return -1;//no higher clusters exist
				else {
                                        //find offset & reconstruct successor
					int offset = minimum(tree.cluster[cIndex]);
					return tree.buildIndex(cIndex, offset);
				}
			}
		}

	}

	/* predecessor()
         *  returns largest integer smaller than x or -1
         *  if no such element exists. Recursive case
         *  checks if predecessor is in current cluster
         *  otherwise checks preceding cluster...
         */
        public static int predecessor(VEB tree, int x) {
            //base case where u = 2 and pred is in same cluster)...
		if(tree.u == 2) {
			if( x == 1 && tree.min == 0)
				return 0;
			else
				return -1;
		}
                //for recursive case where we check...
                //preceding cluster
		else if(tree.max != -1 && x > tree.max)
			return tree.max;
		else { //recursive case

			//check if current cluster's min < x
			int index = tree.high(x);
			int recX = tree.low(x);// value of x in lower cluster
			int minVal = minimum(tree.cluster[index]);
                        // predecessor is in the same tree;
			if (minVal != -1 && recX > minVal){
				int offset = predecessor(tree.cluster[index], recX);
				return tree.buildIndex(index, offset);// reconstruct x value
			}
			else {
				//precessor must be in a lower cluster
				int cIndex = predecessor(tree.summary, index);//find succeeding cluster
				if(cIndex == -1) {
				// check if predecessor higher up tree...
                                // unlike the max there's a possibility that pred
                                // may not be in subtrees, but may exist as the min
                                // of a greater structure
					if(tree.min != -1 && x > tree.min)
						return tree.min;
					else
						return -1;
				}
				else {
                                        //found pred in lower cluster
					int offset = maximum(tree.cluster[cIndex]);
					return tree.buildIndex(cIndex, offset);
				}
			}
		}
	}

	/*
	 *	emptyInsert() places a value into an empty subtree
	*/
	private static void emptyInsert(VEB tree, int x) {
		tree.min = x;
		tree.max = x;
	}

        /* insert()
         *  recursive insert alogrithm, uses the emptyInsert() helper
         *  to handle base case of empty tree...
         */
	public static void insert(VEB tree, int x) {
            //ensure unduly large values aren't inserted
            if(x > tree.u){
                System.out.println("Error x is too large for universe size.");
                return;
            }
            if(tree.min == -1)
		emptyInsert(tree, x);//easy insert when tree is empty
            else {
		if(x<tree.min) {//min & x are swapped
                    int temp = x;
                    x = tree.min;//now x contains old minimum
                    tree.min = temp;
		}
		//whether x was swapped or not, x contains value to be inserted
		if(tree.u > 2) {
                    int index = tree.high(x); // the high bits of x
                    //case where VEB structure is empty
                    if(tree.cluster[index].min == -1) {
                        insert(tree.summary, index);//indicate that subtree isn't empty
                        emptyInsert(tree.cluster[index], tree.low(x));
                    }
                    //case where structure isn't empty
                    else
                        insert(tree.cluster[index], tree.low(x));


		}//as recursion unwinds, max is updated
		if( x > tree.max)
                    tree.max = x; //max values do reside in subTrees
            }

	}
        /* del()
         *  used as a wrapper around delete() method
         *  delete() makes no check to ensure that item x
         *  exists in tree. Thus del() first checks for
         *  item x to exist before attempting deletion
         */
        public static void del(VEB tree, int x){
            if(memberOf(tree, x))
                delete(tree, x);
        }
	private static void delete(VEB tree, int x) {
            //deleting from 1 element base tree
            if(tree.min == tree.max)
		tree.min = tree.max = -1;
            //deleting from 2-element base tree
            else if(tree.u == 2) {
		if(x == tree.min)
                    tree.min = tree.max;
		else
                    tree.max = tree.min;
            }
            //recursive cases
            else {
		//if x is the minimum of a non-base tree, find new min
		if(x == tree.min) {
                    int lowestCluster = minimum(tree.summary);
                    x = tree.buildIndex(lowestCluster, minimum(tree.cluster[lowestCluster]));
                    tree.min = x;// now x has new value to delete
		}
                // delete x recursively
		int clusterIndex = tree.high(x);
		delete(tree.cluster[clusterIndex], tree.low(x));

		//upon return check if cluster is empty
		if(tree.cluster[clusterIndex].min == -1) {
                    delete(tree.summary, clusterIndex);
                    //if max is deleted, then get new max value
                    if(x == tree.max) {
			int maxCluster = maximum(tree.summary);
                        if(maxCluster == -1)
                            tree.max = tree.min;
                        else
                            tree.max = tree.buildIndex(maxCluster, maximum(tree.cluster[maxCluster]));
                    }

		}
                else if(x == tree.max) {
                    //in the case that max is deleted, but cluster high(x) still has values
                    int maxCluster = maximum(tree.summary);
                    tree.max = tree.buildIndex(maxCluster, maximum(tree.cluster[maxCluster]));
		}
            }

	}
}
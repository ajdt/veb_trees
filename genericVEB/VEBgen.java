/******************************************************
 *Program:
 *      VEBgen.java
 *Purpose:
 *      Implements a Van Emde Boas tree. Stored data objects
 *      must implement the "VEBKey" interface. This interface
 *      requires the getKey() method which is used as a key
 *      to store, delete, and lookup values in the tree. VEBKey
 *      is defined generically, so any object type can be used
 *      but it is up to the implementer to come up w/ a unique
 *      integer index for all possible values of the tree.
 *Programmer:
 *       Armando Diaz T.
 *Last Modified:
 *          March 9, 2010
 ******************************************************/
//NOTES::
//check on immutability of Strings!!! change interface!!!

package genericveb;
import java.lang.Math;

public class VEBgen {

    //private data members
    private int size, upperRoot, lowerRoot;//size of tree (must be a power of 2)
    private VEB summary; // summarizes the clusters FIX THIS!!!!!!!!!!!!!!!!!!
    private VEBgen[] cluster;
    private VEBKey max, min;
    private int maxKey, minKey;//the keys of the min/max objects


    public VEBgen(int u){
        size = hiPowerOfTwo(u);
        //initially, summary, cluster, max & min are all null
        //...if u=2, then they remain null
        double sqRoot = Math.sqrt(size);
		upperRoot = hiPowerOfTwo(sqRoot);
		lowerRoot= lowPowerOfTwo(sqRoot);
        if(u > 2) {
           
            //setup summary & VEBgen cluster
            summary = new VEB(upperRoot);
            cluster = new VEBgen[upperRoot];
            //recursively create SubTrees
            for(int i = 0; i<upperRoot; i++)
                cluster[i] = new VEBgen(lowerRoot);
        }
        //denote tree as empty
        //max & min will already be null
        maxKey = minKey = -1;// -1 is invalid, denotes empty tree

    }private static int hiPowerOfTwo(double num){

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

   
    /*******maximum()
     *      returns element w/largest key
     *      value in VEBgen structure
     *************************************/
    public VEBKey maximum() {
        return max;
    }
    /******minimum()
     *      returns element w/smallest key
     *      value in VEBgen structure
     ************************************/
    public VEBKey minimum(){
        return min;
    }
    /******maxKey():
     *      returns largest key value instead
     *      of VEBKEY object, needed b/c
     *      summary structures store "null"
     *      only keys, not VEBKey satellite data
     *********************/
    private int maxKeyVal() {
        return maxKey;
    }
    /**** minKey():
     *      returns smallest key value in tree.
     *      Needed for same reason as maxKey
     ********************/
    private int minKeyVal() {
        return minKey;
    }
    public boolean memberOf(VEBKey item){
        //no need to pass VEBKey object recursively
        //...checking for key suffices
        int keyVal = item.getKey();
        return isMember(this, keyVal);
    }
    private static boolean isMember(VEBgen tree, int key){
        //BASE CASE: tree equals minKey or maxKey, or
        //we've encountered base size & key wasn't found
        if(key == tree.minKey | key == tree.maxKey)
            return true;
        else if(tree.size == 2)
            return false;
        else {//not found & size != 2
            int index = tree.clusterIndex(key);
            int subValue = tree.keySubVal(key);

            //check a subtree for value...
            return isMember(tree.cluster[index], subValue);
        }
    }
    /*******insertRec:
     *      helper method, recursively inserts a value into tree,
     *      using key & the object itself.
     *      Called by public insert method
     ***********************************************************/
    private static void insertRec(VEBgen tree, int key, VEBKey obj){
        if(tree.minKey == -1)//tree is empty
            emptyInsert(tree, key, obj);
        else{
            if(key < tree.minKey){//min & obj are swapped
                VEBKey tempObj = obj;
                int tempKey = key;//temporarily store
                //swap values
                obj = tree.min;
                key = tree.minKey;
                tree.min = tempObj;
                tree.minKey = tempKey;
            }
            //obj & key refer to value to insert regardless of swap
            if(tree.size > 2){
               int clusterIdx = tree.clusterIndex(key);
               int subKy = tree.keySubVal(key);
               // check if subtree is empty, if so update..
               //"summary" structure first
               if(tree.cluster[clusterIdx].minKey == -1)
                   //insertRec(tree.summary, subKy, null);
                   VEB.insert(tree.summary, subKy);
               insertRec(tree.cluster[clusterIdx], subKy, obj);//POSSIBLE INSERT ERRROR HERE!!!!!!!!1

            }
            if( key > tree.maxKey){
                tree.maxKey = key;//max can reside in subtrees too...
                tree.max = obj;
            }
        }
    }
    /*******insert():
     *      public method for insertion, generates key, then
     *      passes actual insertion op onto insertRec() method
     *****************************/
    public void insert(VEBKey obj){
        int key = obj.getKey();
        insertRec(this, key, obj);
    }
    /******emptyInsert():
     *      helper method inserts a value into an empty tree
     *      Didn't have to be separate method, could've been a
     *      part of insertRec();
     ************************************************************/
    private static void emptyInsert(VEBgen tree, int key, VEBKey obj){
            tree.maxKey = tree.minKey = key;
            tree.max = tree.min = obj;
    }
    /*****delete():
     *      returns true if delete successful,
     *      retrieves object's key value, then
     *      passes delete onto deleteRec()
     *********************************/
    public void delete(VEBKey obj){
        int key = obj.getKey();
        deleteRec(this, key, obj);//only key is needed for delete operation
    }
    /****deleteRec():
     *      returns true if delete succeeds, recursively
     *      handles delere of item w/ a certain key
     *************************************************/
    private static void deleteRec(VEBgen tree, int key, VEBKey obj){
        //there's a bug in this, first call deletes even if
        //item isn't the correct one
        if(tree.minKey == tree.maxKey){
            tree.minKey = tree.maxKey = -1;
            tree.max = tree.min = null;
        }
        //delete from base tree w/ 2 elems
        else if(tree.size == 2) {
            if(key == tree.minKey){
                tree.min = tree.max;
                tree.minKey = tree.maxKey;
            }
            else{
                tree.max = tree.min;
                tree.maxKey = tree.minKey;
            }
            
        }
        //recursive cases
        else {
            //if key is min of non-base tree
            if(key == tree.minKey){
                int lowestCluster = VEB.minimum(tree.summary);
                //two calls to "lowestCluster" are made, but
                //both take constant time...
                obj = tree.cluster[lowestCluster].minimum();
                key = tree.buildIdx(lowestCluster,
                        tree.cluster[lowestCluster].minKeyVal());
                tree.min= obj;
                tree.minKey = key;
            }
            //delete item recursively
            int clusIdx = tree.clusterIndex(key);
            int subVal = tree.keySubVal(key);
            deleteRec(tree.cluster[clusIdx], subVal, obj);
            // upon return...
            //check if cluster is empty
            if(tree.cluster[clusIdx].minKey == -1){
                //deleteRec(tree.summary, clusIdx, null);
                VEB.del(tree.summary, clusIdx);
                if(key == tree.maxKey){
                    int maxIdx = VEB.maximum(tree.summary);
                    if(maxIdx == -1){
                        tree.maxKey = tree.minKey;
                        tree.max = tree.min;
                    }
                    else{
                        tree.maxKey = tree.buildIdx(maxIdx, tree.cluster[maxIdx].maxKey);
                        tree.max = tree.cluster[maxIdx].max;
                    }
                }                
            }
            else {
                if(key == tree.maxKey){
                    tree.maxKey = tree.cluster[clusIdx].maxKey;
                    tree.max = tree.cluster[clusIdx].max;
                }
            }
        }

    }
    public  VEBKey successor(VEBKey obj){
        int key = obj.getKey();
        return successorRec(this, key);
    }
    public static VEBKey successorRec(VEBgen tree, int key){
        // base case
        if(tree.size == 2){
            if(key == 0 && tree.maxKey == 1)
                return tree.max;//return minimum VEBgen object
            else
                return null;//object has no successor
        }
        else if(tree.minKey != -1 && key < tree.minKey)
            return tree.min;//return min object
        else {
            //check if cluster's max is greater than key
            int keyIdx = tree.clusterIndex(key);
            int otherMax = tree.cluster[keyIdx].maxKey;
            if(otherMax != -1 && otherMax > tree.keySubVal(key))
                return successorRec(tree.cluster[keyIdx], tree.keySubVal(key));
            else{//check for successor in succeeding cluster
                int nextCluster = VEB.successor(tree.summary, keyIdx);
                if(nextCluster == -1)
                    return null;
                else
                    return tree.cluster[nextCluster].min;
            }
        }
    }
    public  VEBKey predecessor(VEBKey obj){
        int key = obj.getKey();
        return predecessorRec(this, key);
    }
    public static VEBKey predecessorRec(VEBgen tree, int key){
        if(tree.size == 2){
            if(key == 1 && tree.minKey == 0)
                return tree.min;
            else
                return null;
        }
        else if(tree.max != null && key > tree.maxKey)
            return tree.max;
        else{
            int keyIdx = tree.clusterIndex(key);
            int clusMin = tree.cluster[keyIdx].minKey;
            //predecessor is in same cluster
            if(clusMin != -1 && tree.keySubVal(key)> clusMin)
                return predecessorRec(tree.cluster[keyIdx], tree.keySubVal(key));
            else{
                //check Previous cluster
                int prevCluster = VEB.predecessor(tree.summary, keyIdx);
                if(prevCluster == -1){
                    if(tree.minKey != -1 && key > tree.minKey)
                        return tree.min;
                    else
                        return null;
                }
                else
                    return tree.cluster[prevCluster].max;
            }
        }
    }
    
    /*****clusterIndex():
     *      returns the index of the cluster
     *      a given key would be found in...
     *      formula:    high = x/ sqrt(size of Tree)
     *******************************/
    private int clusterIndex(int key){
        return ((int)key/lowerRoot);
    }
    /*******keySubVal():
     *      returns subValue that a key would have
     *      in a subtree. example:
     *      if key = 0011 1010
     *      the "1010" low bits identify the key in
     *      next Recursive VEBgen structure
     ****************************/
    private int keySubVal(int key){
        return (int)key%lowerRoot;
    }
    /******buildIdx:
     * Given sub value of key within a subtree & that
     * subtree's index, corresponding value of key in
     * current VEBgen structure is reconstructed
     ***************************************/
    private int buildIdx(int idx, int subVal){
        return (idx*lowerRoot+subVal);
    }

}

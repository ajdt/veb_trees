/******************************************************
 * Program: AVLNode.java
 *
 * Purpose: class for a Node in an AVL tree. Helper methods
 *  to maintain AVL property are provided ex: notBalanced()
 *  and rebalance(). Helper methods to find the max/min
 *  in a subtree as well as height of a node are provided too.
 *
 * Programmer: Armando Diaz T.
 *
 * Last Modified: March 19, 2010
 *****************************************************/
public class AVLNode {

    // DATA MEMBERS ----------------------------------
   public AVLNode left, right;
   public int value;
   public int height;

   //CONSTRUCTORS -----------------------------------

   //constructor w/children or child
   public AVLNode(AVLNode l, AVLNode r, int data){
       value = data;
       left = l;
       right = r;
       height = 1 +max(l.height, r.height);
   }
   //singelton constructor
   public AVLNode(int data){
       value = data;
       height = 0;
   }
   //HELPER METHODS ------------------------

    /* retMax()
     *  iteratively finds max by going
     * as far right as possible, returns
     * value of rightmost node.
     */
    public static int retMax(AVLNode tree){
        //getMax ensures tree isn't empty
        AVLNode iter = tree;
        while(iter.right != null)
            iter = iter.right;
        return iter.value;

    }
    /*retMin()
     *  finds min iterativel by goingy
     *  as far left as possible, returns
     *  leftmost node's value.
     */
    public static int retMin(AVLNode tree){
        //getMin ensures tree isn't empty
        AVLNode iter = tree;
        while(iter.left != null)
            iter = iter.left;
        return iter.value;
    }
    /* max()
     *  determines & returns max between two values
     */
    public static int max(int x, int y){
        if(x>y)
            return x;
        else
            return y;
    }
    /*updateHeight()
     *  public method for Node to update height
     *  tree class calls method for nodes to update after
     * deletion/insertion
     */
    public void updateHeight(){
        int hLeft = heightOf(left);
        int hRight = heightOf(right);
        
        height = 1 + max(hLeft, hRight);
    }
    /*heightOf()
     * helper method to deal w/ null nodes
     */
    public static int heightOf(AVLNode node){
        if(node == null)
            return -1;
        else
            return node.height;
    }
    /* notBalanced()
     *  public method to check if a given node is not balanced...
     */
    public static boolean notBalanced(AVLNode node){
        int diff = (heightOf(node.left)-heightOf(node.right));
        if (diff >= 2 | diff <= -2)//inbalance property
            return true;
        else
            return false;
    }
    /* rebalance()
     * called by insert or delete...
     * considers 4 cases: LL, LR, RL, RR
     */
    public static AVLNode rebalance(AVLNode node){
        //get heights of left & right
        int hLeft = heightOf(node.left);
        int hRight = heightOf(node.right);
        //heights of inbalanced side's children
        int hSubLeft, hSubRight;

        if(hLeft > hRight){//left side inbalance
            hSubLeft = heightOf(node.left.left);
            hSubRight = heightOf(node.left.right);
            if(hSubLeft > hSubRight)//LL case
                node = rotateLChild(node);
            else {
                //LR two rotations necessary...
                node.left = rotateRChild(node.left);
                
                node = rotateLChild(node);
            }
        }
        else{//hLeft < hRight
            hSubLeft = heightOf(node.right.left);
            hSubRight = heightOf(node.right.right);
            if(hSubRight > hSubLeft)//RR case
                node = rotateRChild(node);
            else{
                //RL case
                node.right = rotateLChild(node.right);
                node = rotateRChild(node);
                
            }

        }
        //recalculate height
        node.updateHeight();
        return node;
    }
    /*rotateRChild()
     *  rotates a node w/it's right child for
     *  rebalancing purposes
     */
    private static AVLNode rotateRChild(AVLNode node){
       
        AVLNode temp = node.right;

        node.right = temp.left;
        temp.left = node;
        //update heights
        node.updateHeight();
        temp.updateHeight();
        return temp;
        
    }
    /*rotateLChild()
     *  rotates a node w/ left child to
     *  rebalance subtree
     */
    private static AVLNode rotateLChild(AVLNode node){
        AVLNode temp = node.left;
        node.left = temp.right;
        temp.right = node;
        //update heights
        node.updateHeight();
        temp.updateHeight();
        return temp;
    }
}

/******************************************************
 * Program: AVLTree.java
 *
 * Purpose: to implement an AVL tree using the AVLNode
 *          class. This implementation doesn't allow for
 *          duplicates.
 * Programmer: Armando Diaz T.
 *
 * Last Modified: March 19, 2010
 *****************************************************/


public class AVLTree {
    

    // DATA MEMBERS -------------------------------------
    private AVLNode tree; // root node
    //flag to signal operation successes
    private static boolean opSuccess = true;
    
    //PUBLIC METHODS------------------------------------

    /* getMax()
     *  calls AVLNode.recMax to get maximum value
     */
    public Integer getMax(){
        if(isEmpty())//null returned if tree is empty
            return null;
        else
            return new Integer(AVLNode.retMax(tree));
    }
    /* getMin()
     *  calls AVLNode.recMin to get min value
     */
    public Integer getMin(){
        if(isEmpty())
            return null;
        else
            return new Integer(AVLNode.retMin(tree));
    }
    /* insert()
     * returns true if op succeeds
     * calls recInsert() to perform deletion
     */
    public boolean insert(int x){
        opSuccess = true;//set flag to true first
        tree = recInsert(x, tree);
        return opSuccess;

    }
    
    /*delete()
     *  returns true if deletion succeeds otherwise false...
     *  calls deleteRec() for actual deletion
     */
    public boolean delete(int x){
        //set opSuccess to true 
        opSuccess = true;
        //method call
        deleteRec(x, tree);
        return opSuccess;
    }
    /*predecessor()
     *  returns null if x has no predecessor
     *  algorithm looks for parent of x, to
     *  find predecessor...
     */
    public Integer predecessor(int x){
        //get parent of node
        AVLNode parent = findParent(x);
        AVLNode xNode;//actual node

        //x isn't in tree if parent is null & x != root...
        if(parent == null && x != tree.value)
            return null;
        //get xNode & check left side...
        if(parent == null){
            xNode = tree;
            //if left is null then no pred
            if(xNode.left == null)
                return null;
        }
        else {
            if(x > parent.value)
                xNode = parent.right;
            else
                xNode = parent.left;
            //if left is null & xNode is a
            //right node, then parent is pred
            // otherwise there's no pred
            if(xNode.left == null)
                if(x> parent.value)
                    return new Integer(parent.value);
                else
                    return null;
        }
        //xNode.left cannot be null @ this point
        return new Integer(AVLNode.retMax(xNode.left));
    }
    /* successor()
     *  analogous to predecessor(), finds parent of x
     *  then tries to find successor based on result
     */
    public Integer successor(int x){
        //get parent of node
        AVLNode parent = findParent(x);
        AVLNode xNode;//actual node

        //x isn't in tree...
        if(parent == null && x != tree.value)
            return null;
        //get xNode & check right side...
        if(parent == null){
            xNode = tree;
            //if right is null then no succ
            if(xNode.right == null)
                return null;
        }
        else {
            if(x > parent.value)
                xNode = parent.right;
            else
                xNode = parent.left;
            //if right is null & xNode is a
            //left node, then parent is succ
            // otherwise there's no succ
            if(xNode.right == null)
                if(x < parent.value)
                    return new Integer(parent.value);
                else
                    return null;
        }
        //xNode.left cannot be null @ this point
        return new Integer(AVLNode.retMin(xNode.right));

    }
    /* printInOrder()
     *  calls on recPrint() to print entire tree in order
     */
    public void printInOrder(){
        recPrint(tree);
    }
    /*isEmpty()
     * helper to detect empty tree
     */
    public boolean isEmpty(){
        return (tree == null);
    }
    /* memberOf()
     *  returns true if x is a member of tree
     *  Uses findParent() if node isn't root.
     */
    public boolean memberOf(int x){
        AVLNode parent = findParent(x);
        if( x == tree.value)
            return true;
        else
            return (parent != null);

    }
    
    //HELPER METHODS------------------------------------------

    /*findParent():
     *  finds the parent of a given node iteratively
     */
    private AVLNode findParent(int x){
        //tree is empty or arg equals root's value
        if(isEmpty() | x == tree.value)
            return null;
        AVLNode parent = tree;
        boolean found = false;//flag for when parent is found
        while(parent != null && !found){
            if(x > parent.value && parent.right != null){
                if(parent.right.value == x)//check right side
                    found = true;
                else
                    parent = parent.right;
            }
            else{//check left side
                if(parent.left != null && parent.left.value == x)
                    found = true;
                else
                    parent = parent.left;
            }
        }

        return parent;
    }
    /*recInsert()
     * recursively inserts a data member, if item already exists
     * function returns & sets opSuccess to false
     */
    private static AVLNode recInsert(int x, AVLNode node){
        if(node == null)
            return new AVLNode(x);
        if(x > node.value)
            node.right = recInsert(x, node.right);
        else
            if( x < node.value)
             node.left = recInsert(x, node.left);
        if(x == node.value)
            AVLTree.opSuccess = false; //insertion failed

        //recalculate height 
        node.updateHeight();

        //check for inbalances
        if(AVLNode.notBalanced(node))
            node = AVLNode.rebalance(node);

        return node;
    }

    /*deleteRec()
     *  recursively delete item x, set opSuccess to
     *  false if item doesn't exist in tree. Calls on
     *  AVL.rebalance() to rebalance subtrees if needed
     */
    private static AVLNode deleteRec(int x, AVLNode node){
        if(node == null){
            opSuccess = false;
            return node;
        }
        //node is not found yet...
        if(x > node.value)
            node.right = deleteRec(x, node.right);
        if(x < node.value)
            node.left = deleteRec(x, node.left);
        //node is found
        if(x == node.value){
            //base case where node has no children
            if(node.left == null && node.right == null)
                return null;
            else{
                if(node.left !=null){
                    node.value = AVLNode.retMax(node.left);
                    node.left = deleteRec(node.value, node.left);
                }
                else {
                    node.value = AVLNode.retMin(node.right);
                    node.right = deleteRec(node.value, node.right);
                }
            }
        }
        //recalculate height
        
        node.updateHeight();
        
        
        //check for inbalances
        if(AVLNode.notBalanced(node))
            node = AVLNode.rebalance(node);
        return node;
    }  
    private static void recPrint(AVLNode node){
        if(node == null)
            return;
        else {
            recPrint(node.left);
            System.out.print(" "+node.value+" ");
            recPrint(node.right);
        }       
    }

}

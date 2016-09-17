
/******************************************************
 *Program:
 *      TestRT.java
 *Purpose:
 *      created to test the AVLTree and VEB java classes
 *      identical operations are performed on an instance
 *      of each class & a report of runtimes is produced
 *
 *Programmer:
 *       Armando Diaz Tolentino
 *Last Modified:
 *          March 20, 2010
 ******************************************************/
import java.io.*;

public class TestRT {
    //DATA MEMBERS -----------------------------------
    BufferedReader insFile;//file for insertions
    BufferedReader delFile;//file for deletion /memberOf
    int size;//num of elements in trees
    private static final int DEL_SIZE = 10;

    //tree data structures
    VEB vebTree;
    AVLTree avlTree;

    public static void main(String args[])
            throws FileNotFoundException, IOException{
        TestRT tst = new TestRT();
    }
    //CONSTRUCTOR-------------------------------------
    public TestRT()throws FileNotFoundException, IOException {
        //initialize data members
        insFile = new BufferedReader(new FileReader("insFile2.txt"));
        delFile = new BufferedReader(new FileReader("delFile2.txt"));
        size = 0;

        
        vebTree = new VEB(4000000);//size is 4 million
        avlTree = new AVLTree();
       report();//create test report
    }
    /*report()
     *  calls helper methods to produce a report
     *  of runtimes
     */
    private void report() throws IOException {
        int[] orders = {10, 100, 1000, 10000, 100000, 200000, 500000, 900000};
        String heading = "\nExecution Time for n = ";
        String hRule = "----------------------------";

        for(int i =0; i < 8; i++){
            //print headings
            System.out.println(heading+orders[i]);            
            setw("AVL", 23, true, " ");//right justified
            setw("VEB", 10, true, " ");
            System.out.print("\n");
            //call each report
            insertRep(orders[i]);
            deleteRep();
            memberRep();
            maxRep();
            minRep();
            System.out.println(hRule);//horizontal rule
            
        }

    }
    /*insertRep()
     *  performs insertions until total size = max_insert
     *  displays average execution time for AVL & VEB tree
     */
    private void insertRep(int max_insert)throws IOException{
        int divisor = max_insert - size+1;//# of insertions
        //accumulators & other time vars
        long avlTime, vebTime, totalAVL =0, totalVEB=0;
        int value = 100;//current insertiom value
        String number;

        for(int i = size; i< max_insert; i++){
            number = insFile.readLine();
            value = Integer.parseInt(number);
            
            vebTime = System.nanoTime();//veb time before
            VEB.insert(vebTree, value);
            vebTime = System.nanoTime() - vebTime;//total VEB time
            avlTime = System.nanoTime();//avl time before
            avlTree.insert(value);
            avlTime = System.nanoTime() - avlTime;//total AVL time
            //update accumulators
            totalAVL += avlTime;
            totalVEB += vebTime;
        }
        //divide by number of insertions to get mean
        totalVEB /= divisor;
        totalAVL /= divisor;
        //print results
        System.out.print("\n");
        setw("Insert:\t", 15);//left justify
        setw(Long.toString(totalAVL), 10);        
        setw(Long.toString(totalVEB), 10);
        System.out.print("\n");
        //update size
        size = max_insert;
    }
    /* deleteRep()
     *  performs ten deletion ops, & averages runtimes for
     *  VEB & AVL. Subtracts 10 from global "size" even if not
     *  all deletes were successful. We don't care about exact
     *  size of trees only approx. size.
     */
    private void deleteRep()throws IOException {
        int delArray[] = nextArray();//array of values to delete
        //long vars to handle time
        long avlTime, vebTime, totalAVL =0, totalVEB=0;

        for(int i=0; i< DEL_SIZE; i++){
            avlTime = System.nanoTime();//before AVL del
            avlTree.delete(delArray[i]);
            avlTime = System.nanoTime() - avlTime;
            vebTime = System.nanoTime();//before VEB del
            VEB.del(vebTree, delArray[i]);
            vebTime = System.nanoTime() - vebTime;

            //update accumulators
            totalAVL += avlTime;
            totalVEB += vebTime;
        }
        //divide by # of deletions
        totalAVL /= DEL_SIZE;
        totalVEB /= DEL_SIZE;
        //print results
        System.out.print("\n");
        setw("Delete:\t", 15);//left justify
        setw(Long.toString(totalAVL), 10);
        setw(Long.toString(totalVEB), 10);
        System.out.print("\n");
        //update size
        size -= 10;

    }
    /* memberRep()
     *  uses an array of 10 random ints & checks AVL & VEB
     *  for membership of these ints. displays average runtime
     */
    public void memberRep() throws IOException {
        int memArray[] = nextArray();
        long avlTime, vebTime, totalAVL =0, totalVEB=0;

        for(int i=0; i< DEL_SIZE; i++){
            avlTime = System.nanoTime();
            avlTree.memberOf(memArray[i]);
            avlTime = System.nanoTime() - avlTime;
            vebTime = System.nanoTime();
            VEB.memberOf(vebTree, memArray[i]);
            vebTime = System.nanoTime() - vebTime;

            //update accumulators
            totalAVL += avlTime;
            totalVEB += vebTime;
        }
        //divide by # of checks
        totalAVL /= DEL_SIZE;
        totalVEB /= DEL_SIZE;
        //print results
        System.out.print("\n");
            setw("MemberOf:\t", 15);//left justify
            setw(Long.toString(totalAVL), 10);
            setw(Long.toString(totalVEB), 10);
            System.out.print("\n");
        
    }
    public void maxRep(){
        long avlTime, vebTime;
        Integer avlMax;
        int vebMax;

        avlTime = System.nanoTime();//before avl max
        avlMax = avlTree.getMax();
        avlTime = System.nanoTime() - avlTime;

        vebTime = System.nanoTime();
        vebMax = VEB.maximum(vebTree);//before veb max
        vebTime = System.nanoTime()- vebTime;

        //display results
        //display error message if results aren't the same
        if(vebMax == -1 && avlMax != null)
            System.out.println("Error: VEB & AVL " +
                    "returned different values for max");
        else if(avlMax == null && vebMax != -1)
            System.out.println("Error: VEB & AVL " +
                    "returned different values for max");
        else if(avlMax.intValue() != vebMax)
            System.out.println("Error: VEB & AVL " +
                    "returned different values for max");
        else{
            System.out.print("\n");
            setw("Max:\t", 15);//left justify
            setw(Long.toString(avlTime), 10);
            setw(Long.toString(vebTime), 10);
            System.out.print("\n");
        }

    }
    public void minRep(){
        long avlTime, vebTime;
        Integer avlMin;
        int vebMin;

        avlTime = System.nanoTime();//before avl min
        avlMin = avlTree.getMin();
        avlTime = System.nanoTime() - avlTime;

        vebTime = System.nanoTime();
        vebMin = VEB.minimum(vebTree);//before veb min
        vebTime = System.nanoTime()- vebTime;

        //display results
        //display error message if results aren't the same
        if(vebMin == -1 && avlMin != null)
            System.out.println("Error: VEB & AVL " +
                    "returned different values for min");
        else if(avlMin == null && vebMin != -1)
            System.out.println("Error: VEB & AVL " +
                    "returned different values for min");
        else if(avlMin.intValue() != vebMin)
            System.out.println("Error: VEB & AVL " +
                    "returned different values for min");
        else{
            System.out.print("\n");
            setw("Min:\t", 15);//left justify
            setw(Long.toString(avlTime), 10);
            setw(Long.toString(vebTime), 10);
            System.out.print("\n");

        }
    }
    /*nextArray()
     * reads an array of 10 integers from the delFile & returns it.
     * This supplies the values that are to be deleted or looked up
     */
    private int[] nextArray()throws IOException {
        int[] array = new int[DEL_SIZE];

        for(int i = 0; i<DEL_SIZE; i++)
           array[i] = Integer.parseInt(delFile.readLine());
        return array;
    }
    /* setw()
     * simulates the setw insertion operator of c++. # of spaces, right/left
     * justifying & different padding characters are supported
     */
    private static void setw(String s, int space, boolean right, String padding){
        int numSpaces = space - s.length();
        if(numSpaces <=0)//space isn't large enough
            System.out.print(s);
        else{
            if(right){//right justify
                for(int i = 0; i<numSpaces; i++)
                    System.out.print(padding);
                System.out.print(s);
            }
            else{//left justify
                System.out.print(s);
                for(int i =0; i< numSpaces; i++)
                    System.out.print(padding);
            }
        }
    }
    /* setw() overloaded 
     *  for default padding using " " & left justify
     */
    private static void setw(String s, int space){
        setw(s, space, false, " ");
    }

}

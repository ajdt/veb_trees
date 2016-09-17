/******************************************************
 *Program:
 *      AVLTest.java
 *Purpose:
 *      Tester class used to check that AVLTree's methods
 *      work correctly. Displays a command prompt-like
 *      environment that accepts commands which work on
 *      an AVLTree object.
 *Programmer:
 *       Armando Diaz T.
 *Last Modified:
 *          March 20, 2010
 ******************************************************/
import java.io.*;
import java.util.StringTokenizer;

public class AVLTest {
    //handles user input & calls interpet() to act on input
    public static void main(String args[]) throws IOException {
        System.out.println("Hi there");
        AVLTree tree = new AVLTree();
        BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));

        String input;
        do{
            System.out.println("next command >");
            input = cin.readLine();
            interpret(input, tree);

        }
        while(!input.equals("quit"));
    }
    /*interpret()
     * interprets a given command, commands with arguments
     * are interpreted using a StringTokenizer.
     *
     * NOTE: if an input argument is incorrectly formatted,
     * (e.g. an integer with non numeric chars is entered)
     *  an exception is thrown
     */
    public static void interpret(String comm, AVLTree tree){

        StringTokenizer tokens = new StringTokenizer(comm);
        String tk = tokens.nextToken();
        boolean success = true;//for boolean return values
        Integer result = null;
        //commands w/out args
        if(comm.equals("max"))
            printInt(tree.getMax());
        if(comm.equals("min"))
            printInt(tree.getMin());
        if(comm.equals("print"))
            tree.printInOrder();

        //commands w/ args
        if(tk.equals("delete"))
            success = tree.delete(Integer.parseInt(tokens.nextToken()));
        if(tk.equals("insert"))
            success = tree.insert(Integer.parseInt(tokens.nextToken()));
        else {
            if(tk.equals("succ"))
                result = tree.successor(Integer.parseInt(tokens.nextToken()));
            if(tk.equals("pred"))
                result = tree.predecessor(Integer.parseInt(tokens.nextToken()));
            if(result != null)
                printInt(result.intValue());
            else
                success = false;
        }

        if(!success)
            System.out.println("Error operation could not be performed.");


    }
    /*printInt()
     *  simple method to print the result of a command executing
     */
    private static void printInt(Integer val){
        if(val == null)
            System.out.println("Error tree is empty");
        else
            System.out.println(val);
    }
}

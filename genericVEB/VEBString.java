
/******************************************************
 *Program:
 *      VEBString.java
 *Purpose:
 *      A wrapper for a java defined String object. The
 *      VEBKey interface is implemented, so that
 *      VEBStrings each have a unique key that can be used
 *      to locate them in a VEB tree
 *Programmer:
 *       Armando Diaz Tolentino
 *Last Modified:
 *      March 7, 2010
 ******************************************************/

package genericveb;
import java.io.*;
import java.util.StringTokenizer;

public class VEBString implements VEBKey {
    private String data;//actual data memeber,cannot be changed
    int keyVal;// store's object's key

    //only one constructor is available, create from
    // pre-existing string
    public VEBString(String s){
        data = s;
        keyVal = calcKey();

    }
    public int getKey() {        
        return keyVal;
    }
    public String toString(){
        return data;
    }
    private int calcKey(){
        int key = 0;
        int stringSize = data.length();
        for(int i =0;i<stringSize; i++)//FIX THIS!!!!!!!!!!!!!!!!
            key+=Math.pow(data.charAt(i), stringSize-i-1);
        return key;
    }

    public static void main(String args[])throws IOException {
        BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
        String input;
        int size;
        //initial greeting & size prompt

        System.out.println("Welcome, please enter a power of 2, for the VEB structure's"
                +" universe size.");
        input = cin.readLine();
        size = Integer.parseInt(input);        
        VEBgen tree = new VEBgen(size);
        printMenu();//display the menu of options...
        while(!input.equals("quit")){
            System.out.println("next command >");
            input = cin.readLine();
            interpret(input, tree);
        }



    }
    // UI METHODS-------------------------------------------------------
    private static void printMenu(){
        System.out.println("Type one of the following commands:");
        System.out.println("-----------------------------------");
        System.out.println("1. insert <string>");
        System.out.println("2. del <string>");
        System.out.println("3.  printMax");
        System.out.println("4.  printMin");
        System.out.println("5.  pred <string>");
        System.out.println("6.  succ <string>");
       
    }
    private static void interpret(String comm, VEBgen tree){
        if(comm.equals("quit"))
            return;
        else if(comm.equals("printMax"))
            System.out.println(tree.maximum());
        else if(comm.equals("printMin"))
            System.out.println(tree.minimum());
        else
            argInterpret(comm, tree);
    }
    private static void argInterpret(String input, VEBgen tree){
       StringTokenizer tokens = new StringTokenizer(input);
       if(tokens.countTokens() < 2){
           System.out.println("Error file may be formatted incorrectly");
           return;
       }
       String  comm = tokens.nextToken();
       String arg = tokens.nextToken();

       if(comm.equals("insert"))
           tree.insert(new VEBString(arg));
       if(comm.equals("del"))
           tree.delete(new VEBString(arg));
       if(comm.equals("pred"))
           tree.predecessor(new VEBString(arg));
       if(comm.equals("succ"))
           tree.successor(new VEBString(arg));
    }
}

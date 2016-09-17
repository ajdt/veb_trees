
/******************************************************
 *Program:
 *      VEBTest.java
 *Purpose:
 *
 *Programmer:
 *       Armando Diaz T.
 *Last Modified:
 *      March 20, 2010
 ******************************************************/
package veb_proj;
import java.io.*;
import java.util.StringTokenizer;
public class VEBTest {
    public static void main(String args[])throws IOException {
        BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
        String input;
        VEB tree;
        System.out.println("Welcome, what size tree would you like to work with?");
        input = cin.readLine();
        tree = new VEB(Integer.parseInt(input));
        int size = Integer.parseInt(input);
        do {


            System.out.println("next Command >");
            input = cin.readLine();
            interpret(input, tree);
        }
        while(!input.equals("quit"));
   }
        
   public static void interpret(String comm, VEB tree){
       StringTokenizer tokens = new StringTokenizer(comm);
       String tk = tokens.nextToken();
       if(comm.equals("max"))
           printInt(VEB.maximum(tree));
       if(comm.equals("min"))
           printInt(VEB.minimum(tree));
       if(tk.equals("delete"))
            VEB.del(tree, Integer.parseInt(tokens.nextToken()));
       if(tk.equals("insert"))
           VEB.insert(tree, Integer.parseInt(tokens.nextToken()));
       if(tk.equals("member")){
           boolean exists = VEB.memberOf(tree,Integer.parseInt(tokens.nextToken()));
           if(exists)
               System.out.println("Item is a member");
           else
               System.out.println("Item is NOT in tree");
       }
       else {          
           if(tk.equals("pred"))
               printInt(VEB.predecessor(tree, Integer.parseInt(tokens.nextToken())));
           if(tk.equals("succ"))
               printInt(VEB.successor(tree, Integer.parseInt(tokens.nextToken())));          
       } 
           
    }
    private static void printInt(int val){
        if(val <= -1)//negative ints are invalid
            System.out.println("Error tree is empty");
        else
            System.out.println(val);
    }

}

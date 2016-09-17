/*
	poduces a text file w/ random ints
*/
import java.io.*;
import java.util.Random;

public class RandInts {

	
	
	public static void main(String args[]) throws IOException {
		BufferedWriter file = new BufferedWriter(new FileWriter("numFile.txt"));
		
		//int size = 500000; //four million is the upper bound
		int size = 4000000;
		size++;// so that upperbound is returned too
		
		//int numInts = 300000;// number of random ints to produce
		int numInts = 1000000;
		Random rand = new Random(System.currentTimeMillis());//time in milliseconds
		fileWriting(file, rand, 0, size, numInts);
		file.close();
		
		
	}
	

	
	
	private static void fileWriting(BufferedWriter file, Random rand,
			int min, int size, int numInts) throws IOException {
		
		if(numInts <= 0)
			return;
		else if(min == size){
			file.write(Integer.toString(min));//this will still be random
			file.newLine();
			numInts--;
			return ;
		
		}
		int num = rand.nextInt(size-min)+min;
		file.write(Integer.toString(num));
		file.newLine();
		numInts--;
		//recursively add other integers to file...
		double splitter = ((double)(num-min))/(size-min);
		int lowSize = (int)Math.floor(splitter*numInts);
		fileWriting(file, rand, min, num, lowSize);// for ranges 0 to min
		fileWriting(file, rand, num +1, size, (numInts - lowSize));// for min to size
	}
}
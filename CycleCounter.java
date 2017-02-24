/*
 * Author: Sarah Ruhl
 * 
 * This program Takes a file containing a number of vertex followed by a list of 
 * edges that form cycles or are branches on to the cycles and counts the number of
 * cycles, the number of vertexes in the cycle and branches attached to the cycle 
 * and the number of edges in the cycle. 
 */
import java.util.*;
import java.io.*;
public class CycleCounter {
	private long weight;// Stores current weight as recursion happens
	private long[] cycleCollections;// Stores all the weight at their cycle number
	private int[] length;// Stores all the weights at their cycle number
	private Node[] nodes;// Stores all the vertexes
	private int cyclecounter;// Stores current count of cycles
	static int size;// Stores number of vertexes
	
	// This object is the vertex used in this program
	final static class Node {
		int cycleNumber;// Stores what cycle this node is currently in(if -1 than it
		// has not been connected to a cycle yet)
		Node outgoing;// Stores the vertex this vertex has a directed edge towards
		int name;// Stores the number that this vertex is.
		boolean notExplored;// Store if this vertex has been explored yet
	 
		// Post: constructs the vertex
		public Node(int name) {
			cycleNumber = -1;// Initializes cycle number to -1
			this.name = name;
			notExplored = true;// Sets to not explored		 
		}
	}
 	// Pre: Takes a scanner of the file(in the proper format) containing the list
	// of vertexes and edges
	// Post: Constructs the graph from the list.
	public CycleCounter(Scanner input){ 
		size = input.nextInt();
		cycleCollections = new long[size/2 +1];
		// There cannot be a number of cycles greater than size/2 because each 
		// cycle is, at mininum, contains 2 vertexes
		length = new int[size/2+1];
		nodes = new Node[size+1];
		cyclecounter = 1; // Initializes first cycle
		for(int i = 1; i<=size; i++) {
			int n1 = input.nextInt();
			int n2 = input.nextInt();
			if(nodes[n1] == null) {// checks to see if that vertex has already been
			// created
				nodes[n1] = new Node(n1);
			}
			if(nodes[n2] == null) {// checks to see if that vertex has already been
			// created
				nodes[n2] = new Node(n2);
			}
			nodes[n1].outgoing = nodes[n2];// Sets the edge
		}
	 }
	 // Pre: Take in a vertex
	 // Post: Performs a DFS and from that determine the weight and length of the cycle
	 // the vertex is in
	 public void DFS(Node v) {
		 v.cycleNumber = cyclecounter;// Set's the vertex is cycle number
		 Node x = v.outgoing;
		 weight++;
		 if(x.cycleNumber == -1) {// x is unexplored
			 DFS(x);
			 if(v.cycleNumber!=-1){// checks if v is a branch to x as if so it's edge is
			 // not added to the length
				 length[v.cycleNumber]++;
			 }
		 } else {		 
			 if(v.notExplored && !x.notExplored) {// Checking for branches
				 v.cycleNumber = x.cycleNumber; // adds v to x's cycle
				 cycleCollections[x.cycleNumber] = cycleCollections[x.cycleNumber] + weight;
				 // updates weight
			 } else {
				 cycleCollections[v.cycleNumber] = weight;// update weight
				 cyclecounter++;// changes to a new cycle
				 length[v.cycleNumber]++;// update length for the last part of the cycle
			 }		 		 
		 }
		 v.notExplored = false; // marks as explore
	 }
	 // Pre: Should be done after DFS is performed on all unexplored vertexes to provide
	 // an accurate result
	 // Post: Prints out the weights and lengths for each cycle
	 public void printLengthsAndWieghts(){
		 int count = 1;// Initializing the index
		 while(cycleCollections[count] != 0){ // As cyclecounter increases by 1 
			 // each time all the cycles are stored at the beginning and once
			 // a 0 is hit, all the follow numbers in the array are 0, indicating
			 // there are no more cycles	 
			 // This next line was broken up to maintain a character count under
			 // 80 character, as that was nailed into me as proper form.
			 System.out.print("The cycle number " + count + " has a weight of "
					 + cycleCollections[count]+" and length of "+length[count]);
			 System.out.println();
			 count++;
		 }
	}	
	public static void main(String[]args) {
		 Scanner name = new Scanner(System.in);
		 System.out.println("Input File Name:");
		 String file = name.nextLine();//Gets inputed file name
		 try {
			Scanner input = new Scanner(new FileInputStream(file));// Creates scanner
			// for the file
		    CycleCounter read = new CycleCounter(input);
		    for(int i = 1; i <= read.size; i++) {// Goes through each vertex	
			 	if(read.nodes[i].notExplored == true) {// If it hasn't been explored
					read.DFS(read.nodes[i]);// It explores
					read.weight = 0;// And after finishing that cycle, sets the weight to 0
			 	}
		    }
		    read.printLengthsAndWieghts();
		 } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
	 }
	}
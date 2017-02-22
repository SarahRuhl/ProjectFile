import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;

/**
 * 
 */

/**
 * @author Ruhl, Sarah
 * Section: BC
 * 
 * Solution to Assignment 5 in CSE 373, Autumn 2016
 * University of Washington.
 * 
 * (Based on starter code v1.3. By Steve Tanimoto.)
 *
 * Java version 8 or higher is recommended.
 *
 */

// Here is the main application class:
public class ExploredGraph {
	Set<Vertex> Ve; // collection of explored vertices
	Set<Edge> Ee;   // collection of explored edges
	
	public ExploredGraph() {
		Ve = new LinkedHashSet<Vertex>();
		Ee = new LinkedHashSet<Edge>();	
	}

	// Post: Clears all the graph so that a new one can be explored
	public void initialize() {
		Ve.clear();// Clears any vertex
		Ee.clear();// Clears any edges
	}
	
	// Pre: Takes a vertex as a parameter
	// Post: Clears the vertex then adds a starting vertex.
	public void initialize(Vertex v){
		initialize();// Clears the fields
		Ve.add(v);// adds start vector
	}
	
	// Post: Gets number of edges
	public int nvertices() {return Ve.size();} //Get number of vertexes
	
	// Post: Gets number of edges
	public int nedges() {return Ee.size();}// returns size of field Ee

	// Pre: Take a starting vertex and a goal vertex as parameter, in that order.
	// Performs an iterative depth-first search between two vertexes
	public void idfs(Vertex vi, Vertex vj) {	
		initialize();// clears away any previous searches done
		int count = 0;// Intializes the number of times that have been iterated.
		ArrayList<Vertex> open = new ArrayList<Vertex>();// Creates a list of vertexes to be explore or tbe
		ArrayList<Vertex> closed = new ArrayList<Vertex>();// Creates a list of vertexes explored
		open.add(vi);// Adds start vertex to the list to be explored	
		while(!open.isEmpty()){	// Goes through the exploration until the tbe list is empty	
			Vertex v = open.remove(0);// Gets the front of the tbe list
			v.label = count;// changes the label in the vertex to keep track at what iteration it was explored
			count +=1;// changes number of iterations
			ArrayList<Vertex> success = successors(v);// calls a helper method to get a list of successors.
			ArrayList<Vertex> temp = new ArrayList<Vertex>();// creates a temporary list to hold the successors 
			//that pass the following tests
			int num = success.size();//gets the number of successors
			for(int i =0; i<num;i++){//This checks to see if vertex already exist
				success.get(i).pred = v;// sets the successor's predisessor to the vertex currently being explored
				if(success.get(i).toString().equals(vj.toString())){//Checks if we've made it to goal vertex		
					temp.add(success.get(i));//adds last vertex to temp
					Edge tempEdge = new Edge(v,success.get(i));//creates edge between this and the next vertex
					Ee.add(tempEdge);//Adds edge to Ee
				}
				if(!check(open, success.get(i).toString()) && !check(closed, success.get(i).toString())){
					// This checks if this successor has been or is waiting to be explored
					temp.add(success.get(i));// adds vertex to temp
					Edge tempEdge = new Edge(v,success.get(i));
					// creates edge between the current and next vertex
					Ee.add(tempEdge);//adds to Ee
				}
			}
			open = shift(open, temp);//adds them to front
			closed.add(v);//adds current vertex to explored list
			Ve.add(v);// ads v to Ve
		}			
		Ve.add(vj);// Adds goal to Ve	
	}
	// Pre: Takes two list of variables.
	// Post: This is a helper method of idfs. It adds the successor to front.
	private ArrayList<Vertex> shift(ArrayList<Vertex> list, ArrayList<Vertex> success) {
		for(int i = 0; i< list.size(); i++){//iterates the list
			success.add(list.get(i));//adds elements of list to the other
		}
		return success;// returns combined list
	}
	// Pre: Takes a start and stop vertex as parameters
	// Post: Performs a Breadth first search
	public void bfs(Vertex vi, Vertex vj) {
		initialize();// clears any previous work
		int count = 0;// initializes number of loops this has gone through.
		ArrayList<Vertex> open = new ArrayList<Vertex>(); //Creates a tbe list
		open.add(vi);//Adds start vertex to tbe list
		ArrayList<Vertex> closed = new ArrayList<Vertex>();// creates explored list
		Vertex w = null;//Initailize the temp variable used to keep track of 
		while(!open.isEmpty()){// explores everything in tbe list
			Vertex v = open.get(0);//gets object tbe ouut of tbe list
			open.remove(0);// removes vertex from list
			v.label = count;// labels at what point it was explore
			count +=1;//adds 1 to count
			ArrayList<Vertex> success = successors(v);//finds successors
			for(int i = 0; i< success.size(); i++){// checks each successor
				success.get(i).pred = v;// set current vector as the next's predisesor
				if (!check(open,success.get(i).toString()) && !check(closed,success.get(i).toString())) {
					// check if successor is contained in either list
					open.add(success.get(i));//adds tbe list					
					Edge tempEdge = new Edge(v,success.get(i));//create edge
					Ee.add(tempEdge);// adds edge to Ee
				} 
			}
			closed.add(v);// adds current vertex to closed
			Ve.add(v);	// adds current vertex to Ve
		}
		Ve.add(vj);//Adds final vector to Ve		
	}
	// Pre: Takes an ArrayList of vertexes and a string form of a vertex
	// Post: Returns true or false depending of if the vertex is already in the list
	private boolean check (ArrayList<Vertex> list, String comp) {
		for(int i=0;i<list.size();i++){// goes through list
			if(comp.equals(list.get(i).toString())) {//check if match
				return true;// if they do returns true
			}
		}
		return false;// Has gone through list it is not there
	}
	// Pre: Takes the vertex in question
	// Post: Returns an ArrayList of successors
	private ArrayList<Vertex> successors(Vertex v){
		ArrayList<Vertex> success = new ArrayList<Vertex>();//Creates list of successors
		for(int i = 1; i <=3; i++){//determines one of the numbers in the operator
			for(int j = 1; j<= 3; j++) {// determines the other number
				Operator test = new Operator(i,j);//creates an operator for two pegs
				Vertex next = test.transition(v);//performs the operation
				if(next != null) {//checks that it is a legal move in the game
					success.add(next);//adds it to list
				}	
			}
		}	
		return success;//returns the successors
	}
	// Pre: Takes a final vertex
	// Post: Returns an ArrayList that shows the la path
	public ArrayList<Vertex> retrievePath(Vertex vi) {
		Vertex v = null;//Initializes a storage vertex	
		Vertex begin = new Vertex("[[4,3,2,1],[],[]]");	// Creates initial vertex	
		for(Vertex vt: Ve){//Goes through Ve and finds the right vertex
			if(vt.toString().equals(vi.toString())){
				v = vt;
			}
		}
		ArrayList<Vertex> path = helper(begin,v);//Finds path
		// This next section is removing the repeated path I get from performing this on idfs
		// It goes through and checks that their isn't a dublicate path in the ArrayList
		ArrayList<Vertex> temp = new ArrayList<Vertex>();
		int length = path.size();
		for(int i =0 ; i < length; i++){
			if(!temp.contains(path.get(i))){
				temp.add(path.get(i));
			}
		}
		return temp;// Returns the path
	}
	// Pre: Takes the two vertexes and returns the path
	// Post: Finds the shortest path between to vectors
	public ArrayList<Vertex> shortestPath(Vertex vi, Vertex vj) {
		bfs(vi,vj);//Uses BFS to get shortest path
		return helper(vi,vj);// returns the path
	}	
	// Pre: Takes the two vectors
	// Post: Returns the last explored path between to vector
	private ArrayList<Vertex> helper(Vertex vi, Vertex vj){
		ArrayList<Vertex> path = new ArrayList<Vertex>();//holds path
		if(vi.toString().equals(vj.toString())) {//Checks if the two vertexes are the same
			path.add(vj);//add the vertexes
		} else {
			for(Vertex v: Ve){//checks the Ve for the final vector
				if(vj.toString().equals(v.toString())&& v.pred!=null){// checks if it matches and that
				// the vectors predessesor isn't null
					ArrayList<Vertex> temp = helper(vi,v.pred);//makes a recusive call and stores it
					path.addAll(temp);//Adds past calls to the path
					path.add(v);// adds current to the path
				}
			}
		}
		return path;// returns the path
	}
	// Post: Returns the set of vertexes that have been created
	public Set<Vertex> getVertices() {return Ve;} 
	// Post: Returns the set of edges that have been created
	public Set<Edge> getEdges() {return Ee;} 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExploredGraph eg = new ExploredGraph();
		eg.initialize(); 
		Vertex v0 = eg.new Vertex("[[4,3,2,1],[],[]]");
		Vertex vf = eg.new Vertex("[[],[],[4,3,2,1]]");
		eg.bfs(v0, vf);
		System.out.println("BFS Path: "+eg.retrievePath(vf).toString());
		System.out.println("Shortest Path: "+eg.shortestPath(v0, vf).toString());
		eg.idfs(v0, vf);
		System.out.println("IDFS Path: "+eg.retrievePath(vf).toString());
	}
	
	// Creates a vertex from a string representing the pegs and rings.
	class Vertex {
		ArrayList<Stack<Integer>> pegs; // Each vertex will hold a Towers-of-Hanoi state.
		// There will be 3 pegs in the standard version, but more if you do extra credit option A5E1.
		Vertex pred;
		int label;
		// Pre: takes a string such as "[[4,3,2,1],[],[]]"
		// Post: Constructs a vertex
		public Vertex(String vString) {
			pred = null;
			label = 0;
			String[] parts = vString.split("\\],\\[");
			pegs = new ArrayList<Stack<Integer>>(3);
			for (int i=0; i<3;i++) {
				pegs.add(new Stack<Integer>());
				try {
					parts[i]=parts[i].replaceAll("\\[","");
					parts[i]=parts[i].replaceAll("\\]","");
					List<String> al = new ArrayList<String>(Arrays.asList(parts[i].split(",")));
					//System.out.println("ArrayList al is: "+al);
					Iterator<String> it = al.iterator();
					while (it.hasNext()) {
						String item = it.next();
                        if (!item.equals("")) {
                               // System.out.println("item is: "+item);
                                pegs.get(i).push(Integer.parseInt(item));
                        }
					}
				}
				catch(NumberFormatException nfe) { nfe.printStackTrace(); }
			}		
		}
		public String toString() {
			String ans = "[";
			for (int i=0; i<3; i++) {
			    ans += pegs.get(i).toString().replace(" ", "");
				if (i<2) { ans += ","; }
			}
			ans += "]";
			return ans;
		}
	}
	
	// Post: reates an edge
	class Edge {
		private Vertex edge1;
		private Vertex edge2;
		
		// Pre: Takes to vertexes
		// Post: Constructs an Edge
		public Edge(Vertex vi, Vertex vj) {
			edge1 = vi;
			edge2 = vj;
		}		
		// Post: Gets one vertex
		public Vertex getEndpoint1(){
			return edge1;
		}		
		// Post: Gets the other Vertex
		public Vertex getEndpoint2() {
			return edge2;
		}
		// Post: Gives a string that explaining what the edge connects to
		public String toString(){
			return "Edge from "+edge1.toString()+" to "+ edge2.toString();
		}
	}
	
	
	// An operator creates one state of the puzzle from another.
	class Operator {
		private int i, j;		
		// Post: Creates an operator
		public Operator(int i, int j) { // Constructor for operators.
			this.i = i-1;
			this.j = j-1;
		}
		//Pre: Takes vertex 
		// Post: Determines if the vertex meets the requirements to have an operation done
		public boolean precondition(Vertex v) {
			// TODO: Add code that will determine whether or not this 
			// operator is applicable to this vertex.
			if(!v.pegs.get(i).isEmpty() ){
				if(!v.pegs.get(j).isEmpty()){
					int firsttop = v.pegs.get(i).peek();
					int secondtop = v.pegs.get(j).peek();
					if(firsttop < secondtop){
						return true;
					}
				} else if (v.pegs.get(j).isEmpty()) {
					return true;
				}
			}
			return false; // Placeholder.  Change this.
		}
		// Pre: Takes a vertex 
		// Post: Transfers the ring to a different peg
		public Vertex transition(Vertex v) {
			// TODO: Add code to return a "successor" of v, according
			if(precondition(v)){
				Vertex newState = new Vertex(v.toString());
				int ring = newState.pegs.get(i).pop();
				newState.pegs.get(j).push(ring);
				//System.out.println(newState.toString());
				return newState;
			}
			return null; // Placeholder.  change this.
		}		
		// Post: Explains what operator is doing
		public String toString() {
			// TODO: Add code to return a string good enough
			// to distinguish different operators
			return "This operation wants to transfer the top ring from peg: "+1+" to peg: "+j+".";
		}
	}

}
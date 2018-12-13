/*
 * 	Prateek Sarna - pxs180012
 * 	Bharath Rudra - bxr180008
 */

/** Starter code for SP8
 *  @author 
 */

// change to your netid
package rbk;

import rbk.Graph.Vertex;
import rbk.Graph.Edge;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Factory;
import rbk.Graph.Timer;

import java.io.File;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;

public class DFS extends GraphAlgorithm<DFS.DFSVertex> {
	private LinkedList<Graph.Vertex> finalList; // List to store the vertices visited
	private int topNum;
	private boolean isCyclic; // boolean variable to check if a particular vertex has cycles
	private int conCom; // variable for storing the number of connected components

	public static class DFSVertex implements Factory {
		int cno;
		boolean seen; // keeps track of vertices whether they are seen or not
		Vertex parent;
		boolean parentSeen; // boolean variable to check the cycles in the graph

		public DFSVertex(Vertex u) {
			this.cno = 0;
		}

		public DFSVertex make(Vertex u) {
			return new DFSVertex(u);
		}
	}

	/**
	 * Constructor
	 * 
	 * @param g
	 */
	public DFS(Graph g) {
		super(g, new DFSVertex(null));
	}

	/**
	 * Static function to run depthFirstSearch.
	 * 
	 * @param g
	 *            of type Graph
	 * @return DFS
	 */
	public static DFS depthFirstSearch(Graph g) {
		DFS d = new DFS(g);
		d.dfs(g);
		return d;
	}

	public void dfs(Iterable<Vertex> iterable) {

		// intialize the values of variables
		finalList = new LinkedList<Vertex>();
		topNum = g.size();
		isCyclic = false;

		for (Vertex u : iterable) {
			get(u).seen = false;
			get(u).parent = null;
			get(u).parentSeen = false;
		}
		conCom = 0; // initializing the number of connected components to 0 every time dfs is run on
					// a graph

		// visiting each vertex of the graph if not seen
		for (Vertex u : iterable) {
			if (!get(u).seen) {
				++conCom;
				dfsVisit(u);
			}
		}
	}

	/*
	 * Helper function traverses in depth to each adj vertex. Updates the cycle
	 * property of the class
	 */
	private void dfsVisit(Vertex u) {

		get(u).seen = true;
		get(u).parentSeen = true;
		get(u).cno = conCom;

		// checking if the adjacent vertices of u are visited
		for (Edge e : g.incident(u)) {
			Vertex v = e.otherEnd(u);

			if (!isCyclic) {
				// if parentseen is true the graph has cycles
				if (get(v).parentSeen)
					isCyclic = true;
			}

			if (!get(v).seen) {
				get(v).parent = u;
				dfsVisit(v);
			}
		}

		finalList.addFirst(u); // add each vertex visited to the starting of the list to
								// get topological ordering of the list
		get(u).parentSeen = false; // making parentSeen of every vertex as false after checking for cycles
	}

	public List<Vertex> topologicalOrder1() {
		// check if graph is directed, if not return null
		if (!g.isDirected())
			return null;
		this.dfs(g);

		// checks if there is a cycle in the graph and returns null, else returns the
		// list
		return isCyclic ? null : finalList;
	}

	// Find topological oder of a DAG using DFS. Returns null if g is not a DAG.
	public static List<Vertex> topologicalOrder1(Graph g) {
		DFS d = new DFS(g);
		return d.topologicalOrder1();
	}

	public static DFS stronglyConnectedComponents(Graph g) {
		DFS d = new DFS(g);
		d.dfs(g);
		List<Vertex> list = d.finalList;
		g.reverseGraph();
		d.dfs(list);
		g.reverseGraph();
		return d;
	}

	// Find the number of connected components of the graph g by running dfs.
	// Enter the component number of each vertex u in u.cno.
	// Note that the graph g is available as a class field via GraphAlgorithm.
	public int connectedComponents() {
		dfs(g);
		return conCom;
	}

	// After running the connected components algorithm, the component no of each
	// vertex can be queried.
	public int cno(Vertex u) {
		return get(u).cno;
	}

	public static void main(String[] args) throws Exception {
		String string = "11 16  1 11 1  11 4 1  4 9 1  9 11 1  4 1 1  11 3 1  3 10 1  10 6 1  6 3 1  2 3 1  2 7 1  7 8 1  8 2 1  5 4 1  5 8 1  5 7 1";
		// String string = "8 9 1 2 1 2 3 1 3 1 1 2 4 1 4 5 1 5 6 1 6 7 1 7 4 1 7 8 1";
		// String string = "7 8 1 2 2 1 3 3 2 4 5 3 4 4 4 5 1 5 1 7 6 7 1 7 6 1 0";
		// String string = "5 4 1 2 1 2 5 1 5 4 1 3 5 1 0";
		// String string = "9 8 1 3 1 3 7 1 3 9 1 1 9 1 9 7 1 7 4 1 2 6 1 6 8 1 0";
		// String string = "10 12 1 3 1 3 2 1 2 4 1 4 7 1 1 8 1 8 5 1 8 2 1 5 4 1 6 8 1
		// 6 10 1 5 10 1 10 9 1 0";

		Scanner in;
		// If there is a command line argument, use it as file from which
		// input is read, otherwise use input from string.
		in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);

		// Read graph from input
		Graph g = Graph.readGraph(in, true);
		g.printGraph(false);

		DFS d = stronglyConnectedComponents(g);
		System.out.println("Number of components: " + d.conCom + "\nu\tconnectedComponents");
		for (Vertex u : g) {
			System.out.println(u + "\t" + d.cno(u));
		}

	}
}
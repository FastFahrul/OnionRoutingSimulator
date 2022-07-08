package functions;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.math.*;

public class Network {

	Map<Integer,Node> lookup;
	
	public Network() {
		// CONSTRUCTOR
		
		// TODO
		
		lookup = new TreeMap<Integer,Node>();
	}
	
	public Vector<Vector<Integer> > readNodesFromFile(String fInName) throws IOException {
		// Reads list of node definitions from a file, four ints per line 
		BufferedReader fIn = new BufferedReader(
							 new FileReader(fInName));
		String s;
		Vector<Vector<Integer> > nodeDefs = new Vector<Vector<Integer> >();
		
		while ((s = fIn.readLine()) != null) {
			Vector<Integer> nodeDef = new Vector<Integer>();
			java.util.StringTokenizer line = new java.util.StringTokenizer(s);
			while (line.hasMoreTokens()) {
				nodeDef.add(Integer.parseInt(line.nextToken()));
			}
			nodeDefs.add(nodeDef);
		}
		fIn.close();
		
		return nodeDefs;
	}

	public Vector<String> readMessagesFromFile(String fInName) throws IOException {
		// Reads list of node definitions from a file, four ints per line 
		BufferedReader fIn = new BufferedReader(
							 new FileReader(fInName));
		String s;
		Vector<String> msgs = new Vector<String>();
		
		while ((s = fIn.readLine()) != null) {
			msgs.add(s);
		}
		fIn.close();
		
		return msgs;
	}

	public void printNetwork() {
		for (Integer i : lookup.keySet()) {
			System.out.println("ID: " + lookup.get(i).getID()
					+ " | e : " + lookup.get(i).getE()
					+ " | k : " + lookup.get(i).getK());
		}
	}

	public static void main(String[] args) {

		String nodeFileInName = "/Users/fahrul/Documents/MacquarieDocs/LearningMaterials/sample-inputs/nodedef1.in";

		Network net = new Network();

		
		try {
			Vector<Vector<Integer> > inputNodes = net.readNodesFromFile(nodeFileInName);
			for (Vector<Integer> v : inputNodes) {
				Node n = new Node(v.get(0), v.get(1), v.get(2), v.get(3), Boolean.FALSE, net.lookup);
				net.lookup.put(v.get(0), n);
			}
			System.out.println(net.lookup.get(3).firstRForInitiatingMessage(2, 6));
			net.lookup.get(3).initiateMessageAnon("hello", 4, 6);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

	}
}

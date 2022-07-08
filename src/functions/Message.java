package functions;

import java.util.Vector;

public class Message {

	private String msg;
	private Integer destNode;
	private Integer currentNode;
	private Vector<Integer> visitedNodes;
	
	public Message(String s, Integer d) {
		// PRE: s is the message, 
		//      n the current (message initiator) node,
		//      d is the destination node
		msg = s;
		currentNode = -1;
		destNode = d;
		visitedNodes = new Vector<Integer>();
	}
	
	public void arrivedAt(Integer n) {
		currentNode = n;
		visitedNodes.add(n);
	}
	
	public String getMessage() {
		if (destNode == currentNode)
			return msg;
		else return "";
	}
	
	public Boolean isDestination() {
		if (destNode == currentNode)
			return Boolean.TRUE;
		else
			return Boolean.FALSE;
	}
	
}

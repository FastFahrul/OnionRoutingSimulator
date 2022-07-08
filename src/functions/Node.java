package functions;

import java.util.*;
import java.io.IOException;
import java.math.*;

// VERSION 7/5/22

public class Node {

	// TODO: fields
	Integer n;
	Integer e;
	Integer d;
	Integer K;
	Boolean useBI;
	Map<Integer,Node>m;
	Message msg;
	Boolean isCorrupt = false;
	Vector<Integer> path1;
	Vector<Integer> path2;
	Integer previousNode;
	
	public Node(Integer n, Integer e, Integer d, Integer K, Boolean useBI, Map<Integer,Node> m) {
		// PRE: n is node ID,
		//      e is the exponent for the function f()
		//      d is the exponent for the function g()
		//      K is the divisor in f() and g()
		//      useBI is true if BigInteger should be used for NodeTransitionFunction, false otherwise
		//      m is a non-null map of node IDs to node objects
		
		// TODO
		
		this.n = n;
		this.e = e;
		this.d = d;
		this.K = K;
		this.useBI = false;
		this.m = m;
	}
	
	public Boolean isDestinationNode() {
		// PRE: Based on msg received
		// POST: Returns true if this is the destination node, false otherwise
		
		// TODO
		
		if(msg.isDestination()) {
			return true;
		}
		
		else {
		return false;
		}
	}

	public Message getMessage() {
		// PRE: -
		// POST: Returns the current received message, null if no received message
		
		// TODO
		
		if(msg == null) {
			return null;
		}
		
		return msg;
	}

	public void sendMsgToNodeByPath(Node n, Message msg, Vector<Integer> path) {
		// PRE: n is a non-null node that the message is being sent to,
		//      msg is a message,
		//      path is a list of valid node IDs that the message still has to traverse,
		//        after being transmitted to Node n
		// POST: invokes receiveMsgFromNodeByPath on node n
		
		// TODO
	
		msg.arrivedAt(n.n);
		n.msg = msg;
		Iterator<Integer> Itr = path.iterator();
		
		
		if(Itr.hasNext() && !n.isDestinationNode()) {
			Integer newNodeID = Itr.next();
			receiveMsgFromNodeByPath(msg,newNodeID,path);
		}
	}
	
	public void sendMsgToNodeAnon(Node n, Message msg, Integer r, NodeTransitionFunction ntf) {
		// PRE: n is a non-null node that the message is being sent to,
		//      msg is a message,
		//      r is the current value of r from the forward transition function,
		//      ntf is the forward transition function
		// POST: invokes receiveMsgFromNodeAnon on node n

		// TODO
		
		this.msg = msg;
		n.msg = msg;
		
		if(n.isCorrupt == true) {
			NodeTransitionFunction g = new NodeTransitionFunction(d,K);
			n.previousNode = g.f(r)%m.size();;
		}
		
		if(m.size()>2) {
			Integer nextNodeID = r%m.size();
			receiveMsgFromNodeAnon(msg,nextNodeID,r,ntf);
		}
		else {
			receiveMsgFromNodeAnon(msg,n.n,r,ntf);
		}
	}

	public void sendMsgToNodeAnon(Node n, Message msg, BigInteger r, NodeTransitionFunction ntf) {
		// PRE: n is a non-null node,
		//      msg is a message,
		//      r is the current value of r from the forward transition function.
		//      ntf is the forward transition function
		// POST: invokes receiveMsgFromNodeAnon on node n

		// TODO
		
		this.msg = msg;
		n.msg = msg;
		
		Integer sizeOf = m.size();
		
		BigInteger size = BigInteger.valueOf(sizeOf);
		
		if(n.isCorrupt == true) {
			NodeTransitionFunction g = new NodeTransitionFunction(d,K);
			n.previousNode = r.intValue()%m.size();
		}
		
		if(m.size()>2) {
			BigInteger nextNodeID = r.mod(size);
			Integer nextNodeIDInt = nextNodeID.intValue();
			receiveMsgFromNodeAnon(msg,nextNodeIDInt,r,ntf);
		}
		else {
			receiveMsgFromNodeAnon(msg,n.n,r,ntf);
		}	
	}
	
	public void receiveMsgFromNodeByPath(Message msg, Integer sendID, Vector<Integer> path) {
		// PRE: msg is a message, 
		//      sendID is the ID of the sending node,
		//      r is the current value of r from the forward transition function,
		//      ntf is the forward transition function
		// POST: If the path is empty, stop;
		//       otherwise, record in the message that the node has been visited and
		//                  send the message onwards.

		// TODO
		
		if(path.size()<=0) {
			return;
		}
		
		path.remove(0);
		Node newNode = m.get(sendID);
	
		sendMsgToNodeByPath(newNode,msg,path);
	}
	
	public void receiveMsgFromNodeAnon(Message msg, Integer sendID, Integer r, NodeTransitionFunction ntf) {
		// PRE: msg is a message, 
		//      sendID is the ID of the sending node,
		//      r is the current value of r from the forward transition function,
		//      ntf is the forward transition function
		// POST: If this is the destination node, stop;
		//       otherwise, record in the message that the node has been visited and
		//                  send the message onwards.

		// TODO
		
		msg.arrivedAt(sendID);
		
		Node nextNode = m.get(sendID);
		ntf = new NodeTransitionFunction(e,K);
		Integer nextR = ntf.f(r);
		
		nextNode.msg = msg;
		
		if(!nextNode.isDestinationNode()) {		
			sendMsgToNodeAnon(nextNode,msg,nextR,ntf);
		}
	}

	public void receiveMsgFromNodeAnon(Message msg, Integer sendID, BigInteger r, NodeTransitionFunction ntf) {
		// PRE: msg is a message, 
		//      id is the ID of the sending node,
		//      r is the current value of r from the forward transition function,
		//      ntf is the forward transition function
		// POST: If this is the destination node, stop;
		//       otherwise, record in the message that the node has been visited and
		//                  send the message onwards.

		// TODO
		
		msg.arrivedAt(sendID);
		
		Node nextNode = m.get(sendID);
		ntf = new NodeTransitionFunction(e,K);
		BigInteger nextR = ntf.f(r);
		
		nextNode.msg = msg;
		
		if(!nextNode.isDestinationNode()) {		
			sendMsgToNodeAnon(nextNode,msg,nextR,ntf);
		}
	}

	public NodeTransitionFunction createForwardNodeTransitionFunction() {
		// PRE: -
		// POST: Creates a NodeTransitionFunction using this node's public function f() with parameters e, K

		// TODO
		
		NodeTransitionFunction f = new NodeTransitionFunction(e,K);
		
		return f;
	}
	
	public Integer firstRForInitiatingMessage(Integer k, Integer v) {
		// PRE: v is destination node ID, k is number of steps
		// POST: Uses the trapdoor function inverse, applied to destination node v with number of steps k, to calculate the node path;
		//       returns value of r that determines first step on node path

		// TODO
		
		Integer rOfInitiator = 0;
		NodeTransitionFunction g = new NodeTransitionFunction(d,K);
		Integer temp = g.f(v);
		rOfInitiator = temp;
		
		int count = k-1;
		while(count>0) {
			 g = new NodeTransitionFunction(d,K);
			 rOfInitiator = temp;
			 temp = g.f(rOfInitiator);
			 count--;
		}

		return rOfInitiator;
	}

	public BigInteger firstRForInitiatingMessage(Integer k, BigInteger v) {
		// PRE: v is destination node ID, k is number of steps as a BigInteger
		// POST: Uses the trapdoor function inverse, applied to destination node v with number of steps k, to calculate the node path;
		//       returns value of r that determines first step on node path

		// TODO
		
		BigInteger rOfInitiator = new BigInteger("0");
		NodeTransitionFunction g = new NodeTransitionFunction(d,K);
		BigInteger temp = g.f(v);
		rOfInitiator = temp;
		
		int count = k-1;
		while(count>0) {
			 g = new NodeTransitionFunction(d,K);
			 rOfInitiator = temp;
			 temp = g.f(rOfInitiator);
			 count--;
			
		}

		return rOfInitiator;
	}
	
	public void initiateMessageAnon(String msgStr, Integer k, Integer v) {
		// PRE: msg is an original message, v is destination node ID, k is number of steps
		// POST: Adds destination ID to msg; 
		//       creates Message to send to next node, as determined by firstRForInitiatingMessage(k, v),
		//       along with forward transition function
		
		// TODO
		Message msg = new Message(msgStr,v);
		this.msg = msg;
		msg.arrivedAt(this.n);

		
		if(useBI == false) {
			NodeTransitionFunction ntf = new NodeTransitionFunction(e,K);
			Integer r = firstRForInitiatingMessage(k,v);
			Integer nextNodeID = r%m.size();
			Node nextNode = m.get(nextNodeID);
		
			sendMsgToNodeAnon(nextNode, msg, r, ntf);
		}
		
		else {
			NodeTransitionFunction ntf = new NodeTransitionFunction(e,K);
			BigInteger V = BigInteger.valueOf(v);
			BigInteger r = firstRForInitiatingMessage(k,V);
			
			Integer sizeOf = m.size();
			
			BigInteger size = BigInteger.valueOf(sizeOf);
			
			BigInteger nextNodeID = r.mod(size);
			Integer nextNodeIDInt = nextNodeID.intValue();
			Node nextNode = m.get(nextNodeIDInt);
			
			sendMsgToNodeAnon(nextNode,msg,r,ntf);
		}
	}
	
	public Integer getID() {
		// PRE: -
		// POST: Returns node ID

		// TODO

		return n;
	}
	
	public Integer getE() {
		// PRE: -
		// POST: Returns value of e in this node's function f()

		// TODO
		
		return e;
	}
	
	public Integer getK() {
		// PRE: -
		// POST: Returns value of K in this node's function f()

		// TODO
		
		return K;
	}
	
	// (HIGH) DISTINCTION: guess initiator
	
	public Integer guessInitiator() {
		// PRE: -
		// POST: Guesses a node to be the initiator if it can track back through corrupted nodes
		//       along two separate paths
		//      returns this node ID, or -1 if no guess

		// TODO
		
		return null;
	}

	public void setCorrupt() {
		// PRE: -
		// POST: Sets a node to be corrupt
		
		

		// TODO
		
		this.isCorrupt = true;
		Vector<Integer> path1 = new Vector<Integer>();
		Vector<Integer> path2 = new Vector<Integer>();
		
		this.path1 = path1;
		this.path2 = path2;
	}
	
	public Integer lastSender() {
		// PRE: -
		// POST: If a node is not corrupt, returns -1;
		//       if a node is corrupt, returns ID of node that last sent it a message,
		//       -1 if it has not been sent any messages

		// TODO
		
		if(this.isCorrupt == false) {
			return -1;
		}
		else {
			return this.previousNode;
		}
	}
	
	// DISTINCTION: visits
	
	public Vector<Integer> allVisitableNodes(Integer v) {
		// PRE: v is destination node ID
		// POST: Returns set of all possible nodes that can be visited on the way to v,
		//         using the trapdoor method, for all possible lengths of path
		
		// TODO
		
		int counter = 1;
		int size = m.size();
		Vector<Integer> vec = new Vector<Integer>(); 
		String msg = "Test";
		
		while(counter<=size) {
			initiateMessageAnon2(msg, counter, v, vec);
			counter++;
			}
		
		Collections.sort(vec);
		
		return vec;
	}
	
	public void initiateMessageAnon2(String msgStr, Integer k, Integer v, Vector<Integer> vec) {
		// PRE: msg is an original message, v is destination node ID, k is number of steps
		// POST: Adds destination ID to msg; 
		//       creates Message to send to next node, as determined by firstRForInitiatingMessage(k, v),
		//       along with forward transition function
		
		// TODO
		Message msg = new Message(msgStr,v);
		this.msg = msg;
		msg.arrivedAt(this.n);
		if(vec.size() == 0){
			vec.add(this.n);
		}

		
			NodeTransitionFunction ntf = new NodeTransitionFunction(e,K);
			Integer r = firstRForInitiatingMessage(k,v);
			Integer nextNodeID = r%m.size();
			Node nextNode = m.get(nextNodeID);
			
		
			sendMsgToNodeAnon2(nextNode, msg, r, ntf, vec);
	}
	
	public void sendMsgToNodeAnon2(Node n, Message msg, Integer r, NodeTransitionFunction ntf, Vector<Integer> vec) {
		// PRE: n is a non-null node that the message is being sent to,
		//      msg is a message,
		//      r is the current value of r from the forward transition function,
		//      ntf is the forward transition function
		// POST: invokes receiveMsgFromNodeAnon on node n

		// TODO
		
		this.msg = msg;
		
		if(m.size()>2) {
			Integer nextNodeID = r%m.size();
			receiveMsgFromNodeAnon2(msg,nextNodeID,r,ntf,vec);
		}
		else {
			receiveMsgFromNodeAnon2(msg,n.n,r,ntf, vec);
		}
	}
	
	public void receiveMsgFromNodeAnon2(Message msg, Integer sendID, Integer r, NodeTransitionFunction ntf, Vector<Integer> vec) {
		// PRE: msg is a message, 
		//      sendID is the ID of the sending node,
		//      r is the current value of r from the forward transition function,
		//      ntf is the forward transition function
		// POST: If this is the destination node, stop;
		//       otherwise, record in the message that the node has been visited and
		//                  send the message onwards.

		// TODO
		
		msg.arrivedAt(sendID);
		
		boolean toAdd = false;
		
		for(int i = 0; i<vec.size();i++) {
			if(vec.get(i) == sendID) {
				toAdd = true;
			}
		}
		
		if(toAdd == false) {
			vec.add(sendID);
		}
			
		Node nextNode = m.get(sendID);
		ntf = new NodeTransitionFunction(e,K);
		Integer nextR = ntf.f(r);
		
		nextNode.msg = msg;
		
		if(!nextNode.isDestinationNode()) {		
			sendMsgToNodeAnon2(nextNode,msg,nextR,ntf, vec);
		}
	}
	
	public static void main(String[] args) {
		Node n1 = new Node(1, 3, 7, 33, Boolean.FALSE,null);
		System.out.println(n1.firstRForInitiatingMessage(5, 6));
	}

}

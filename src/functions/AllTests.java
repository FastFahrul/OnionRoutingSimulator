package functions;

import static org.junit.Assert.*;
import java.lang.reflect.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Vector;
import java.util.Arrays;
import java.util.Queue;
import java.util.TreeSet;
import java.math.*;

import org.junit.Test;

public class AllTests {

	// Tests for supplied Message class
	
	@Test
	public void testMessageA() {
		Message m = new Message("Hello world", 5);
		m.arrivedAt(3);
		assertEquals(m.isDestination(), Boolean.FALSE);
	}
	
	@Test
	public void testMessageB() {
		Message m = new Message("Hello world", 5);
		m.arrivedAt(3);
		assertEquals(m.getMessage(), "");
	}
	
	@Test
	public void testMessageC() {
		Message m = new Message("Hello world", 5);
		m.arrivedAt(5);
		assertEquals(m.getMessage(), "Hello world");
	}
	
	@Test
	public void testMessageD() {
		Message m = new Message("Hello world", 5);
		m.arrivedAt(5);
		
		Field field = null;
		try {
			field = Message.class.getDeclaredField("msg");
			field.setAccessible(true);
			try {
				String fieldValue = (String) field.get(m);
				System.out.println(fieldValue);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} catch (NoSuchFieldException nsfe) {
		    throw new RuntimeException(nsfe);
		}
	}
	
	@Test
	public void testMessageE() {
		Message m = new Message("Hello world", 5);
		m.arrivedAt(1);
		m.arrivedAt(5);
		
		Field field = null;
		try {
			field = Message.class.getDeclaredField("visitedNodes");
			field.setAccessible(true);
			try {
				Vector<Integer> fieldValue = (Vector<Integer>) field.get(m);
				System.out.println(fieldValue);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} catch (NoSuchFieldException nsfe) {
		    throw new RuntimeException(nsfe);
		}
	}
	
	// PASS-LEVEL TESTS

	@Test
	public void testNodeTransitionFunction() {
		NodeTransitionFunction ntf = new NodeTransitionFunction(3, 33);
		assertEquals(Integer.valueOf(26), ntf.f(5));
	}
	
	@Test
	public void testIsDestinationNode() {
		Message m = new Message("Hello world", 1);
		m.arrivedAt(1);
		Node n1 = new Node(1, 3, 7, 33, Boolean.FALSE, null);
		n1.msg = m;
		
		assertTrue(n1.isDestinationNode());
	}
	
	@Test
	public void testGetE() {
		Node n = new Node(1, 3, 7, 33, Boolean.FALSE, null);
		assertEquals(Integer.valueOf(3), n.getE());
	}
	
	@Test
	public void testGetK() {
		Node n = new Node(1, 3, 7, 33, Boolean.FALSE, null);
		assertEquals(Integer.valueOf(33), n.getK());
	}
	
	@Test
	public void testNodeID() {
		Node n = new Node(1, 3, 7, 33, Boolean.FALSE, null);
		assertEquals(Integer.valueOf(1), n.getID());
	}
	
	@Test
	public void testGetMessage() {
		Message m = new Message("Hello world", 1);
		Node n = new Node(1, 3, 7, 33, Boolean.FALSE, null);
		
		n.msg = m;
		
		m.arrivedAt(1);
		System.out.println(n.getMessage().getMessage());
		assertSame(m,n.getMessage());
	}

	@Test
	public void testGenerateTransitionFunction() {

		Node n1 = new Node(1, 3, 7, 33, Boolean.FALSE, null);
		NodeTransitionFunction ntf = n1.createForwardNodeTransitionFunction();
		
		assertEquals(Integer.valueOf(26), ntf.f(5));
	}

	@Test
	public void testTransmitByPathA() {
		Network net = new Network();

		Vector<Integer> path = new Vector<Integer>(Arrays.asList(1,3,5,19));
		Message m = new Message("Hello in testTransmitByPathA", 6);
		
		String nodeFileInName = "/Users/fahrul/Documents/MacquarieDocs/LearningMaterials/sample-inputs/nodedef1.in";		
		// CHANGE THIS STRING (HERE, AND IN TESTS BELOW) TO WHEREVER YOU STORE YOUR DATA
		
		try {
			Vector<Vector<Integer> > inputNodes = net.readNodesFromFile(nodeFileInName);
			for (Vector<Integer> v : inputNodes) {
				Node n = new Node(v.get(0), v.get(1), v.get(2), v.get(3), Boolean.FALSE, net.lookup);
				net.lookup.put(v.get(0), n);
			}
			Node startNode = net.lookup.get(2);
			net.lookup.get(2).sendMsgToNodeByPath(startNode, m, path);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		
		assertFalse(net.lookup.get(19).getMessage().isDestination());
		
	}
	
	@Test
	public void testTransmitByPathB() {
		Network net = new Network();
		Integer[] p = {1, 3, 5, 19};
		Vector<Integer> path = new Vector<Integer>(Arrays.asList(p));
		Message m = new Message("Hello in testTransmitByPathB", 6);
		
		String nodeFileInName = "/Users/fahrul/Documents/MacquarieDocs/LearningMaterials/sample-inputs/nodedef1.in";
		try {
			Vector<Vector<Integer> > inputNodes = net.readNodesFromFile(nodeFileInName);
			for (Vector<Integer> v : inputNodes) {
				Node n = new Node(v.get(0), v.get(1), v.get(2), v.get(3), Boolean.FALSE, net.lookup);
				net.lookup.put(v.get(0), n);
			}
			Node startNode = net.lookup.get(2);
			net.lookup.get(2).sendMsgToNodeByPath(startNode, m, path);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		Integer[] ans = {2, 1, 3, 5, 19};
		Vector<Integer> ansVec = new Vector<Integer>(Arrays.asList(ans));
		Field field = null;
		try {
			field = Message.class.getDeclaredField("visitedNodes");
			field.setAccessible(true);
			try {
				Vector<Integer> fieldValue = (Vector<Integer>) field.get(net.lookup.get(19).getMessage());
				System.out.println(fieldValue);
				assertEquals(ansVec, fieldValue);
				
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} catch (NoSuchFieldException nsfe) {
		    throw new RuntimeException(nsfe);
		}
	}
	
	@Test
	public void testTransmitByPathC() {
		Network net = new Network();
		Integer[] p = {1, 3, 5, 19};
		Vector<Integer> path = new Vector<Integer>(Arrays.asList(p));
		Message m = new Message("Hello in testTransmitByPathC", 19);
		
		String nodeFileInName = "/Users/fahrul/Documents/MacquarieDocs/LearningMaterials/sample-inputs/nodedef1.in";
		try {
			Vector<Vector<Integer> > inputNodes = net.readNodesFromFile(nodeFileInName);
			for (Vector<Integer> v : inputNodes) {
				Node n = new Node(v.get(0), v.get(1), v.get(2), v.get(3), Boolean.FALSE, net.lookup);
				net.lookup.put(v.get(0), n);
			}
			Node startNode = net.lookup.get(2);
			net.lookup.get(2).sendMsgToNodeByPath(startNode, m, path);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		
		assertTrue(net.lookup.get(19).getMessage().isDestination());
	}
	
	@Test
	public void testOneStepTransmitA() {
		Network net = new Network();
		Integer[] ans = {2};
		Vector<Integer> ansVec = new Vector<Integer>(Arrays.asList(ans));
		
		Message msg = new Message("hello", 2);
		
		Node n1 = new Node(1, 3, 7, 33, Boolean.FALSE, net.lookup);
		net.lookup.put(1, n1);
		Node n2 = new Node(2, 3, 7, 33, Boolean.FALSE, net.lookup);
		net.lookup.put(2, n2);
		n1.sendMsgToNodeAnon(n2, msg, 1, null);
		Field field = null;
		try {
			field = Message.class.getDeclaredField("visitedNodes");
			field.setAccessible(true);
			try {
				Vector<Integer> fieldValue = (Vector<Integer>) field.get(n2.getMessage());
				System.out.println(fieldValue);
				assertEquals(ansVec, fieldValue);
				
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} catch (NoSuchFieldException nsfe) {
		    throw new RuntimeException(nsfe);
		}
	}

	@Test
	public void testOneStepTransmitB() {
		Network net = new Network();
		
		Message msg = new Message("hello", 2);
		
		Node n1 = new Node(1, 3, 7, 33, Boolean.FALSE, net.lookup);
		net.lookup.put(1, n1);
		Node n2 = new Node(2, 3, 7, 33, Boolean.FALSE, net.lookup);
		net.lookup.put(2, n2);
		n1.sendMsgToNodeAnon(n2, msg, 1, null);
		
		assertTrue(n2.getMessage().isDestination());
	}

	@Test
	public void testMultiStepTransmit() {
		Network net = new Network();
		Message msg = new Message("Hello in testMultiStepTransmit", 6);
		
		String nodeFileInName = "/Users/fahrul/Documents/MacquarieDocs/LearningMaterials/sample-inputs/nodedef1.in";
		try {
			Vector<Vector<Integer> > inputNodes = net.readNodesFromFile(nodeFileInName);
			for (Vector<Integer> v : inputNodes) {
				Node n = new Node(v.get(0), v.get(1), v.get(2), v.get(3), Boolean.FALSE, net.lookup);
				net.lookup.put(v.get(0), n);
			}
			NodeTransitionFunction ntf = net.lookup.get(1).createForwardNodeTransitionFunction();
			net.lookup.get(1).sendMsgToNodeAnon(net.lookup.get(4), msg, 24, ntf);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}


		Integer[] ans = {4, 10, 6};  // note that the start node 1 is not in the visited list;
		                             // nodes are only recorded as being visited when a message is received,
		                             // and the start node is added in initiateMessageAnon 
		Vector<Integer> ansVec = new Vector<Integer>(Arrays.asList(ans));
		Field field = null;
		try {
			field = Message.class.getDeclaredField("visitedNodes");
			field.setAccessible(true);
			try {
				Vector<Integer> fieldValue = (Vector<Integer>) field.get(net.lookup.get(6).getMessage());
				System.out.println(fieldValue);
				assertEquals(ansVec, fieldValue);
				
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} catch (NoSuchFieldException nsfe) {
		    throw new RuntimeException(nsfe);
		}
	}

	// CREDIT-LEVEL TESTS

	@Test
	public void testCalcFirstR() {

		Node n1 = new Node(1, 3, 7, 33, Boolean.FALSE, null);
		assertEquals(Integer.valueOf(24), n1.firstRForInitiatingMessage(3, 6));
	}

	@Test
	public void testBasicTransmissionCheck() {
		// Tests example from specs

		Network net = new Network();

		String nodeFileInName = "/Users/fahrul/Documents/MacquarieDocs/LearningMaterials/sample-inputs/nodedef1.in";
		Message m = new Message("hello",6);
		
		try {
			Vector<Vector<Integer> > inputNodes = net.readNodesFromFile(nodeFileInName);
			for (Vector<Integer> v : inputNodes) {
				Node n = new Node(v.get(0), v.get(1), v.get(2), v.get(3), Boolean.FALSE, net.lookup);
				net.lookup.put(v.get(0), n);
			}
			net.lookup.get(1).initiateMessageAnon("hello", 3, 6);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		Integer[] ans = {1, 4, 10, 6};
		Vector<Integer> ansVec = new Vector<Integer>(Arrays.asList(ans));
		Field field = null;
		try {
			field = Message.class.getDeclaredField("visitedNodes");
			field.setAccessible(true);
			try {
				Vector<Integer> fieldValue = (Vector<Integer>) field.get(net.lookup.get(6).getMessage());
				System.out.println(fieldValue);
				assertEquals(ansVec, fieldValue);

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} catch (NoSuchFieldException nsfe) {
			throw new RuntimeException(nsfe);
		}

	}

	@Test
	public void testCalcFirstRTwo() {

		Network net = new Network();

		String nodeFileInName = "/Users/fahrul/Documents/MacquarieDocs/LearningMaterials/sample-inputs/nodedef1.in";
		try {
			Vector<Vector<Integer> > inputNodes = net.readNodesFromFile(nodeFileInName);
			for (Vector<Integer> v : inputNodes) {
				Node n = new Node(v.get(0), v.get(1), v.get(2), v.get(3), Boolean.FALSE, net.lookup);
				net.lookup.put(v.get(0), n);
			}
			net.lookup.get(1).initiateMessageAnon("hello", 3, 6);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		
		assertEquals(Integer.valueOf(17), net.lookup.get(1).firstRForInitiatingMessage(4, 8));
	}

	@Test
	public void testBasicTransmissionCheckTwo() {

		Network net = new Network();

		String nodeFileInName = "/Users/fahrul/Documents/MacquarieDocs/LearningMaterials/sample-inputs/nodedef1.in";
		try {
			Vector<Vector<Integer> > inputNodes = net.readNodesFromFile(nodeFileInName);
			for (Vector<Integer> v : inputNodes) {
				Node n = new Node(v.get(0), v.get(1), v.get(2), v.get(3), Boolean.FALSE, net.lookup);
				net.lookup.put(v.get(0), n);
			}
			net.lookup.get(1).initiateMessageAnon("hello", 4, 8);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}



		Integer[] ans = {1, 17, 9, 2, 8};
		Vector<Integer> ansVec = new Vector<Integer>(Arrays.asList(ans));
		Field field = null;
		try {
			field = Message.class.getDeclaredField("visitedNodes");
			field.setAccessible(true);
			try {
				Vector<Integer> fieldValue = (Vector<Integer>) field.get(net.lookup.get(8).getMessage());
				System.out.println(fieldValue);
				assertEquals(ansVec, fieldValue);

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} catch (NoSuchFieldException nsfe) {
			throw new RuntimeException(nsfe);
		}
	}
	
	@Test
	public void testGenerateTransitionFunctionThree() {

		Node n1 = new Node(2, 5, 29, 91, Boolean.FALSE, null);
		NodeTransitionFunction ntf = n1.createForwardNodeTransitionFunction();
		
		assertEquals(Integer.valueOf(61), ntf.f(3));
	}

	@Test
	public void testBigIntR() {
		NodeTransitionFunction ntf = new NodeTransitionFunction(3, 33);
		BigInteger r = new BigInteger("5");
		assertEquals(new BigInteger("26"), ntf.f(r));
	}

	@Test
	public void testCalcFirstBigIntR() {

		Node n1 = new Node(1, 3, 7, 33, Boolean.FALSE, null);
		assertEquals(new BigInteger("24"), n1.firstRForInitiatingMessage(3, new BigInteger("6")));
	}
	
	@Test
	public void testOneStepTransmitBigInt() {
		Network net = new Network();
		Integer[] ans = {2};
		Vector<Integer> ansVec = new Vector<Integer>(Arrays.asList(ans));
		Message msg = new Message("hello", 2);
		
		Node n1 = new Node(1, 3, 7, 33, Boolean.FALSE, net.lookup);
		net.lookup.put(1, n1);
		Node n2 = new Node(2, 3, 7, 33, Boolean.FALSE, net.lookup);
		net.lookup.put(2, n2);
		n1.sendMsgToNodeAnon(n2, msg, new BigInteger("1"), null);
		Field field = null;
		try {
			field = Message.class.getDeclaredField("visitedNodes");
			field.setAccessible(true);
			try {
				Vector<Integer> fieldValue = (Vector<Integer>) field.get(n2.getMessage());
				System.out.println(fieldValue);
				assertEquals(ansVec, fieldValue);
				
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} catch (NoSuchFieldException nsfe) {
		    throw new RuntimeException(nsfe);
		}
	}

	@Test
	public void testBigIntRThree() {
		NodeTransitionFunction ntf = new NodeTransitionFunction(5, 91);
		BigInteger r = new BigInteger("3");
		assertEquals(new BigInteger("61"), ntf.f(r));
	}

	@Test
	public void testCalcFirstBigIntRThree() {

		Node n1 = new Node(3, 5, 29, 91, Boolean.FALSE, null);
		assertEquals(new BigInteger("41"), n1.firstRForInitiatingMessage(2, new BigInteger("6")));
	}
	
	@Test
	public void testOneStepTransmitBigIntThree() {
		Network net = new Network();
		Integer[] ans = {3};
		Vector<Integer> ansVec = new Vector<Integer>(Arrays.asList(ans));

		Node n1 = new Node(2, 5, 29, 91, Boolean.FALSE, net.lookup);
		net.lookup.put(2, n1);
		Node n2 = new Node(3, 3, 27, 55, Boolean.FALSE, net.lookup);
		net.lookup.put(3, n2);
		Message msg = new Message("hello", 3);
		n1.sendMsgToNodeAnon(n2, msg, new BigInteger("2"), null);
		Field field = null;
		try {
			field = Message.class.getDeclaredField("visitedNodes");
			field.setAccessible(true);
			try {
				Vector<Integer> fieldValue = (Vector<Integer>) field.get(n2.getMessage());
				System.out.println(fieldValue);
				assertEquals(ansVec, fieldValue);
				
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} catch (NoSuchFieldException nsfe) {
		    throw new RuntimeException(nsfe);
		}
	}

	// (HIGH) DISTINCTION-LEVEL TESTS

	@Test
	public void testSetCorruptA() {
		Network net = new Network();
		String nodeFileInName = "/Users/fahrul/Documents/MacquarieDocs/LearningMaterials/sample-inputs/nodedef1.in";

		try {
			Vector<Vector<Integer> > inputNodes = net.readNodesFromFile(nodeFileInName);
			for (Vector<Integer> v : inputNodes) {
				Node n = new Node(v.get(0), v.get(1), v.get(2), v.get(3), Boolean.TRUE, net.lookup);
				net.lookup.put(v.get(0), n);
			}
			net.printNetwork();
			
			int[] corruptNodes = {2, 8, 13, 19};
			for (int i = 0; i < corruptNodes.length; i++)
				net.lookup.get(corruptNodes[i]).setCorrupt();

			net.lookup.get(2).sendMsgToNodeAnon(net.lookup.get(8), new Message("hello", 8), new BigInteger("2"), null);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		
		assertEquals(Integer.valueOf(2), net.lookup.get(8).lastSender());
	}
	
	@Test
	public void testSetCorruptB() {
		Network net = new Network();
		String nodeFileInName = "/Users/fahrul/Documents/MacquarieDocs/LearningMaterials/sample-inputs/nodedef1.in";

		try {
			Vector<Vector<Integer> > inputNodes = net.readNodesFromFile(nodeFileInName);
			for (Vector<Integer> v : inputNodes) {
				Node n = new Node(v.get(0), v.get(1), v.get(2), v.get(3), Boolean.TRUE, net.lookup);
				net.lookup.put(v.get(0), n);
			}
			net.printNetwork();
			
			int[] corruptNodes = {2, 8, 13, 19};
			for (int i = 0; i < corruptNodes.length; i++)
				net.lookup.get(corruptNodes[i]).setCorrupt();

			net.lookup.get(2).sendMsgToNodeAnon(net.lookup.get(9), new Message("hello", 9), new BigInteger("2"), null);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		
		assertEquals(Integer.valueOf(-1), net.lookup.get(9).lastSender());
	}
	
	@Test
	public void testGuessInitiator() {
		Network net = new Network();
		String nodeFileInName = "/Users/fahrul/Documents/MacquarieDocs/LearningMaterials/sample-inputs/nodedef1.in";
		String msgFileInName = "/Users/fahrul/Documents/MacquarieDocs/LearningMaterials/sample-inputs/msg0.in";

		try {
			Vector<Vector<Integer> > inputNodes = net.readNodesFromFile(nodeFileInName);
			for (Vector<Integer> v : inputNodes) {
				Node n = new Node(v.get(0), v.get(1), v.get(2), v.get(3), Boolean.TRUE, net.lookup);
				net.lookup.put(v.get(0), n);
			}
			net.printNetwork();
			
			int[] corruptNodes = {2, 8, 13, 19};
			for (int i = 0; i < corruptNodes.length; i++)
				net.lookup.get(corruptNodes[i]).setCorrupt();

			Vector<String> messages = net.readMessagesFromFile(msgFileInName);
			net.lookup.get(1).initiateMessageAnon(messages.get(0), 2, 8);
			net.lookup.get(1).initiateMessageAnon(messages.get(0), 4, 7);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		
		assertEquals(Integer.valueOf(1), net.lookup.get(8).guessInitiator());
	}
	
	@Test
	public void testGuessInitiatorThreeA() {
		Network net = new Network();
		String nodeFileInName = "/Users/fahrul/Documents/MacquarieDocs/LearningMaterials/sample-inputs/nodedef2.in"; //2
		String msgFileInName = "/Users/fahrul/Documents/MacquarieDocs/LearningMaterials/sample-inputs/msg0.in";

		try {
			Vector<Vector<Integer> > inputNodes = net.readNodesFromFile(nodeFileInName);
			for (Vector<Integer> v : inputNodes) {
				Node n = new Node(v.get(0), v.get(1), v.get(2), v.get(3), Boolean.TRUE, net.lookup);
				net.lookup.put(v.get(0), n);
			}
			net.printNetwork();
			
			int[] corruptNodes = {1, 6, 16, 21};
			for (int i = 0; i < corruptNodes.length; i++)
				net.lookup.get(corruptNodes[i]).setCorrupt();

			Vector<String> messages = net.readMessagesFromFile(msgFileInName);
			net.lookup.get(3).initiateMessageAnon(messages.get(0), 4, 6);
			net.lookup.get(3).initiateMessageAnon(messages.get(0), 3, 16);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		
		assertEquals(Integer.valueOf(3), net.lookup.get(6).guessInitiator());
	}
	
	@Test
	public void testGuessInitiatorThreeB() {
		Network net = new Network();
		String nodeFileInName = "/Users/fahrul/Documents/MacquarieDocs/LearningMaterials/sample-inputs/nodedef2.in"; //2
		String msgFileInName = "/Users/fahrul/Documents/MacquarieDocs/LearningMaterials/sample-inputs/msg0.in";

		try {
			Vector<Vector<Integer> > inputNodes = net.readNodesFromFile(nodeFileInName);
			for (Vector<Integer> v : inputNodes) {
				Node n = new Node(v.get(0), v.get(1), v.get(2), v.get(3), Boolean.TRUE, net.lookup);
				net.lookup.put(v.get(0), n);
			}
			net.printNetwork();
			
			int[] corruptNodes = {1, 6, 16};
			for (int i = 0; i < corruptNodes.length; i++)
				net.lookup.get(corruptNodes[i]).setCorrupt();

			Vector<String> messages = net.readMessagesFromFile(msgFileInName);
			net.lookup.get(3).initiateMessageAnon(messages.get(0), 4, 6);
			net.lookup.get(3).initiateMessageAnon(messages.get(0), 3, 16);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		
		assertEquals(Integer.valueOf(-1), net.lookup.get(6).guessInitiator());
	}
	
	@Test
	public void testAllVisitableNodes() {

		Network net = new Network();
		Vector<Integer> ans = new Vector<Integer>(Arrays.asList(1, 4, 6, 10, 18));

		String nodeFileInName = "/Users/fahrul/Documents/MacquarieDocs/LearningMaterials/sample-inputs/nodedef1.in";
		try {
			Vector<Vector<Integer> > inputNodes = net.readNodesFromFile(nodeFileInName);
			for (Vector<Integer> v : inputNodes) {
				Node n = new Node(v.get(0), v.get(1), v.get(2), v.get(3), Boolean.FALSE, net.lookup);
				net.lookup.put(v.get(0), n);
			}
			net.lookup.get(1).allVisitableNodes(6);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		
		assertEquals(ans, net.lookup.get(1).allVisitableNodes(6));
	}
}

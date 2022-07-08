package functions;

import java.math.*;  // for BigInteger

public class NodeTransitionFunction {
	
	Integer e;
	Integer K;
	BigInteger eBig;
	BigInteger KBig;
	
	public NodeTransitionFunction(Integer exp, Integer KVal) {
		// CONSTUCTOR: Sets the class to calculate f(x) = (x ^ exp) mod KVal 
		
		// TODO
		
		e = exp;
		K = KVal;
		eBig = BigInteger.valueOf(exp);
		KBig = BigInteger.valueOf(KVal);
		
	
	}
	
	public Integer f(Integer val) {
		// PRE: -
		// POST: Implements f(val)
		// Code adapted from https://www.geeksforgeeks.org/modular-exponentiation-power-in-modular-arithmetic/
		
		// TODO
		
		Integer result = 1;
		
		if(val >= K) {
			val = val % K;
		}
		
		if(val == 0) {
			return 0;
		}
		
		while(e>0) {
			if(e%2 != 0) {
				result = (result * val) % K;
			}
			
			e = e/2;
			val = (val * val) % K;	
		}
		
		return result;
	}

	public BigInteger f(BigInteger val) {
		// PRE: -
		// POST: Implements f(val), with val as a BigInteger

		// TODO
		
		return val.modPow(eBig, KBig);
	}

	public static void main(String[] args) {
		NodeTransitionFunction ntf = new NodeTransitionFunction(3, 33);
		
		System.out.println(ntf.f(5));
	}
	
}

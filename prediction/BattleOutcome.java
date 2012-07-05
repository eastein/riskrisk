package prediction;
import fraction.*;

/**
 * Class to represent a single outcome of a battle - no more 
 * fighting is possible whenever data is stored in a BattleOutcome.
 * Also stores the exact probability of the outcome and provides
 * methods for manipulating it. 
 * 
 * @author Eric Stein <eastein@wpi.edu>
 * Copyright (c) 2005 Eric Stein.  Licensed under the GNU Public License, Version 2.
 * The GPL is included in this program distribution in gpl.txt.
 */
public class BattleOutcome {
	// number of attackers remaining
	private int a_left;
	// number of defenders remaining
	private int d_left;
	
	Fraction probability;
	
	/**
	 * compares this BattleOutcome to the passed BattleOutcome.  Compares only
	 * the army numbers, not probabilities.
	 * @param obj
	 */
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this.getClass() != obj.getClass()) return false;
		BattleOutcome b = (BattleOutcome)obj;
		return (b.a_left == a_left && b.d_left == d_left);
	}
	
	/**
	 * determines if the passed army sizes are
	 * a finished battle
	 * 
	 * @param a
	 * @param d
	 */
	static boolean isFinal(int a, int d) {
		return ((a == 1 && d >= 1) || (a > 1 && d == 0));
	}
	
	/**
	 * Builds a BattleOutcome with the passed army sizes and
	 * a probability of 1/1.
	 * 
	 * @param a
	 * @param d
	 * @throws RuntimeException when not a valid completed battle
	 */
	public BattleOutcome(int a, int d) throws RuntimeException {
		if (!isFinal(a, d)) {
			throw new RuntimeException("Not a valid final result.");
		} else {
			a_left = a;
			d_left = d;
			probability = new Fraction(1, 1);
		}
	}
	
	/**
	 * Builds a BattleOutcome with the passed army sizes and
	 * the given probability
	 * 
	 * @param a
	 * @param d
	 * @param f
	 * @throws RuntimeException when not a valid completed battle
	 */
	public BattleOutcome(int a, int d, Fraction f) throws RuntimeException {
		if (!isFinal(a, d)) {
			throw new RuntimeException("Not a valid final result.");
		} else {
			a_left = a;
			d_left = d;
			probability = f;
		}
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param b
	 */
	public BattleOutcome(BattleOutcome b) {
		a_left = b.a_left;
		d_left = b.d_left;
		probability = new Fraction(b.probability);
	}
	
	/**
	 * Clones self but multiplies the probability of the
	 * clone by the given Fraction
	 * 
	 * @param f
	 */
	public BattleOutcome cloneMultiply(Fraction f) {
		Fraction t = new Fraction(probability);
		t.multiply(f);
		return new BattleOutcome(a_left, d_left, t);
	}
	
	/**
	 * Calls the reduce() function on the probability - delegation
	 */
	public void reduce () {
		probability.reduce();
	}
	
	/**
	 * Determines whether or not the attackers were victorious with at least n armies remaining
	 */
	public boolean victory(int n) {
		return (d_left == 0) && (a_left >= n);
	}
	
	/**
	 * Represent as a string with the default display method
	 */
	public String toString() {
		return toString(Fraction.default_print);
	}
	
	/**
	 * Represent as a string with one of the 3 display methods
	 */
	public String toString(int type) {
		return "Attacker: " + a_left + " Defender: " + d_left + " Probability: " + probability.toString(type);
	}
}
package prediction;
import fraction.*;

/**
 * Represents a list of BattleOutcomes - with plenty of helpful functions
 * for manipulation.
 * 
 * @author Eric Stein <eastein@wpi.edu>
 * Copyright (c) 2005 Eric Stein.  Licensed under the GNU Public License, Version 2.
 * The GPL is included in this program distribution in gpl.txt.
 */
public class BattleOutcomeSet {
	//point to first node
	private BattleOutcomeNode head;
	//point to the last node
	private BattleOutcomeNode tail;
	//keep track of number of BattleOutcomes
	private int count;
	
	/**
	 * Default constructor produces empty BattleOutcomeSet
	 */
	public BattleOutcomeSet() {
		head = null;
		tail = null;
		count = 0;
	}
	
	/**
	 * Add a BattleOutcome to the set without attempting to merge
	 * it with an already existing equivalent BattleOutcome
	 * @param b
	 */
	public void append(BattleOutcome b) {
		if (head == null) {
			head = new BattleOutcomeNode(b);
			tail = head;
			count = 1;
		} else {
			tail.next = new BattleOutcomeNode(b);
			tail = tail.next;
			count++;
		}
	}
	
	/**
	 * Add a BattleOutcome to the set - attempt to combine it with
	 * an equivalent BattleOutcome.  If not possible, mereley append.
	 * @param b
	 */
	public void merge(BattleOutcome b) {
		if (head != null) {
			//search for an equivalent BattleOutcome
			BattleOutcomeNode p = head;
			while (p != null) {
				if (p.data.equals(b)) {
					//found an equivalent BattleOutcome! merge.
					p.data.probability.add(b.probability);
					return;
				}
				p = p.next;
			}
		}
		//could not merge, append:
		append(b);
	}
	
	/**
	 * Represent BattleOutcomeSet as a string.
	 */
	public String toString() {
		if (head == null) {
			return "";
		} else {
			String b = "";
			BattleOutcomeNode p = head;
			while (p != null) {
				b += p.data;
				if (p.next != null)
					b += "\n";
				p = p.next;
			}
			return b;
		}
	}
	
	/**
	 * Determines the probability of successful invasion with the given number of armies left
	 */
	public Fraction probVictory(int n) {
		if (head == null) {
			return new Fraction(0,1);
		} else {
			BattleOutcomeNode b = head;
			Fraction f = new Fraction(0,1);
			while (b != null) {
				if (b.data.victory(n))
					f.add(b.data.probability);
				b = b.next;
			}
			f.reduce();
			return f;
		}
	}
	
	/**
	 * Private constructor for use by other functions that need to generate
	 * a BattleOutcomeSet
	 * @param h
	 * @param t
	 * @param c
	 */
	private BattleOutcomeSet(BattleOutcomeNode h, BattleOutcomeNode t, int c) {
		head = h;
		tail = t;
		count = c;
	}
	
	/**
	 * Copy constructor
	 * @param b
	 */
	public BattleOutcomeSet(BattleOutcomeSet b) {
		if (b.head == null) {
			head = null;
			tail = null;
			count = 0;
		} else {
			count = b.count;
			BattleOutcomeNode h = new BattleOutcomeNode(new BattleOutcome(b.head.data));
			head = h;
			BattleOutcomeNode p = b.head.next;
			while (p != null) {
				h.next = new BattleOutcomeNode(p.data);
				h = h.next;
				tail = h;
				p = p.next;
			} 
		}
	}
	
	/**
	 * Return the number of BattleOutcomes in the set.
	 */
	public int count() {
		return count;
	}
	
	/**
	 * Generate a new BattleOutcomeSet with the same outcomes as this,
	 * but with all probabilities multiplied by f
	 * @param f
	 */
	public BattleOutcomeSet cloneMultiply(Fraction f) {
		if (head == null) {
			return new BattleOutcomeSet();
		} else {
			//point p at the first node (it exists)
			BattleOutcomeNode p = head;
			//point nh at the first node in the new list
			BattleOutcomeNode nh = new BattleOutcomeNode(p.data.cloneMultiply(f));
			//there is now one node in the list
			int c = 1;
			//the current head of the new list is also the tail (for now)
			BattleOutcomeNode t = nh;
			while (p.next != null) {
				//advance
				p = p.next;
				//point the last node at the new one
				t.next = new BattleOutcomeNode(p.data.cloneMultiply(f));
				//point at the new node
				t = t.next;
				//there's now another node, increment counter
				c++;
			}
			//use private constructor to generate new BattleOutcomeSet
			return new BattleOutcomeSet(nh, t, c);
		}
	}
	
	/**
	 * Merge another BattleOutcomeSet's BattleOutcomes into this set.
	 * @param b
	 */
	public void merge(BattleOutcomeSet b) {
		if (b.head != null) {
			BattleOutcomeNode p = b.head;
			//cycle through passed BattleOutcomeSet's nodes
			while (p != null) {
				//merge BattleOutcome from node into this
				merge(p.data);
				p = p.next;
			}
		}
	}
}
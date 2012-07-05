package prediction;

/**
 * Node for BattleOutcomeSet (which is basically a glorified linked list)
 * 
 * @author Eric Stein <eastein@wpi.edu>
 * Copyright (c) 2005 Eric Stein.  Licensed under the GNU Public License, Version 2.
 * The GPL is included in this program distribution in gpl.txt.
 */
public class BattleOutcomeNode {
	BattleOutcomeNode next;
	BattleOutcome data;
	
	/**
	 * Create a node with BattleOutcome already attached
	 * @param b
	 */
	public BattleOutcomeNode(BattleOutcome b) {
		next = null;
		data = b;
	}
	
	/**
	 * Create a blank node
	 */
	public BattleOutcomeNode() {
		next = null;
		data = null;
	}
}
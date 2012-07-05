package prediction;

/**
 * Class for finding precise and accurate probabilities of the outcomes of battles
 * to the death in the game Risk.  Everything is static for a reason - it caches
 * the recursive calculation steps to make further calculations faster.
 * 
 * @author Eric Stein <eastein@wpi.edu>
 * Copyright (c) 2005 Eric Stein.  Licensed under the GNU Public License, Version 2.
 * The GPL is included in this program distribution in gpl.txt.
 */
public class Battle {
	//default cache size limits
	private static int adim = 42;
	private static int ddim = 42;
	/**
	 * cache to make recursive calculation possible
	 */
	private static BattleOutcomeSet[][] cache = new BattleOutcomeSet[adim][ddim];
	
	/**
	 * Calculates all the possible outcomes of a fight to the death and
	 * their probabilities.  Returns null if the arguments are illegal.
	 * 
	 * @param a
	 * @param d
	 * @return BattleOutcomeSet
	 */
	public static BattleOutcomeSet predict(int a, int d) {
		if ((a > 1) && (d > 0)) {
			if (a > adim  || d > ddim ) {
				//outside of cache range, find new size for cache
				int nad = Math.max(a, adim);
				int ndd = Math.max(d, ddim);
				//build new cache
				BattleOutcomeSet[][] resize = new BattleOutcomeSet[nad][ndd];
				//copy old cache to new cache
				for (int i = 0; i < adim; i++) {
					for (int j = 0; j < ddim; j++) {
						resize[i][j] = cache[i][j];
					}
				}
				//record new cache size
				adim = nad;
				ddim = ndd;
				//overwrite old cache
				cache = resize;
			}
			
			if (cache[a - 1][d - 1] == null) {
				//if the requested battle has not been calculated,
				//calculate it and place it in cache
				cache[a - 1][d - 1] = calcPredict(a, d);
			}
			//return from cache
			return cache[a - 1][d - 1];
		} else {
			if (BattleOutcome.isFinal(a, d)) {
				BattleOutcomeSet b = new BattleOutcomeSet();
				b.append(new BattleOutcome(a, d));
				return b;
			} else {
				//cannot be calculated, arguments illegal
				return null;
			}
		}
	}
	
	/**
	 * Does the actual calculation for predict().  No argument bounds checking
	 * whatsoever - use without predict() at your own risk.
	 * @param a
	 * @param d
	 */
	public static BattleOutcomeSet calcPredict(int a, int d) {
		//create empty set of battle outcomes
		BattleOutcomeSet merger = new BattleOutcomeSet();
		//find number of dice attacker is allowed
		int adice = Math.min(a - 1, DiceOutcome.MAX_A_DICE);
		//find number of dice defender is allowed
		int ddice = Math.min(d, DiceOutcome.MAX_D_DICE);
		//find the total number of army losses there will be
		int loss = Math.min(adice, ddice);
		//distribute them in all possible ways between attacker and defender...
		for (int i = 0; i <= loss; i++) {
			/*
			 * this is the real meat of the program.  Summary of
			 * what this hard to read line actually does:
			 * 
			 * 1 calls predict with the arguments
			 * 1.1 the number of men the attacker will have remaining
			 * 1.2 the number of men the defender will have remaining
			 * 2 calls DiceOutcome.probability (which is itself a caching function) with the
			 *    dice roll required to produce the losses resulting in the remaining armies
			 *    passed to predict()
			 * 3 calls cloneMultiply() on the BattleOutcomeSet returned by predict()
			 *    with the Fraction returned by DiceOutcome.probability()
			 * 4 calls merge() on the BattleOutcomeSet that is being built with the
			 *    BattleOutcomeSet produced by the above 3 steps.  This adds all the
			 *    matching outcomes' probabilities.
			 */ 
			merger.merge(predict(a - loss + i, d - i).cloneMultiply(DiceOutcome.probability(adice, ddice, i)));
		}
		//you've now merged all the outcome sets together, return!
		return merger;
	}
}
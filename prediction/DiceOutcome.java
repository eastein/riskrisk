package prediction;
import fraction.*;

/**
 * Class with static methods for finding the probabilities of specific outcomes of
 * specific die rolls in Risk using standard rules.
 * 
 * @author Eric Stein <eastein@wpi.edu>
 * Copyright (c) 2005 Eric Stein.  Licensed under the GNU Public License, Version 2.
 * The GPL is included in this program distribution in gpl.txt.
 */
public class DiceOutcome {
	//these constants govern the dice tossing rules.  Modifying them will
	//affect all needed changes project-wide.
	public static final int DIE_SIDES = 6;
	public static final int MAX_A_DICE = 3;
	public static final int MAX_D_DICE = 2;
	
	//create cache array
	private static Fraction[][][] cache = new Fraction[MAX_A_DICE][MAX_D_DICE][Math.min(MAX_A_DICE, MAX_D_DICE) + 1];
	
	/**
	 * Performs a power calculation on two longs.  Not optimised for anything except
	 * very small exponents - beware using it for very large exponents.
	 * @param a
	 * @param b
	 */
	private static long pow(long a, long b) {
		long r = 1;
		for (long i = 0; i < b; i++) {
			r *= a;
		}
		return r;
	}
	
	/**
	 * Calculates probability of the specified number of attacker wins based on the
	 * number of dice rolled be each side.  Throws a runtime exception if the input
	 * isn't ok.  Note: actual calculation is performed by calcProbability().
	 * 
	 * @param adice
	 * @param ddice
	 * @param awin
	 * @return Fraction representing the probability of the event
	 */
	public static Fraction probability(int adice, int ddice, int awin) {
		//throw runtime exception if bad input
		if ((adice > MAX_A_DICE) || (ddice > MAX_D_DICE) || (adice < 1) || (ddice < 1) || (awin < 0) || (awin > Math.min(adice, ddice)))
			throw new RuntimeException("Impossible probability calculation");
		
		//if it isn't in cache, call secondary function and place in cache
		if (cache[adice - 1][ddice - 1][awin] == null)
			cache[adice - 1][ddice - 1][awin] = calcProbability(adice, ddice, awin);
		
		//grab from cache and return it
		return cache[adice - 1][ddice - 1][awin];
	}
	
	/**
	 * See the description of probability() for explanation.
	 * @param adice
	 * @param ddice
	 * @param awin
	 */
	private static Fraction calcProbability(int adice, int ddice, int awin) {
		//integer counter for counting the number of outcomes that match the criteria
		long result_matches = 0;
		//total number of outcomes
		long outcomes = pow(DIE_SIDES, adice + ddice);
		
		//counter for number of times the attacker wins the roll
		long attacker_wins;
		//array for storing dice results
		long[] dice_outcome;
		for (long o = 0; o < outcomes; o++) {
			//set counter to 0
			attacker_wins = 0;
			//place the demultiplexed dice in the array
			dice_outcome = diceDemultiplex(o, adice + ddice);

			//sort attacker's dice
			bubbleSort(dice_outcome, 0, adice - 1);
			//sort defender's dice
			bubbleSort(dice_outcome, adice, adice + ddice - 1);
			
			//loop through top dies, incrementing if attacker wins
			for (int i = 0; i < Math.min(adice, ddice); i++)
				if (dice_outcome[adice - Math.min(adice, ddice) + i] > dice_outcome[adice + ddice - Math.min(adice, ddice) + i])
					attacker_wins++;
			
			//if the number matches the expected number, mark this outcome as matching
			//the criteria
			if (attacker_wins == awin)
				result_matches++;
		}
		 
		//create a fully reduced fractional probability
		Fraction prob = new Fraction(result_matches, outcomes);
		prob.reduce();
		return prob;
	}
	
	/**
	 * bubblesort the section of the array of longs specified
	 * @param array
	 * @param start
	 * @param end
	 */
	private static void bubbleSort(long[] array, int start, int end) {
		if (end - start > 0) {
			//temp variable for storing longs during swap
			long t;
			boolean change = true;
			while (change) {
				//stop loop unless a swap is made
				change = false;
				for (int i = start; i < end; i++) {
					//if a swap is required...
					if (array[i] > array[i + 1]) {
						//swap
						t = array[i];
						array[i] = array[i + 1];
						array[i + 1] = t;
						//and note it.
						change = true;
					}
				}
			}
		}
	}
	
	/**
	 * Demultiplexes n into the specified number of dies in an array.
	 * Equivalent to putting the number n into base DIE_SIDES and putting
	 * the digits into an array.
	 * @param n
	 * @param dies
	 * @return
	 */
	private static long[] diceDemultiplex(long n, int dies) {
		//allocate array
		long[] temp = new long[dies];
		
		//iterate through digits
		for (int d = 0; d < dies; d++)
			temp[d] = (n % pow(DIE_SIDES, d + 1) - n % pow(DIE_SIDES, d)) / pow(DIE_SIDES, d);
		
		//return array
		return temp;
	}
}
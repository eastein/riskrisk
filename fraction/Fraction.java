package fraction;
import java.math.BigInteger;
import java.math.BigDecimal;

/**
 * Class to represent fraction objects.  Does not allow itself to hold invalid
 * fractions, and all negative fractions are shown to be negative by a negative
 * numerator.
 * 
 * @author Eric Stein <eastein@wpi.edu>
 * Copyright (c) 2005 Eric Stein.  Licensed under the GNU Public License, Version 2.
 * The GPL2 is distributed with this program as gpl.txt.
 */
public class Fraction {
	/** store the value of the numerator */
	private BigInteger numerator;
	/** store the value of the denominator */
	private BigInteger denominator;
	
	public static final int PRINT_FRACTION = 1;
	public static final int PRINT_DECIMAL = 2;
	public static final int PRINT_BOTH = PRINT_FRACTION + PRINT_DECIMAL;
	public static int default_print = PRINT_BOTH;
	
	/**
	 * generate a fraction object with no errors
	 * @param n
	 * @param d
	 * @throws Exception if the denominator is not a positive integer
	 */
	public Fraction(long n, long d) {
		numerator = BigInteger.valueOf(n);
		if (d < 1) throw new RuntimeException ("Invalid denominator");
		denominator = BigInteger.valueOf(d);
	}
	
	/**
	 * generate a fraction object from another fraction object (copy constructor)
	 * @param obj
	 */
	public Fraction(Fraction obj) {
		numerator = obj.numerator;
		denominator = obj.denominator;
	}
	
	/**
	 * gets the numerator
	 * @return numerator
	 */
	public long getNumerator() {
		return numerator.longValue();
	}
	
	/**
	 * gets the denominator
	 * @return denominator
	 */
	public long getDenominator() {
		return denominator.longValue();
	}
	
	/**
	 * sets the numerator
	 * @param i
	 */
	public void setNumerator(long i) {
		numerator = BigInteger.valueOf(i);
	}
	
	/**
	 * sets the denominator
	 * @param i
	 * @throws Exception if i is not a positive integer
	 */
	public void setDenominator(long i) {
		if (i < 1) {
			throw new RuntimeException ("Denominator must be a positive integer.");
		} else {
			denominator = BigInteger.valueOf(i);
		}
	}
	
	/**
	 * perform floating point division with the numerator and denominator, producing decimal form
	 * @return double representing fraction
	 */
	public double toDouble() {
		return toBigDecimal().doubleValue();
	}
	
	/**
	 * sets fraction object to the reciprocal of itself
	 * @throws Exception if the numerator is currently zero (division by zero)
	 */
	public void invert() {
		if (numerator.equals(BigInteger.ZERO)) throw new RuntimeException ("Cannot invert: numerator is zero");
		
		BigInteger t = denominator;
		denominator = numerator;
		numerator = t;

		if (denominator.compareTo(BigInteger.ZERO) == -1) {
			denominator = denominator.negate();
			numerator = numerator.negate();
		}
	}
	
	/**
	 * adds the fraction f to the fraction, avoiding too large an increase in the denominator.
	 * @param f
	 */
	public void add(Fraction f) {
		BigInteger lcm = denominator.multiply(f.denominator).divide(denominator.gcd(f.denominator));
		numerator = numerator.multiply(lcm).divide(denominator).add(f.numerator.multiply(lcm).divide(f.denominator));
		denominator = lcm;
	}
	
	/** 
	 * multiplies the fraction f into the fraction.  Does not reduce.
	 * @param f
	 */
	public void multiply(Fraction f) {
		numerator = numerator.multiply(f.numerator);
		denominator = denominator.multiply(f.denominator);
	}
	
	/**
	 * Reduces to simplest form using greatest common divisor of numerator
	 * and denominator.
	 */
	public void reduce() {
		BigInteger gcd = numerator.gcd(denominator);
		numerator = numerator.divide(gcd);
		denominator = denominator.divide(gcd);
	}
	
	/**
	 * Represent the fraction as a string using hte default display type
	 */
	public String toString() {
		return toString(default_print);
	}
	
	/** 
	 * represents the fraction as a string - using the specified display type
	 * @return string representing fraction
	 */
	public String toString(int type) {
		String frac;
		if (numerator.equals(BigInteger.ZERO)) {
			frac = "0";
		} else {
			if (denominator.equals(BigInteger.ONE)) {
				frac = numerator.toString();
			} else {
				frac = numerator + "/" + denominator;
			}
		}
		switch (type) {
			case PRINT_FRACTION:
				return frac;
			case PRINT_DECIMAL:
				return toDouble() + "";
			case PRINT_BOTH:
				return frac + " (" + toDouble() + ")";
		}
		throw new RuntimeException("No such display type:" + type);
	}
	
	/**
	 * Produce a BigDecimal representation of the fraction
	 */
	public BigDecimal toBigDecimal() {
		return (new BigDecimal(numerator)).divide(new BigDecimal(denominator), 200, BigDecimal.ROUND_HALF_EVEN);
	}
}
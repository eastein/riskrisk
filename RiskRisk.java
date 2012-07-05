import prediction.*;
import fraction.*;
import java.util.Scanner;

/**
 * RiskRisk is intended for use while playing the game Risk.  It helps you
 * make informed decisions based on the true probabilities of certain
 * game-crucial events.  Enjoy.
 * 
 * @author Eric Stein <eastein@wpi.edu>
 * Copyright (c) 2005 Eric Stein.  Licensed under the GNU Public License, Version 2.
 * The GPL2 is distributed with this program as gpl.txt.
 */
public class RiskRisk {
	private static BattleOutcomeSet b;
	private static Scanner s = new Scanner(System.in);
	
	/**
	 * Interaction etc
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("RiskRisk Beta 1\nCopyright (C) 2005 Eric Stein");
		printHelp();
		String c;
		try {
			while (true) {
				try {
					boolean cmd = false;
					c = s.nextLine();
					if (c.equals("help")) {
						printHelp();
						cmd = true;
					} 
					if (c.equals("invade")) {
						getOutcome();
						System.out.println("End outcomes:");
						System.out.println(b);
						cmd = true;
					} 
					if (c.equals("format")) {
						System.out.println("Enter one of the following: fraction,decimal,both");
						c = s.nextLine();
						if (c.equals("fraction"))
							Fraction.default_print = Fraction.PRINT_FRACTION;
						if (c.equals("decimal"))
							Fraction.default_print = Fraction.PRINT_DECIMAL;
						if (c.equals("both"))
							Fraction.default_print = Fraction.PRINT_BOTH;
						cmd = true;
					} 
					if (c.equals("victory")) {
						getOutcome();
						System.out.println("Probability of successful invasion: " + b.probVictory(1).toString());
						cmd = true;
					} 
					if (c.equals("goodvictory")) {
						System.out.println("How many attacking armies (minimum) must survive the invasion?");
						int n = s.nextInt();
						getOutcome();
						System.out.println("Probability of successful invasion with " + n + " armies remaining (minimum): " + b.probVictory(n).toString());
						cmd = true;
					} 
					if (c.equals("quit")) {
						System.out.println("Terminating. Goodbye.");
						System.exit(0);
					} 
					if (c.equals("license")) {
						System.out.println("\nThis program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License version 2 as published by the Free Software Foundation.  The GPL is also included in this distribution as the file gpl.txt.\n\nThe sourcecode for this program should be included in the distribution.  If not, visit http://toba.ath.cx:724/.");
						cmd = true;
					} 
					if (c.equals("extraction")) {
						System.out.println("\nGNU/Linux: unzip riskrisk.jar\nWindows: rename riskrisk.jar to riskrisk.zip and open with WinZip or equivalent\nOther: the jar file is in zip format.  Find a utility which that can extract from zip and use it.");
						cmd = true;
					}
					if (c.equals("")) {
						cmd = true;
					}
					if (cmd == false) {
						System.out.println("No such command: " + c);
					}
				} catch (java.util.InputMismatchException e) {
					System.out.println("Bad input.");
				}
			}
		} catch (java.lang.OutOfMemoryError e) {
			System.out.println("RiskRisk has exhausted the allocated memory.  Use 'java -XmxXXXm -jar riskrisk.jar' where XXX is the number of megabytes to allocate.");
		}
	}
	
	/**
	 * Print program help to the standard out
	 */
	private static void printHelp() {
		System.out.println("\nVery large battles can take an extremely long time or cause the program to run out of memory.\nAll commands are typed without their arguments.  Arguments should be entered when requested.\n\nCommands:\n\thelp - displays this help screen\n\tinvade - shows all possible endgames and their exact probabilities\n\tformat - change numerical output settings\n\tvictory - calculate probability of victory\n\tgoodvictory - calculate probability of invasion with at least the given number of armies surviving\n\tquit - close RiskRisk\n\tlicense - licensing and legal information\n\textraction - information on how to extract source code and the GPL from the distribution");
	}
	
	/**
	 * User interaction for getting the outcome into the class variable b
	 */
	private static void getOutcome() {
		System.out.println("Enter attacking and defending army numbers:");
		int attackers = s.nextInt();
		int defenders = s.nextInt();
		System.out.println("Attacking Armies: " + attackers + " Defending Armies: " + defenders);
		b = Battle.predict(attackers, defenders);
	}
}
package puzzles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * A class that is part of a Ceaser Cipher-like system.
 * The program takes a single sequence of integers, unbroken 
 * by whitespace or non-integers, as input; it returns all the 
 * possible messages that have this sequence as their encoding.
 * 
 * The encoding works in the following manner:
 * 1 corresponds to "a", 2 corresponds to "b", and so on for a -- z.
 * 
 * 
 * <Algorithm>
 * The program works in 3 basic steps:
 * 	(1) Permute the input sequence such that all orderings of the integers
 * 		as single or paired-with-a-neighbor are found. Additionally, none of
 * 		the integers ever change places -- they only match or are standalone 
 * 		in comparison to their immediate neighbors,
 * 	(2) validate every found permutation -- some are going to be invalid. A
 * 		valid grouping of numbers to be translated to a message is one with 
 * 		all numbers in the range [1,26], inclusive,
 * 	(3) Translate all the valid sequences of numbers, and print out the possible
 * 		messages.
 * 
 * (example): Input Sequence "1234"
 * 
 * The algorithm takes the leftmost integer and steps right.
 * Each time it steps, it pairs and unpairs the elements to the
 * right of the current index. So, for this sequence, it builds the
 * permutations in the following manner:
 * 
 * 1 -- 2	3	4
 * 		23		4
 * 		2	34
 * 12 -- 3	4
 * 		 34
 * 1 23 -- 4
 * 1 2 34
 * (DONE)
 * One further note -- it is possible to have duplicate permutations this way.
 * Checks are made for having already seen a permutation before any translations
 * or validation checks are performed.
 * 
 * 
 * 
 * @author Bailey Malone
 * 			8/20/2012
 *
 */
public class NumToAlphaDecoding 
{
	// Alphas <--> Numbers Object
	private static HashMap<String, String> translator = new HashMap<String, String>();
	
	/**
	 * Does the actual decoding.
	 * INPUT: Unbroken sequence of digits representing an encoded
	 * 		String message
	 * OUTPUT: An ArrayList of all possible decodings of the input
	 * 		sequence of digits
	 * 
	 * @param digits
	 * @return
	 */
	private static ArrayList<String> decode(char[] digits)
	{
		ArrayList<String> possibleDecodings = new ArrayList<String>();

		// Build possible encodings from Left to Right
		int buildCount = 1;
		String encoding = Character.toString(digits[0]);

		// Main Loop -- building patterns one char at a time
		int buildIndex = 1; /* (We're starting AFTER the first digit passed as input) */
		while (buildCount < digits.length-1)
		{
			// HAVE NOT grouped to the end of the current iteration...
			/* Type 1 -- First run at this grouping --> add all as singles */
			String firstEncoding = encoding;
			for (int i = buildIndex; i < digits.length; i++)
			{
				firstEncoding += (" " + digits[i]);
			}
			if (!possibleDecodings.contains(firstEncoding.trim()))
				possibleDecodings.add(firstEncoding.trim());


			/* Type 2 -- Walk through and pair neighbors, adding */
			String secondEncoding = encoding;
			int groupIndex = buildIndex;
			for (int i = 0; i < (digits.length-buildIndex-1); i++)
			{
				if (groupIndex == (digits.length-1))
					break;
				
				// Loop through sub-array after buildIndex...
				for (int j = buildIndex; j < (digits.length-1); j++)
				{
					if (j == groupIndex) // We're grouping j and j+1
						secondEncoding += (" " + digits[j] + digits[j+1]);
					else	// We're just adding the next number
						secondEncoding += (" " + digits[j]);
				}
				
				if (!possibleDecodings.contains(secondEncoding.trim()))
					possibleDecodings.add(secondEncoding.trim());
				secondEncoding = encoding;
				groupIndex++;
			}

			// HAVE grouped to the end of the current iteration...
			String[] leftSide = encoding.split("");	encoding = "";
			for (String s : leftSide)
				if (s.compareTo("") != 0)
					encoding += (" " + s);
			encoding += digits[buildIndex++];
			
			buildCount++;
		}

		// Go through and validate any possible decodings before returning them
		for (int i = 0; i < possibleDecodings.size(); i++)
			if(!isValidNumSequence(possibleDecodings.get(i)))
				possibleDecodings.remove(i);

		return possibleDecodings;
	}

	/**
	 * Due to the manner in which the decoding algorithm works,
	 * sequences of numbers that are not valid (i.e., not all 
	 * numbers are in the range [1,26] inclusive).
	 * We do not want to try and decode these sequences, so we check
	 * for and remove all invalid finds.
	 * 
	 * @param seq
	 * @return -- true if all numbers in the sequence are in the range
	 * 		[1,26] inclusive; false otherwise
	 */
	private static boolean isValidNumSequence(String seq)
	{
		// Tokenize the sequence on whitespace to separate the numbers
		String[] tokens = seq.split(" ");
		
		// Check each number -- is it within [1,26] inclusive?
		for (String s : tokens)
		{
			/* DEBUG */
			//System.out.println("token --> " + s);
			
			Integer x = Integer.parseInt(s);
			if (x < 1 || x > 26)
				return false;
		}
		
		return true;
	}
	
	private static ArrayList<String> translate(ArrayList<String> decodings) 
	{
		ArrayList<String> msgs = new ArrayList<String>();
		
		String msg = "";
		for (String ciphertext : decodings)
		{
			String[] numbers = ciphertext.split(" ");
			for (String x : numbers)
			{
				msg += translator.get(x);
			}
			
			msgs.add(msg);
			msg = "";
		}
		
		return msgs;
	}
	
	private static void setupTranslator()
	{
		translator.put("1", "a");	translator.put("2", "b");	translator.put("3", "c");
		translator.put("4", "d");	translator.put("5", "e");	translator.put("6", "f");
		translator.put("7", "g");	translator.put("8", "h");	translator.put("9", "i");
		translator.put("10", "j");	translator.put("11", "k");	translator.put("12", "l");
		translator.put("13", "m");	translator.put("14", "n");	translator.put("15", "o");
		translator.put("16", "p");	translator.put("17", "q");	translator.put("18", "r");
		translator.put("19", "s");	translator.put("20", "t");	translator.put("21", "u");
		translator.put("22", "v");	translator.put("23", "w");	translator.put("24", "x");
		translator.put("25", "y");	translator.put("26", "z");
		translator.put("0", "");	translator.put("00", "");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// Initalize the map that translates numbers to letters
		setupTranslator();
		
		// Read in the digit sequence
		Scanner input = new Scanner(System.in);

		System.out.println("Enter the digit sequence you wish to decode: ");
		String digits = input.next();
		
		/* Make checks to see if invalid data has been etnered! */
		for (char c : digits.toCharArray())
		{
			if (Character.isLetter(c) || Character.isSpace(c))
			{
				System.err.println("Invalid Input! A single sequence of digits unbroken by whitespace, please!");
				System.exit(1);
			}
		}

		
		// Get the decodings
		// First, convert the input to a char array representing the numbers
		char[] charNumbers = digits.toCharArray();
		ArrayList<String> decodings = decode(charNumbers);
		ArrayList<String> messages = translate(decodings);


		// Print out the list of possible decodings
		System.out.println("\n\nAll Possible Encoded Messages:\n");
		for (String s : messages)
			System.out.println(s);
	}
}

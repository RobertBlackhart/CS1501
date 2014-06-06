/**
 * Name: Robert McDermot
 * Date: 9/2/13
 * Time: 9:53 AM
 */

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ComputeDistanceBetween
{
	public static void main(String[] args)
	{
		Stopwatch stopwatch = new Stopwatch();

		if(args.length != 2)
		{
			System.out.println("Usage: java ComputeDistanceBetween filename_1 filename_2");
			System.exit(-1);
		}

		ArrayList<WordFreqPair> wordList1 = getWordList(new File(args[0]));
		ArrayList <WordFreqPair> wordList2 = getWordList(new File(args[1]));

		DecimalFormat df = new DecimalFormat("0.000000");
		System.out.println("The distance between the documents is: " + df.format(getRadians(wordList1,wordList2)) + " radians");
		System.out.println("Time elapsed: " + stopwatch.elapsedTime() + " seconds");
	}

	private static ArrayList<WordFreqPair> getWordList(File file)
	{
		int numLines = 0, numWords = 0;
		Scanner fileScanner;
		ArrayList<WordFreqPair> wordList = new ArrayList<WordFreqPair>();
		HashMap<String,WordFreqPair> hashMap = new HashMap<String,WordFreqPair>();

		try
		{
			fileScanner = new Scanner(file);

			while(fileScanner.hasNextLine())
			{
				numLines++;
				ArrayList<String> words = getWords(fileScanner.nextLine());

				for(String word : words)
				{
					if(word == null || word.equals("")) //ignore empties
						continue;

					numWords++;
					word = word.toLowerCase();
					//HashMap containsKey() has O(1) access instead of ArrayList's contains() which has O(n)
					if(!hashMap.containsKey(word))
						hashMap.put(word,new WordFreqPair(word,1));
					else
						hashMap.get(word).freq++;
				}
			}

			wordList.addAll(hashMap.values());
			quicksort(wordList,0,wordList.size()-1);
		}
		catch(FileNotFoundException e)
		{
			System.out.println(file.getName() + " is not found!");
		}
		finally
		{
			System.out.println("File " + file.getName() + ": " + numLines + " lines, " + numWords + " words, " + wordList.size() + " distinct words");
		}

		return wordList;
	}

	private static double getRadians(ArrayList<WordFreqPair> list1, ArrayList<WordFreqPair> list2)
	{
		double radians, normX = 0, normY = 0, innerProduct = 0;
		HashMap<String,WordFreqPair> hashMap = new HashMap<String, WordFreqPair>();
		for(WordFreqPair pair : list2) //store list2 in a hashmap because we will be asking if it contains values from list 1 often
			hashMap.put(pair.word,pair);

		for(WordFreqPair pair : list1)
		{
			normX += pair.freq*pair.freq;
			if(hashMap.containsKey(pair.word))
			{
				//so we don't have to run through the lists twice, calculate the inner product at the same time as part of list2's norm
				WordFreqPair pair2 = hashMap.get(pair.word);
				innerProduct += pair.freq*pair2.freq;
				normY += pair2.freq*pair2.freq;
				hashMap.remove(pair.word); //remove the elements that are the same so that the norm doesn't get unduly inflated
			}
		}
		normX = Math.sqrt(normX);
		for(WordFreqPair pair : hashMap.values()) //go through the remaining values in list2
		{
			normY += pair.freq*pair.freq;
		}
		normY = Math.sqrt(normY);

		radians = Math.acos(innerProduct/(normX*normY));

		return radians;
	}

	//serves the same function as line.split("[^a-zA-Z0-9]");
	//splits on any non alphanumeric character
	private static ArrayList<String> getWords(String line)
	{
		ArrayList<String> words = new ArrayList<String>();
		StringBuilder word = new StringBuilder();

		for(int i=0; i<line.length(); i++)
		{
			char c = line.charAt(i);
			if(c < 48 || (c > 57 && c < 65) || (c > 90 && c < 97) || c > 122)
			{
				words.add(word.toString());
				word.setLength(0);
			}
			else
				word.append(c);
		}

		words.add(word.toString());

		return words;
	}

	private static void quicksort(ArrayList<WordFreqPair> array, int left, int right)
	{
		// If the list has 2 or more items
		if(left < right)
		{
			int pivotIndex = left+(right-left)/2;

			// Get lists of bigger and smaller items and final position of pivot
			pivotIndex = partition(array, left, right, pivotIndex);

			// Recursively sort elements smaller than the pivot
			quicksort(array, left, pivotIndex - 1);

			// Recursively sort elements at least as big as the pivot
			quicksort(array, pivotIndex + 1, right);
		}
	}

	private static int partition(ArrayList<WordFreqPair> array, int left, int right, int pivotIndex)
	{
		WordFreqPair pivotValue = array.get(pivotIndex);
		WordFreqPair temp = array.get(right);
		array.set(right,pivotValue);
		array.set(pivotIndex,temp); //move pivot to end

		int storeIndex = left;
		for(int i = left; i < right; i++) // left ≤ i < right
		{
			if(array.get(i).compareTo(pivotValue) < 0)
			{
				temp = array.get(i);
				array.set(i,array.get(storeIndex));
				array.set(storeIndex,temp);
				storeIndex++;
			}
		}

		temp = array.get(storeIndex);
		array.set(storeIndex,array.get(right));
		array.set(right,temp); // Move pivot to its final place
		return storeIndex;
	}
}

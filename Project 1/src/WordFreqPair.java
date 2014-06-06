/**
 * Name: Robert McDermot
 * Date: 9/2/13
 * Time: 10:38 AM
 */
public class WordFreqPair implements Comparable<WordFreqPair>
{
	String word;
	int freq;

	public WordFreqPair(String word, int freq)
	{
		this.word = word;
		this.freq = freq;
	}

	@Override
	public int compareTo(WordFreqPair o)
	{
		return o.word.compareTo(this.word);
	}

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof WordFreqPair)
			return ((WordFreqPair)o).word.equals(this.word);
		else
			return false;
	}

	@Override
	public int hashCode()
	{
		return word.hashCode();
	}
}
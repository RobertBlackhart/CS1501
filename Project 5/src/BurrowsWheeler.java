/**
 * Name:  Robert McDermot
 * Email: rom66@pitt.edu
 * ID #:  ***2800
 * Date:  11/25/13
 */
public class BurrowsWheeler
{
	// apply Burrows-Wheeler encoding, reading from standard input and writing to standard output	- 20 points
	public static void encode()
	{

	}

	// apply Burrows-Wheeler decoding, reading from standard input and writing to standard output	- 20 points
	public static void decode()
	{

	}

	// if args[0] is '-', apply Burrows-Wheeler encoding   - 5 points
	// if args[0] is '+', apply Burrows-Wheeler decoding   - 5 points
	public static void main(String[] args)
	{
		//Check args length
		if(args.length == 0)
		{
			System.out.println("Usage (encode): java BurrowsWheeler - < file.txt\nUsage (decode): java BurrowsWheeler + < file.txt");
			return;
		}

		//Check for +/-
		if(args[0].equals("-"))
		{
			encode();
		}
		else if(args[0].equals("+"))
		{
			decode();
		}
		else
		{
			System.out.println("Usage (encode): java BurrowsWheeler - < file.txt\nUsage (decode): java BurrowsWheeler + < file.txt");
			return;
		}
	}
}

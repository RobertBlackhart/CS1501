/**
 * Name:  Robert McDermot
 * Email: rom66@pitt.edu
 * ID #:  ***2800
 * Date:  11/25/13
 */
public class MoveToFront
{
	private static char[] sequence = new char[256]; //Alphabet Sequence (extended ASCII)

	// apply move-to-front encoding, reading from standard input and writing to standard output - 20 points
	public static void encode()
	{
		initSequence();

		while(!BinaryStdIn.isEmpty()) //while input remains
		{
			char c = BinaryStdIn.readChar();

			//Search for it in the array
			for(int i = 0; i < 256; i++)
			{
				if(c == sequence[i])
				{
					BinaryStdOut.write((byte) i);

					//Move character at index i to the front of sequence
					for(; i > 0; i--)
					{
						sequence[i] = sequence[i - 1]; //Move all to the right
					}
					sequence[0] = c; //Place byte in front

					break; //short-circuit
				}
			}
		}

		BinaryStdIn.close();
		BinaryStdOut.close();
	}

	// apply move-to-front decoding, reading from standard input and writing to standard output - 20 points
	public static void decode()
	{
		initSequence();

		while(!BinaryStdIn.isEmpty()) //while input remains
		{
			char index = BinaryStdIn.readChar();
			char c = sequence[index];
			BinaryStdOut.write(c);

			//Move character at index to the front of sequence
			for(; index > 0; index--)
			{
				sequence[index] = sequence[index - 1]; //Move all to the right
			}
			sequence[0] = c; //Place byte in front

		}

		BinaryStdIn.close();
		BinaryStdOut.close();
	}

	private static void initSequence()
	{
		//initialize sequence array with alphabet
		for(int i = 0; i < 256; i++)
		{
			sequence[i] = (char) i;
		}
	}

	// if args[0] is '-', apply move-to-front encoding - 5 points
	// if args[0] is '+', apply move-to-front decoding - 5 points
	public static void main(String[] args)
	{

		//Check args length
		if(args.length == 0)
		{
			System.out.println("Usage (encode): java MoveToFront - < file.txt\nUsage (decode): java MoveToFront + < file.txt");
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
			System.out.println("Usage (encode): java MoveToFront - < file.txt\nUsage (decode): java MoveToFront + < file.txt");
			return;
		}

	}
}
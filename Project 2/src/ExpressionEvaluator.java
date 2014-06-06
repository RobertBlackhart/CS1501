import java.io.*;
import java.util.Scanner;

/**
 * Name:  Robert McDermot
 * Email: rom66@pitt.edu
 * ID #:  ***2800
 * Date:  9/11/13
 */
public class ExpressionEvaluator
{
	public static void main(String[] args)
	{
		if(args.length != 1)
		{
			System.out.println("use: java ExpressionEvaluator [file]");
			System.exit(-1);
		}

		File evalFile = null;
		try
		{
			evalFile = new File(args[0]);
		}
		catch(NullPointerException ex)
		{
			System.out.println("Unable to read \""+args[0]+"\". Exiting...");
			System.exit(-1);
		}
		try
		{
			boolean afterAssignments = false;
			Scanner fileScanner = new Scanner(evalFile);
			int lineNumber = 1;
			while(fileScanner.hasNextLine())
			{
				String line = fileScanner.nextLine();
				if(Expression.isAtomAssignment(line))
				{
					String[] parts = line.split(" ");
					Expression.setAtom(parts[0],parts[1]);
					System.out.print(parts[0] + " = " + parts[1] +"\t");
				}
				else
				{
					if(!afterAssignments)
					{
						afterAssignments = true;
						System.out.println();
					}
					try
					{
						Expression expression = new Expression(line);
						System.out.println(lineNumber + ". " + expression + " = " + expression.evaluate());
						lineNumber++;
						String input = "";
						BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
						System.out.print("Continue? y or n : ");
						input = br.readLine();
						if(!input.equals("y"))
							System.exit(0);
					}
					catch(ParseError parseError)
					{
						System.err.println(parseError.getMessage()+"\nGet next expression");
					}
				}
			}

			System.out.println("EOF reached");
			System.exit(0);
		}
		catch(FileNotFoundException ex)
		{
			ex.printStackTrace();
		}
		catch(IOException e)
		{
			System.err.println("Unable to read input.");
		}
	}
}
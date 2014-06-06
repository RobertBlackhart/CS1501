/**
 * Name:  Robert McDermot
 * Email: rom66@pitt.edu
 * ID #:  ***2800
 * Date:  9/11/13
 */
public class ParseError extends Exception
{
	public ParseError(String message)
	{
		super(message);
	}

	public ParseError(String message, Throwable throwable)
	{
		super(message,throwable);
	}
}

/**
 * Name:  Robert McDermot
 * Email: rom66@pitt.edu
 * ID #:  ***2800
 * Date:  9/17/13
 */
public class Node
{
	public String symbol;
	public Node left, right;

	public Node(String s)
	{
		symbol = s;
		left = null;
		right = null;
	}
	public Node(String s, Node l, Node r)
	{
		symbol = s;
		left = l;
		right = r;
	}
}

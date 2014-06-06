/**
 * Name:  Robert McDermot
 * Email: rom66@pitt.edu
 * ID #:  ***2800
 * Date:  10/31/13
 */
public class Edge
{
	Vertex left, right;
	double weight;

	public boolean equals(Object o)
	{
		if(o instanceof Edge)
		{
			Edge other = (Edge)o;
			return ((other.left.equals(left) && other.right.equals(right)) || (other.right.equals(left) && other.left.equals(right)));
		}
		else
			return false;
	}
}
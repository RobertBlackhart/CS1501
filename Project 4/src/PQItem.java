/**
 * Name:  Robert McDermot
 * Email: rom66@pitt.edu
 * ID #:  ***2800
 * Date:  11/4/13
 */
public class PQItem implements Comparable<PQItem>
{
	Vertex vertex;
	double distance;

	public PQItem(Vertex v, double d)
	{
		vertex = v;
		distance = d;
	}

	@Override
	public int compareTo(PQItem o)
	{
		return (int) (distance-o.distance);
	}

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof PQItem)
		{
			PQItem other = (PQItem)o;
			return other.vertex.equals(vertex);
		}
		else
			return false;
	}
}

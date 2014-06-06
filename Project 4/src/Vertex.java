/**
 * Name:  Robert McDermot
 * Email: rom66@pitt.edu
 * ID #:  ***2800
 * Date:  10/31/13
 */
public class Vertex implements Comparable<Vertex>
{
	int index;
	boolean visited;

	public Vertex(int i)
	{
		index = i;
	}

	public boolean isVisited()
	{
		return visited;
	}

	public void setVisited(boolean visited)
	{
		this.visited = visited;
	}

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Vertex)
		{
			Vertex other = (Vertex)o;
			if(other.index == index)
				return true;
		}
		return false;
	}

	@Override
	public int compareTo(Vertex other)
	{
		return index-other.index;
	}
}
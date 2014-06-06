import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Name:  Robert McDermot
 * Email: rom66@pitt.edu
 * ID #:  ***2800
 * Date:  10/29/13
 */
public class Graph
{
	private ArrayList<Edge> edges;
	private ArrayList<Vertex> vertices;
	private int numVertices, numEdges;

	public Graph(File file)
	{
		try
		{
			Scanner scanner = new Scanner(file);
			setNumVertices(Integer.valueOf(scanner.nextLine()));
			setNumEdges(Integer.valueOf(scanner.nextLine()));

			edges = new ArrayList<Edge>();
			vertices = new ArrayList<Vertex>();

			while(scanner.hasNextLine())
			{
				Scanner lineScanner = new Scanner(scanner.nextLine());
				Vertex leftNode = new Vertex(lineScanner.nextInt());
				Vertex rightNode = new Vertex(lineScanner.nextInt());
				int weight = lineScanner.nextInt();

				Edge edge = new Edge();
				edge.left = leftNode;
				edge.right = rightNode;
				edge.weight = weight;
				edges.add(edge);

				if(!vertices.contains(leftNode))
					vertices.add(leftNode);
				if(!vertices.contains(rightNode))
					vertices.add(rightNode);
			}
		}
		catch(Exception ex)
		{
			System.err.println("Error while parsing input file");
			ex.printStackTrace();
			System.exit(-1);
		}
	}

	public ArrayList<Edge> getEdgesOfVertex(Vertex vertex)
	{
		ArrayList<Edge> returnList = new ArrayList<Edge>();

		for(Edge edge : getEdges())
		{
			if(edge.left.equals(vertex) || edge.right.equals(vertex))
				returnList.add(edge);
		}

		return returnList;
	}

	public ArrayList<ArrayList<Vertex>> getConnectedComponents()
	{
		ArrayList<ArrayList<Vertex>> connectedComponents = new ArrayList<ArrayList<Vertex>>();
		for(Vertex vertex : getVertices())
			vertex.setVisited(false);

		while(true)
		{
			Vertex startVertex = null;
			for(Vertex vertex : getVertices())
			{
				if(!vertex.isVisited())
				{
					startVertex = vertex;
					break;
				}
			}

			if(startVertex == null)
				break;

			connectedComponents.add(new ArrayList<Vertex>());
			dfs(startVertex, connectedComponents.get(connectedComponents.size()-1));

			int count = 0;
			for(ArrayList<Vertex> component : connectedComponents)
				count += component.size();

			if(count == numVertices)
				break;
		}

		return connectedComponents;
	}

	private void dfs(Vertex vertex, ArrayList<Vertex> components)
	{
		vertex.setVisited(true);
		components.add(vertex);
		for(Edge edge : getEdgesOfVertex(vertex))
		{
			if(!edge.left.isVisited() && !components.contains(edge.left))
				dfs(edge.left,components);
			if(!edge.right.isVisited() && !components.contains(edge.right))
				dfs(edge.right,components);
		}
	}

	public void removeEdge(Edge edge)
	{
		getEdges().remove(edge);
		numEdges--;
	}

	public void insertEdge(Edge edge)
	{
		getEdges().add(edge);
		numEdges++;
	}

	/* The function returns one of the following values
   0 --> If grpah is not Eulerian
   1 --> If graph has an Euler path (Semi-Eulerian)
   2 --> If graph has an Euler Circuit (Eulerian)  */
	public int isEulerian()
	{
		// Check if all vertices are connected
		ArrayList<ArrayList<Vertex>> components = getConnectedComponents();
		if(components.size() != 1)
			return 0;

		// Count vertices with odd degree
		int odd = 0;
		for(Vertex v : components.get(0))
		{
			if(getEdgesOfVertex(v).size() % 2 != 0)
				odd++;
		}

		// If count is more than 2, then graph is not Eulerian
		if (odd > 2)
			return 0;

		// If odd count is 2, then semi-eulerian.
		// If odd count is 0, then eulerian
		// Note that odd count can never be 1 for undirected graph
		return (odd == 2) ? 1 : 2;
	}

	public ArrayList<Vertex> getVertices()
	{
		return vertices;
	}

	public void setVertices(ArrayList<Vertex> vertices)
	{
		this.vertices = vertices;
	}

	public ArrayList<Edge> getEdges()
	{
		return edges;
	}

	public void setEdges(ArrayList<Edge> edges)
	{
		this.edges = edges;
	}

	public int getNumVertices()
	{
		return numVertices;
	}

	public void setNumVertices(int numVertices)
	{
		this.numVertices = numVertices;
	}

	public int getNumEdges()
	{
		return numEdges;
	}

	public void setNumEdges(int numEdges)
	{
		this.numEdges = numEdges;
	}
}

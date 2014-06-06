import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Name:  Robert McDermot
 * Email: rom66@pitt.edu
 * ID #:  ***2800
 * Date:  10/29/13
 */
public class hw4_rom66
{
	static Graph graph;
	static DecimalFormat formatter = new DecimalFormat("#.00");
	static DecimalFormat formatter4 = new DecimalFormat("#.0000");

	public static void main(String[] args)
	{
		if(args.length != 1)
		{
			System.err.println("Usage: java hw4_rom66 [input_file]");
			System.exit(-1);
		}

		File file = new File(args[0]);
		graph = new Graph(file);

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while(true)
		{
			System.out.print("1. R (eport)\n2. M (inimum Spanning Tree)\n3. S (hortest Path from) i j\n4. D (own edge) i j\n5. U (p edge) i j\n6. C (hange weight of edge) i j x\n7. E (ulerian)\n8. Q (uit))\n\nEnter your choice: ");
			try
			{
				String input = br.readLine();

				if(input.equals("1") || input.toLowerCase().equals("r") || input.toLowerCase().equals("report"))
					report();
				if(input.equals("2") || input.toLowerCase().equals("m") || input.toLowerCase().equals("minimum spanning tree"))
					mst();
				if(input.equals("3") || input.toLowerCase().equals("s") || input.toLowerCase().equals("shortest path from"))
				{
					int fromVertex, toVertex;
					System.out.print("Enter from vertex: ");
					fromVertex = Integer.parseInt(br.readLine());
					System.out.print("Enter to vertex: ");
					toVertex = Integer.parseInt(br.readLine());
					shortestPath(fromVertex,toVertex);
				}
				if(input.equals("4") || input.toLowerCase().equals("d") || input.toLowerCase().equals("down edge"))
				{
					int fromVertex, toVertex;
					System.out.print("Enter from vertex: ");
					fromVertex = Integer.parseInt(br.readLine());
					System.out.print("Enter to vertex: ");
					toVertex = Integer.parseInt(br.readLine());
					Edge edge = new Edge();
					edge.left = new Vertex(fromVertex);
					edge.right = new Vertex(toVertex);
					graph.removeEdge(edge);
					System.out.println("Remove edge " + fromVertex + "->" + toVertex + "\n");
				}
				if(input.equals("5") || input.toLowerCase().equals("u") || input.toLowerCase().equals("up edge"))
				{
					int fromVertex, toVertex;
					double weight;
					System.out.print("Enter from vertex: ");
					fromVertex = Integer.parseInt(br.readLine());
					System.out.print("Enter to vertex: ");
					toVertex = Integer.parseInt(br.readLine());
					System.out.print("Enter weight: ");
					weight = Double.parseDouble(br.readLine());
					Edge edge = new Edge();
					edge.left = new Vertex(fromVertex);
					edge.right = new Vertex(toVertex);
					edge.weight = weight;
					graph.insertEdge(edge);
					System.out.println("Insert edge " + fromVertex + "->" + toVertex + " (" + weight + ")\n");
				}
				if(input.equals("6") || input.toLowerCase().equals("c") || input.toLowerCase().equals("change weight of edge"))
				{
					int fromVertex, toVertex;
					double weight;
					System.out.print("Enter from vertex: ");
					fromVertex = Integer.parseInt(br.readLine());
					System.out.print("Enter to vertex: ");
					toVertex = Integer.parseInt(br.readLine());
					System.out.print("Enter to weight: ");
					weight = Double.parseDouble(br.readLine());
					double origWeight = Double.NaN;
					for(Edge edge : graph.getEdges())
					{
						if(edge.left.equals(new Vertex(fromVertex)) && edge.right.equals(new Vertex(toVertex)))
						{
							origWeight = edge.weight;
							edge.weight = weight;
						}
						if(edge.right.equals(new Vertex(toVertex)) && edge.left.equals(new Vertex(fromVertex)))
						{
							origWeight = edge.weight;
							edge.weight = weight;
						}
					}
					System.out.println("Change edge " + fromVertex + "->" + toVertex + " from " + origWeight + " to " + weight + "\n");
				}
				if(input.equals("7") || input.toLowerCase().equals("e") || input.toLowerCase().equals("eulerian"))
				{
					printEulerTour();
				}
				if(input.equals("8") || input.toLowerCase().equals("q") || input.toLowerCase().equals("quit"))
					System.exit(0);
			}
			catch(IOException e)
			{
				System.err.println("Could not read from standard input. Exiting.");
				System.exit(-1);
			}
		}
	}

	public static void report()
	{
		System.out.println(graph.getNumVertices() + " " + graph.getNumEdges());
		Collections.sort(graph.getVertices());
		for(Vertex vertex : graph.getVertices())
		{
			System.out.print(vertex.index+": ");
			for(Edge edge : graph.getEdgesOfVertex(vertex))
			{
				System.out.print(edge.left.index+"-"+edge.right.index+"  "+formatter.format(edge.weight)+"  ");
			}
			System.out.println();
		}

		System.out.println();

		ArrayList<ArrayList<Vertex>> components = graph.getConnectedComponents();
		if(components.size() == 1)
			System.out.println("Network is connected");
		else
			System.out.println("Network is disconnected");

		System.out.println("Connected Components");
		for(int i=0; i<components.size(); i++)
		{
			System.out.print(" " + (i + 1) + ".  ");
			for(Vertex vertex : components.get(i))
				System.out.print(vertex.index + " ");
			System.out.println();
		}

		System.out.println();
	}

	public static void mst()
	{
		Edge[] edgeTo = new Edge[graph.getNumVertices()];
		double[] distTo = new double[graph.getNumVertices()];
		for(int i=0; i<distTo.length; i++)
			distTo[i] = Double.MAX_VALUE;
		boolean[] marked = new boolean[graph.getNumVertices()];
		PriorityQueue<PQItem> pq = new PriorityQueue<PQItem>();

		distTo[0] = 0;
		pq.add(new PQItem(graph.getVertices().get(0),distTo[0]));

		while(!pq.isEmpty())
		{
			Vertex v = pq.remove().vertex;
			scan(v,edgeTo,distTo,marked,pq);
		}

		double weight = 0;
		for(Edge e : edgeTo)
		{
			if(e != null)
			{
				weight += e.weight;
				System.out.println(e.left.index + "-" + e.right.index + " " + formatter.format(e.weight));
			}
		}
		System.out.println(formatter4.format(weight)+"\n");
	}

	private static void scan(Vertex v, Edge[] edgeTo, double[] distTo, boolean[] marked, PriorityQueue<PQItem> pq)
	{
		marked[v.index] = true;
		for(Edge e : graph.getEdgesOfVertex(v))
		{
			Vertex w;
			if(e.left.equals(v))
				w = e.right;
			else
				w = e.left;

			if(marked[w.index])
				continue;
			if(e.weight < distTo[w.index])
			{
				distTo[w.index] = e.weight;
				edgeTo[w.index] = e;

				if(pq.contains(new PQItem(w,distTo[w.index])))
					pq.remove(new PQItem(w, distTo[w.index]));
				pq.add(new PQItem(w,distTo[w.index]));
			}
		}
	}

	static public void shortestPath(int fromVertex, int toVertex)
	{
		System.out.println("Shortest path from " + fromVertex + " to " + toVertex + " is:");
		Edge[] edgeTo = new Edge[graph.getNumVertices()];
		edgeTo[fromVertex] = new Edge();
		double[] distTo = new double[graph.getNumVertices()];
		for(int i=0; i<distTo.length; i++)
			distTo[i] = Double.MAX_VALUE;
		distTo[fromVertex] = 0;
		PriorityQueue<PQItem> pq = new PriorityQueue<PQItem>();
		pq.add(new PQItem(new Vertex(fromVertex),0));

		while(!pq.isEmpty())
		{
			Vertex v = pq.remove().vertex;
			for(Edge e : graph.getEdgesOfVertex(v))
			{
				relax(v,e,distTo,edgeTo,pq);
			}
		}
		Stack<Edge> edgeStack = new Stack<Edge>();
		edgeStack.push(edgeTo[toVertex]);
		if(edgeStack.peek() == null)
		{
			System.out.println("No path\n");
			return;
		}
		double total = edgeStack.peek().weight;
		while(true)
		{
			if(edgeStack.peek().weight == 0)
				break;
			edgeStack.push(edgeTo[edgeStack.peek().right.index]);
			total += edgeStack.peek().weight;
		}
		System.out.print("("+formatter.format(total)+")  ");
		while(!edgeStack.isEmpty())
		{
			Edge edge = edgeStack.pop();
			if(edge.left != null && edge.right != null)
				System.out.print(edge.right.index+"->"+edge.left.index+"  "+formatter.format(edge.weight)+"   ");
		}
		System.out.println("\n");
	}

	static private void relax(Vertex v, Edge e, double[] distTo, Edge[] edgeTo, PriorityQueue<PQItem> pq)
	{
		Vertex left,right;
		if(e.left.equals(v))
		{
			left = e.left;
			right = e.right;
		}
		else
		{
			left = e.right;
			right = e.left;
		}
		e.right = right;
		e.left = left;
		if(distTo[right.index] > distTo[left.index]+e.weight)
		{
			distTo[right.index] = distTo[left.index] + e.weight;
			edgeTo[right.index] = e;

			if(pq.contains(new PQItem(right,distTo[right.index])))
				pq.remove(new PQItem(right, distTo[right.index]));
			pq.add(new PQItem(right,distTo[right.index]));
		}
	}

	public static void printEulerTour()
	{
		Vertex current = null;
		String pathCircuit = "path";

		if(graph.isEulerian() == 1)
		{
			System.out.println("Graph has a Eulerian path.");
			for(Vertex v : graph.getConnectedComponents().get(0))
			{
				if(graph.getEdgesOfVertex(v).size() % 2 != 0)
				{
					current = v;
					break;
				}
			}
		}
		else if(graph.isEulerian() == 2)
		{
			System.out.println("Graph has a Eulerian circuit.");
			pathCircuit = "circuit";
			current = new Vertex(0);
		}
		else
		{
			System.out.println("Graph has no Eulerian circuit or path\n");
			return;
		}

		Stack<Vertex> stack = new Stack<Vertex>();
		ArrayList<Vertex> tour = new ArrayList<Vertex>();
		ArrayList<Edge> edgesLeft = new ArrayList<Edge>();
		for(Edge e : graph.getEdges())
			edgesLeft.add(e);

		while(true)
		{
			int neighbors = 0;
			for(Edge e : edgesLeft)
			{
				if(e.left.equals(current) || e.right.equals(current))
				{
					neighbors++;
				}
			}
			if(neighbors > 0)
			{
				stack.push(current);
				for(int i=0; i<edgesLeft.size(); i++)
				{
					Edge e = edgesLeft.get(i);
					if(e.left.equals(current) || e.right.equals(current))
					{
						if(e.left.equals(current))
							current = e.right;
						else
							current = e.left;
						edgesLeft.remove(i);
						break;
					}
				}
			}
			else
			{
				tour.add(current);

				if(stack.isEmpty())
					break;
				current = stack.pop();
			}
		}

		System.out.print("Eulerian " + pathCircuit + ": ");
		for(int i=0; i<tour.size(); i++)
		{
			if(i < tour.size()-1)
				System.out.print(tour.get(i).index + " - ");
			else
				System.out.print(tour.get(i).index);
		}
		System.out.println("\n");
	}
}
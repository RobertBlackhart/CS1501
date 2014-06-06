import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Name:  Robert McDermot
 * Email: rom66@pitt.edu
 * ID #:  ***2800
 * Date:  9/26/13
 */
public class Solver
{
	private Board initialBoard;
	private ArrayList<Board> solution = new ArrayList<Board>();

	public Solver(Board initial)
	{
		initialBoard = initial;
		if(isSolvable())
		{
			PriorityQueue<SearchNode> pq = new PriorityQueue<SearchNode>();
			HashMap<String,SearchNode> closedMap = new HashMap<String, SearchNode>();
			SearchNode initialNode = new SearchNode(initialBoard, 0, null);
			pq.add(initialNode);
			while(!pq.isEmpty())
			{
				SearchNode current = pq.remove();
				if(current.board.isGoal())
				{
					while(current.previous != null)
					{
						solution.add(0,current.board);
						current = current.previous;
					}
					break;
				}

				closedMap.put(current.board.toString(), current);

				for(Board neighbor : current.board.neighbors())
				{
					SearchNode successor = new SearchNode(neighbor,current.numMoves+1,current);

					if(!closedMap.containsKey(neighbor.toString()))
						pq.add(successor);
				}
			}
		}
		//find a solution to the initial board (using A*)
	}

	public boolean isSolvable()
	{
		return initialBoard.isSolvable();
	}

	public int moves()
	{
		//min number of moves to solve initial board
		return solution.size();
	}

	public Iterable<Board> solution()
	{
		//sequence of boards in a shortest solution
		return solution;
	}

	public static void main(String[] args)
	{
		// create initial board from file
		In in = new In(args[0]);
		int N = in.readInt();
		int[][] blocks = new int[N][N];

		for(int i = 0; i < N; i++)
		{
			for(int j = 0; j < N; j++)
			{
				blocks[i][j] = in.readInt();
			}
		}

		Board initial = new Board(blocks);      // solve the puzzle
		Solver solver = new Solver(initial);    // print solution to standard output

		if(!initial.isSolvable())
		{
			System.out.println("No solution possible");
		}
		else
		{
			System.out.println("Minimum number of moves = " + solver.moves());

			for(Board board : solver.solution())
			{
				System.out.println(board);
			}
		}
	}

	private class SearchNode implements Comparable<SearchNode>
	{
		Board board;
		int numMoves;
		SearchNode previous;
		int cost;

		public SearchNode(Board b, int m, SearchNode p)
		{
			board = b;
			numMoves = m;
			previous = p;
			cost = board.manhattan()+numMoves;
		}

		@Override
		public int compareTo(SearchNode o)
		{
			return cost-o.cost;
		}
	}
}

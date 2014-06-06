import java.util.ArrayList;

/**
 * Name:  Robert McDermot
 * Email: rom66@pitt.edu
 * ID #:  ***2800
 * Date:  9/26/13
 */
public class Board
{
	private int[][] blocks;
	private int dimension;

	public Board(int[][] blocks)
	{
		this.blocks = blocks;
		dimension = blocks.length;
		//construct a board from an N by N array of blocks
		// (where blocks[i][j] = block in row i, column j
	}

	public int dimension()
	{
		return dimension;
	}

	public int hamming()
	{
		//number of blocks out of position from goal state
		int hamming = 0;
		for(int i = 0; i < dimension; i++)
		{
			for(int j = 0; j < dimension; j++)
			{
				if(blocks[i][j] != 0 && blocks[i][j] != i * dimension + j + 1)
				{
					hamming++;
				}
			}
		}
		return hamming;
	}

	public int manhattan()
	{
		int manhattanDistanceSum = 0;
		for(int x = 0; x < dimension; x++) // x-dimension, traversing rows (i)
		{
			for(int y = 0; y < dimension; y++)
			{ // y-dimension, traversing cols (j)
				int value = blocks[x][y]; // tiles array contains board elements
				if(value != 0)
				{ // we don't compute MD for element 0
					int targetX = (value - 1) / dimension; // expected x-coordinate (row)
					int targetY = (value - 1) % dimension; // expected y-coordinate (col)
					int dx = x - targetX; // x-distance to expected coordinate
					int dy = y - targetY; // y-distance to expected coordinate
					manhattanDistanceSum += Math.abs(dx) + Math.abs(dy);
				}
			}
		}
		return manhattanDistanceSum;
	}

	public boolean isGoal()
	{
		int[][] goal = new int[dimension][dimension];
		int num = 1;
		for(int i = 0; i < dimension; i++)
		{
			for(int j = 0; j < dimension; j++)
			{
				goal[i][j] = num;
				num++;
			}
		}
		goal[dimension - 1][dimension - 1] = 0;
		Board goalBoard = new Board(goal);
		return this.equals(goalBoard);
	}

	public boolean isSolvable()
	{
		int inversions = 0;
		int blankRowFromBottom = 0;
		int[] oneDArray = new int[dimension * dimension];
		for(int i = 0; i < dimension; i++)
		{
			for(int j = 0; j < dimension; j++)
			{
				oneDArray[i * dimension + j] = blocks[i][j];
				if(blocks[i][j] == 0)
				{
					blankRowFromBottom = dimension - 1 - i;
				}
			}
		}
		for(int i = 0; i < oneDArray.length; i++)
		{
			for(int j = i; j < oneDArray.length; j++)
			{
				if(oneDArray[j] != 0 && oneDArray[i] > oneDArray[j])
				{
					inversions++;
				}
			}
		}

		boolean solvable = false;
		if(dimension % 2 != 0) //grid width is odd
		{
			if(inversions % 2 == 0)
			{
				solvable = true;
			}
		}
		else
		{
			if(blankRowFromBottom % 2 != 0) //second to last row, 4th to last row...
			{
				if(inversions % 2 != 0)
				{
					solvable = true;
				}
			}
			else //last row, third to last row...
			{
				if(inversions % 2 == 0)
				{
					solvable = true;
				}
			}
		}
		return solvable;
	}

	@Override
	public boolean equals(Object other)
	{
		if(other instanceof Board)
		{
			Board otherBoard = (Board) other;
			if(dimension == otherBoard.dimension)
			{
				for(int i = 0; i < dimension; i++)
				{
					for(int j = 0; j < dimension; j++)
					{
						if(blocks[i][j] != otherBoard.blocks[i][j])
						{
							return false;
						}
					}
				}
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	public Iterable<Board> neighbors()
	{
		ArrayList<Board> neighbors = new ArrayList<Board>();
		int x = -1, y = -1;
		for(int i = 0; i < dimension; i++)
		{
			for(int j = 0; j < dimension; j++)
			{
				if(blocks[i][j] == 0)
				{
					x = i;
					y = j;
				}
			}
		}

		if(x > 0)
		{
			int[][] copy = copyArray();
			int temp = copy[x - 1][y];
			copy[x - 1][y] = 0;
			copy[x][y] = temp;
			neighbors.add(new Board(copy));
		}
		if(x < dimension - 1)
		{
			int[][] copy = copyArray();
			int temp = copy[x + 1][y];
			copy[x + 1][y] = 0;
			copy[x][y] = temp;
			neighbors.add(new Board(copy));
		}
		if(y > 0)
		{
			int[][] copy = copyArray();
			int temp = copy[x][y - 1];
			copy[x][y - 1] = 0;
			copy[x][y] = temp;
			neighbors.add(new Board(copy));
		}
		if(y < dimension - 1)
		{
			int[][] copy = copyArray();
			int temp = copy[x][y + 1];
			copy[x][y + 1] = 0;
			copy[x][y] = temp;
			neighbors.add(new Board(copy));
		}

		return neighbors;
	}

	private int[][] copyArray()
	{
		int[][] copy = new int[dimension][dimension];
		for(int i = 0; i < dimension; i++)
		{
			for(int j = 0; j < dimension; j++)
			{
				copy[i][j] = blocks[i][j];
			}
		}
		return copy;
	}

	@Override
	public String toString()
	{
		String output = dimension + "";
		for(int i = 0; i < dimension; i++)
		{
			output += "\n";
			for(int j = 0; j < dimension; j++)
			{
				output += " " + blocks[i][j];
			}
		}
		output += "\n";
		return output;
	}
}
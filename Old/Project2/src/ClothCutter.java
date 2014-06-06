import java.util.ArrayList;

public class ClothCutter
{
	private int width, height;
	private ArrayList<Pattern> patterns;
	ArrayList<Cut> cuts;
	ArrayList<Garment> garments;

	public ClothCutter(int w, int h, ArrayList<Pattern> p)
	{
		setWidth(w);
		setHeight(h);
		setPatterns(p);
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public ArrayList<Pattern> getPatterns()
	{
		return patterns;
	}

	public void setPatterns(ArrayList<Pattern> patterns)
	{
		this.patterns = patterns;
	}

	public int getValue()
	{
		return 1;
	}

	public ArrayList<Garment> getGarments()
	{
		return garments;
	}

	public void optimize()
	{
	}
}

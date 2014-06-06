import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: master
 * Date: 2/8/13
 * Time: 4:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class Cloth extends JPanel
{
	private int width, height, pixels;

	public Cloth(int w, int h, int p)
	{
		setHeight(h);
		setWidth(w);
		setPixels(p);
	}

	public void drawCut(Cut c)
	{

	}

	public void drawGarment(Garment g)
	{

	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public int getPixels()
	{
		return pixels;
	}

	public void setWidth(int w)
	{
		width = w;
	}

	public void setHeight(int h)
	{
		height = h;
	}

	public void setPixels(int p)
	{
		pixels = p;
	}
}

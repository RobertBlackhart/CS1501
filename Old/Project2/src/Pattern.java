/**
 * Created with IntelliJ IDEA.
 * User: master
 * Date: 2/4/13
 * Time: 9:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class Pattern
{
    int width, height, cost;
    String name;

    public Pattern(int w, int h, int c, String n)
    {
        setWidth(w);
        setHeight(h);
        setCost(c);
        setName(n);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

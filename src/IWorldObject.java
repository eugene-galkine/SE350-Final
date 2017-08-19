import javafx.geometry.Point2D;
import javafx.scene.Node;


public interface IWorldObject
{	
	public Node getImg();
	public boolean intersects(Point2D p);
	public String save();
	public void load(String s);
}

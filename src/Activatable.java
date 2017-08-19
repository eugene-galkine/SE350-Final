import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;


public abstract class Activatable implements IWorldObject
{
	protected Point2D pos;
	protected ImageView imgView;
	
	public abstract void onActivate(IWorldObject activator);

	@Override
	public Node getImg()
	{
		return imgView;
	}

	@Override
	public boolean intersects(Point2D p)
	{
		return pos.equals(p);
	}
}

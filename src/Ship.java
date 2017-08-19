import java.util.Observable;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

//This class needs to extend Observable because the player needs to extend this class and there is no multiple inheritance in java
public abstract class Ship extends Observable implements IWorldObject
{
	protected Point2D pos;
	protected ImageView imgView;
	
	@Override
	public Node getImg()
	{
		return imgView;
	}
	
	@Override
	public boolean intersects (Point2D p)
	{
		return pos.equals(p);
	}
	
	protected void goNorth()
	{
		if (World.getInstance().canMoveTo(new Point2D(pos.getX(), pos.getY() - 1)))
			setPos(new Point2D(pos.getX(), pos.getY() - 1));
	}
	
	protected void goEast()
	{
		if (World.getInstance().canMoveTo(new Point2D(pos.getX() - 1, pos.getY())))
			setPos(new Point2D(pos.getX() - 1, pos.getY()));
	}
	
	protected void goSouth()
	{
		if (World.getInstance().canMoveTo(new Point2D(pos.getX(), pos.getY() + 1)))
			setPos(new Point2D(pos.getX(), pos.getY() + 1));
	}
	
	protected void goWest()
	{
		if (World.getInstance().canMoveTo(new Point2D(pos.getX() + 1, pos.getY())))
			setPos(new Point2D(pos.getX() + 1, pos.getY()));
	}
	
	protected void setPos(Point2D newPos)
	{
		//sets the current Position of this object to the new one
		pos = newPos;
		//move the image
		imgView.setY(pos.getY()*World.getInstance().getCellSize());
		imgView.setX(pos.getX()*World.getInstance().getCellSize());
		//Observable stuff
		setChanged();
		notifyObservers();
	}
}

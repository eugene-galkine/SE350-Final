import java.util.Random;
import java.util.StringTokenizer;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;


public class Shark implements IWorldObject
{
	Point2D pos;
	ImageView img;
	int dir;
	
	public Shark ()
	{
		//argumentless constructor for loading a save
		img = new ImageView(new Image("shark.png",World.getInstance().getCellSize(),World.getInstance().getCellSize(),true,true));
		setPos(new Point2D(0,0));
		dir = 0;
	}
	
	public Shark(Point2D start)
	{
		img = new ImageView(new Image("shark.png",World.getInstance().getCellSize(),World.getInstance().getCellSize(),true,true));
		setPos(start);
		
		//face a random direction
		dir = (new Random()).nextInt(4);
		img.setRotate(dir*90);
	}
	
	@Override
	public Node getImg()
	{
		return img;
	}

	@Override
	public boolean intersects(Point2D p)
	{
		return pos.equals(p);
	}	
	
	public void move (Rectangle bounds)
	{
		Point2D newPos;
		
		switch (dir%4)
		{
		case 0://left
		case 2://right
			//try moving forward, if we can't, turn around
			newPos = new Point2D(pos.getX() + (dir%4 == 0 ? -1 : 1), pos.getY());
			if (World.getInstance().isValidPos(newPos) && bounds.contains(newPos))
				setPos(newPos);
			else
				turnAround();
			break;
		case 1://up
		case 3://down
			//try moving forward, if we can't, turn around
			newPos = new Point2D(pos.getX(), pos.getY() + (dir%4 == 1 ? -1 : 1));
			if (World.getInstance().isValidPos(newPos) && bounds.contains(newPos))
				setPos(newPos);
			else
				turnAround();
			break;
		}
		
		World.getInstance().testPlayerCollision(pos);
	}
	
	private void setPos(Point2D newPos)
	{
		pos = newPos;
		img.setLayoutY(pos.getY()*World.getInstance().getCellSize());
		img.setLayoutX(pos.getX()*World.getInstance().getCellSize());
	}
	
	private void turnAround ()
	{
		//turns the shark 180 degrees
		dir += 2;
		img.setRotate(dir*90);
		//don't let the direction value get too high otherwise we might reach the int limit if it runs too long
		while (dir > 4)
			dir -= 4;
	}

	@Override
	public String save()
	{
		return "Shark,"+pos.getX()+","+pos.getY()+","+dir;
	}

	@Override
	public void load(String s)
	{
		StringTokenizer st = new StringTokenizer(s, ",");
		//the first one is just the class name
		st.nextToken();
		double x = new Double(st.nextToken());
		double y = new Double(st.nextToken());
		dir = new Integer(st.nextToken());
		
		setPos(new Point2D(x,y));
		img.setRotate(dir*90);
	}
}

import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;


public class AnimalContainer extends Activatable implements Runnable
{
	private ArrayList<Shark> list;
	private Rectangle bounds;
	private AnchorPane container;
	
	public AnimalContainer()
	{
		//argumentless constructor for loading a save
		list = new ArrayList<Shark>();
		container = new AnchorPane();
		
		World.getInstance().addObject(this);
	}
	
	public AnimalContainer (Point2D start, int width, int height, int count)
	{
		list = new ArrayList<Shark>();
		bounds = new Rectangle(start.getX(), start.getY(), width, height);
		container = new AnchorPane();
		
		World.getInstance().addObject(this);
		
		//spawn the specified number of sharks inside of the bounds
		for (int i = 0; i < count; i++)
		{
			Point2D pos;
			Random r = new Random();
			do
			{
				pos = new Point2D(r.nextInt(width) + start.getX(), r.nextInt(height) + start.getY());
			} while (!World.getInstance().isSpotOpen(pos));
			add(new Shark(pos));
		}
	}

	@Override
	public void run()
	{
		//infinite loop that updates all the sharks a few times every second
		while (World.getInstance().isGameRunning())
		{
			for (Shark s : list)
				s.move(bounds);
			
			try
			{
				Thread.sleep(300);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void add(Shark obj)
	{
		list.add(obj);
		container.getChildren().add(obj.getImg());
	}

	@Override
	public Node getImg()
	{
		return container;
	}

	@Override
	public boolean intersects(Point2D p)
	{
		//test for collsion with sharks
		for (Shark s : list)
			if (s.intersects(p))
				return true;
		
		return false;
	}

	@Override
	public void onActivate(IWorldObject activator)
	{
		//someone hit the shark, game over if it's the player
		if (activator instanceof Player)
			((Player)activator).gameOver();
	}
	
	@Override
	public String save()
	{
		String save = "AnimalContainer,"+bounds.getX()+","+bounds.getY()+","+bounds.getWidth()+","+bounds.getHeight();
		for (Shark s : list)
			save += "[" + s.save();
		return save;
	}

	@Override
	public void load(String s)
	{
		StringTokenizer st = new StringTokenizer(s, "[");
		//the string contains the shark data and ours, seperate them
		String ours = st.nextToken();
		//load the sharks
		while (st.hasMoreTokens())
		{
			Shark shark = new Shark();
			shark.load(st.nextToken());
			add(shark);
		}
		
		st = new StringTokenizer(ours, ",");
		//the first one is just the class name
		st.nextToken();
		double x = new Double(st.nextToken());
		double y = new Double(st.nextToken());
		double w = new Double(st.nextToken());
		double h = new Double(st.nextToken());
		
		bounds = new Rectangle(x, y, w, h);
	}
}

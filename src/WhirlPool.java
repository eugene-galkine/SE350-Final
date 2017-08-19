import java.util.StringTokenizer;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class WhirlPool extends Activatable
{
	public WhirlPool ()
	{
		//argumentless constructor for loading a save
		imgView = new ImageView(new Image("whirlpool.jpg",World.getInstance().getCellSize()-2,World.getInstance().getCellSize()-2,true,true));
		pos = new Point2D(0,0);
		World.getInstance().addObject(this);
	}
	
	public WhirlPool (Point2D pos)
	{
		this.pos = pos;
		//init image and make it smaller that a grid space so you can see the cell outline
		imgView = new ImageView(new Image("whirlpool.jpg",World.getInstance().getCellSize()-2,World.getInstance().getCellSize()-2,true,true));
		imgView.setX(pos.getX()*World.getInstance().getCellSize()+1);
		imgView.setY(pos.getY()*World.getInstance().getCellSize()+1);
		
		World.getInstance().addObject(this);
	}
	
	@Override
	public void onActivate(IWorldObject activator)
	{
		((Player) activator).teleportRandom();
	}

	@Override
	public String save()
	{
		return "WhirlPool,"+pos.getX()+","+pos.getY();
	}

	@Override
	public void load(String s)
	{
		StringTokenizer st = new StringTokenizer(s, ",");
		//the first one is just the class name
		st.nextToken();
		double x = new Double(st.nextToken());
		double y = new Double(st.nextToken());
		pos = new Point2D(x,y);
		imgView.setX(pos.getX()*World.getInstance().getCellSize()+1);
		imgView.setY(pos.getY()*World.getInstance().getCellSize()+1);
	}
}

import java.util.StringTokenizer;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Key extends Activatable
{
	public Key ()
	{
		//argumentless constructor for loading a save
		imgView = new ImageView(new Image("key.png", World.getInstance().getCellSize(),World.getInstance().getCellSize(), true, true));
		pos = new Point2D(0,0);
		World.getInstance().addObject(this);
	}
	
	public Key (Point2D pos)
	{
		imgView = new ImageView(new Image("key.png", World.getInstance().getCellSize(),World.getInstance().getCellSize(), true, true));
		this.pos = pos;
		imgView.setLayoutX(pos.getX()*World.getInstance().getCellSize());
		imgView.setLayoutY(pos.getY()*World.getInstance().getCellSize());
		World.getInstance().addObject(this);
	}
	
	@Override
	public void onActivate(IWorldObject activator)
	{
		//if the player entered our cell, he picks up key and we disapear
		if (activator instanceof Player)
		{
			((Player) activator).pickUpKey();
			World.getInstance().removeObject(this);
		}
	}
	
	@Override
	public String save()
	{
		return "Key,"+pos.getX()+","+pos.getY();
	}

	@Override
	public void load(String s)
	{
		StringTokenizer st = new StringTokenizer(s, ",");
		//the first one is just the class name
		st.nextToken();
		double x = new Double(st.nextToken());
		double y = new Double(st.nextToken());
		pos = new Point2D(x, y);
		imgView.setLayoutX(x*World.getInstance().getCellSize());
		imgView.setLayoutY(y*World.getInstance().getCellSize());
	}
}

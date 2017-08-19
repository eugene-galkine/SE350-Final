import java.util.StringTokenizer;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Treasure extends Activatable
{
	public Treasure ()
	{
		//argumentless constructor for loading a save
		imgView = new ImageView(new Image("treasure.png", World.getInstance().getCellSize(),World.getInstance().getCellSize(), true, true));
		pos = new Point2D(0,0);
		World.getInstance().addObject(this);
	}
	
	public Treasure (Point2D pos)
	{
		imgView = new ImageView(new Image("treasure.png", World.getInstance().getCellSize(),World.getInstance().getCellSize(), true, true));
		this.pos = pos;
		imgView.setLayoutX(pos.getX()*World.getInstance().getCellSize());
		imgView.setLayoutY(pos.getY()*World.getInstance().getCellSize());
		World.getInstance().addObject(this);
	}
	
	@Override
	public void onActivate(IWorldObject activator)
	{
		if (activator instanceof Player && ((Player)activator).hasKey())
			((Player)activator).winGame();
		else if (!((Player)activator).hasKey())
			Notifier.getInstance().setText("You need to find the key first!");
	}
	
	@Override
	public String save()
	{
		return "Treasure,"+pos.getX()+","+pos.getY();
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

import java.util.Random;
import java.util.StringTokenizer;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;


public class Player extends Ship implements EventHandler<KeyEvent>
{	
	private boolean hasKey = false;
	
	public Player ()
	{
		//argumentless constructor for loading a save
		init(new Point2D(0,0));
	}
	
	public Player (Point2D start)
	{
		init(start);
	}
	
	private void init(Point2D start)
	{
		imgView = new ImageView(new Image("ship.png", World.getInstance().getCellSize(), World.getInstance().getCellSize(), true, true));
		setPos(start);
		World.getInstance().addObject(this);
	}

	@Override
	public void handle(KeyEvent e)
	{
		//don't take input if the game ended
		if (!World.getInstance().isGameRunning())
			return;
			
		switch (e.getCode())
		{
		case RIGHT:
			goWest();
			break;
		case LEFT:
			goEast();
			break;
		case UP:
			goNorth();
			break;
		case DOWN:
			goSouth();
			break;
		default:
			break;
		}
		
		World.getInstance().testActivatable(this, pos);
		World.getInstance().snapCameraTo(pos);
	}

	//this function is used for whirlpools
	public void teleportRandom()
	{
		//find a random open spot
		Random r = new Random();
		Point2D newPos;
		do
		{
			newPos = new Point2D(r.nextInt(World.getInstance().getMap()[0].length), r.nextInt(World.getInstance().getMap().length));
		} while (!World.getInstance().isSpotOpen(newPos));
		
		//teleport to new spot
		setPos(newPos);
		World.getInstance().snapCameraTo(pos);
		
		//display the message that we entered a whirlpool
		Notifier.getInstance().setText("You entered a whirlpool");
	}

	public void gameOver()
	{
		World.getInstance().stopGame();
		Notifier.getInstance().setText("GAME OVER");
	}
	
	public void pickUpKey()
	{
		hasKey = true;
		Notifier.getInstance().setText("You found the key!");
	}
	
	public void winGame()
	{
		World.getInstance().stopGame();
		Notifier.getInstance().setText("You found the Treasure! You Win!");
	}
	
	public boolean hasKey ()
	{
		return hasKey;
	}

	@Override
	public String save()
	{
		return "Player,"+pos.getX()+","+pos.getY()+","+hasKey;
	}

	@Override
	public void load(String s)
	{
		StringTokenizer st = new StringTokenizer(s, ",");
		//the first one is just the class name
		st.nextToken();
		double x = new Double(st.nextToken());
		double y = new Double(st.nextToken());
		hasKey = new Boolean(st.nextToken());
		
		setPos(new Point2D(x,y));
		World.getInstance().snapCameraTo(pos);
	}
}

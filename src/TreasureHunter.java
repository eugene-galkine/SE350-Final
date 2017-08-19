import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class TreasureHunter extends Application
{
	private static final int SCREEN_SIZE = 650;//there should be an odd number of cells (num cells = SCREEN_SIZE/CELL_SIZE)
	private static final int CELL_SIZE = 50;
	
	private static final int WHIRLPOOL_COUNT = 4;
	
	private AnchorPane pane, parentPane;
	private Scene scene;
	
	public static void main (String[] args)
	{
		launch();
	}
	
	@Override
	public void start(Stage s) throws Exception
	{
		//the game pane is inside the parent pane which doesn't move around and holds the ui
		parentPane = new AnchorPane();
		pane = new AnchorPane();
		
		parentPane.getChildren().add(pane);
		
		s.setResizable(false);
		scene = new Scene(parentPane, SCREEN_SIZE - 10, SCREEN_SIZE + 40);
		s.setScene(scene);
		s.setTitle("Treasure Hunter");
		//this needs to be here so it kills the other thread upon closing
		s.setOnCloseRequest(e ->  System.exit(0));
		
		new World (pane, SCREEN_SIZE, CELL_SIZE);
		setUpMap();
		setUpObjects();
		setUpUI();
		
		s.show();
	}

	private void resetGame()
	{
		//reset everything
		parentPane.getChildren().clear();
		pane.getChildren().clear();
		parentPane.getChildren().add(pane);
		new World (pane, SCREEN_SIZE, CELL_SIZE);
	}
	
	private void setUpMap()
	{
		int[][] map = World.getInstance().getMap();
		//this draws the water and the land
		for (int y = 0; y < map.length; y++)
		{
			for (int x = 0; x < map[0].length; x++)
			{
				Rectangle r = new Rectangle(x*CELL_SIZE,y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
				r.setStroke(Color.BLACK);
				r.setFill(map[y][x] == 1 ? Color.GREEN : Color.PALETURQUOISE);
				
				pane.getChildren().add(r);
			}
		}
		
		//move camera to bottom right corner of the map
		pane.setLayoutX(-(map[0].length-(SCREEN_SIZE/CELL_SIZE))*CELL_SIZE);
		pane.setLayoutY(-(map.length-(SCREEN_SIZE/CELL_SIZE))*CELL_SIZE);
	}
	
	private void setUpObjects()
	{
		int[][] map = World.getInstance().getMap();
		
		//create a shark container and spawn 10 sharks in it and start the thread
		new Thread(new AnimalContainer(new Point2D(18,0), 16, 14, 12)).start();
		
		//spawn whirlpools
		for (int i = 0; i < WHIRLPOOL_COUNT; i++)		
			new WhirlPool(World.getInstance().findRandomOpenSpot());
		
		//spawn key
		new Key(World.getInstance().findRandomOpenSpot());
		
		//spawn treasure
		new Treasure(World.getInstance().findRandomOpenSpot());
		
		//add player to the world and make him the key handler
		scene.setOnKeyPressed(new Player(new Point2D(map[0].length - 1, map.length - 1)));
	}
	
	private void setUpUI()
	{
		//toolbar at the bottom of the screen
		Rectangle r = new Rectangle(0, SCREEN_SIZE, SCREEN_SIZE, 50);
		r.setFill(Color.BURLYWOOD);
		parentPane.getChildren().add(r);
		
		//set up buttons on the tool bar
		createButton("Reset Level", 20, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent e)
			{
				resetGame();
				setUpMap();
				setUpObjects();
				setUpUI();
			}
		});
		
		createButton("Save", 100, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent e)
			{
				SaveHandler.save(); 
			}
		});
		
		createButton("Load", 145, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent e)
			{
				resetGame();
				setUpMap();
				scene.setOnKeyPressed(SaveHandler.load());
				setUpUI();
			}
		});
		
		createButton("Exit", SCREEN_SIZE - 60, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent e)
			{
				Platform.exit();
				System.exit(0);
			}
		});
		
		//setup the notification text
		Text text = new Text();
		text.setLayoutX(10);
		text.setLayoutY(35);
		text.setFont(new Font(40));
		text.setStrokeWidth(1);
		text.setStroke(Color.WHITE);
		new Notifier(text);
		parentPane.getChildren().add(text);
	}
	
	private void createButton (String name, int x, EventHandler<MouseEvent> event)
	{
		Button b = new Button(name);
		b.setLayoutX(x);
		b.setLayoutY(SCREEN_SIZE + 12);
		b.setOnMouseClicked(event);
		parentPane.getChildren().add(b);
	}
}

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class SaveHandler
{
	public static void save()
	{
		//don't want to save if player already lost
		if (!World.getInstance().isGameRunning())
			return;
		
		//get list of all objects and save their info to a text file (couldn't get serialization to work with javafx)
		ArrayList<IWorldObject> objects = World.getInstance().getObjects();
		BufferedWriter writer;
		try
		{
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("game.save")));
		
			//write each line
			for (IWorldObject object : objects)
				writer.write(object.save()+"\n");
		
			writer.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static Player load()
	{
		BufferedReader reader;
		Player p = null;
		try
		{
			//read the list of objects and init them
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("game.save")));
			String line;
			
			while ((line = reader.readLine()) != null)
			{
				//get name of class
				String name = new StringTokenizer(line, ",").nextToken();
				
				//init class by it's string name
				@SuppressWarnings("unchecked")
				Class<IWorldObject> newClass = (Class<IWorldObject>) Class.forName(name);
				IWorldObject object = (IWorldObject) newClass.newInstance();
				//pass it the line so it can go back to old state
				object.load(line);
				
				//return this for input handling
				if (name.equals("Player"))
					p = (Player) object;
				else if (name.equals("AnimalContainer"))//this is a AnimalContainer and we need to start the thread
					new Thread((AnimalContainer)object).start();
			}
			
			reader.close();
		} catch (FileNotFoundException e)
		{
			System.out.println("ERROR: No save exists");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return p;
	}
}

/**
 * Purpose: to load the game from outside input file and then, validate && suppress non-fatal errors && output the maze as a String
 * Name: Minh Vu
 * Last modified date: 14/04/2022
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@SuppressWarnings({"PMD.NoPackage", "PMD.PreserveStackTrace"}) // Expected faults will output certain information to player which doesn't require stack-trace 
public class GameLoader 
{
    //Class-field:
    private GameMechanic mech;
    //Shared instance of maze
    private Maze maze = Maze.getInstance();

    //Constructor:
    public GameLoader(GameMechanic mech)
    {
        this.mech = mech;
    }

    @Override
    public String toString() 
    {
        return mech.toString();
    }

    public String readFile() throws GameException
    {
        String warning = null;
        try(FileReader fileReader = new FileReader("src/main/resources/MazeStructure.txt"))
        {
            @SuppressWarnings("PMD.CloseResource") // Already included in try-with-resource()
            BufferedReader reader = new BufferedReader(fileReader);
            String line = reader.readLine();
            if(line != null)
            {
                String[] sizes = line.split(" ", 2);
                maze.createMaze(Integer.parseInt(sizes[0]) , Integer.parseInt(sizes[1]));
            }
            while((line = reader.readLine()) != null)
            {
                //trim to minimize chances of extra white-spaces causing error
                String value = populateMaze(line.trim());
                //if the returned String is not empty and the warning String hasn't already included similar type of the returned String
                if(value != null && ( warning == null || (!warning.contains(value))))
                {
                    if(warning == null)
                    {
                        warning = "";
                    }
                    warning += value;
                }
            }
            reader.close();
        }
        catch(FileNotFoundException e)
        {
            throw new GameException("Couldn't find the file MazeStructure.txt. " + e.getMessage()); // NOPMD - return e.getMessage() for stackTrace()
        }
        catch(IOException e)
        {
            throw new GameException("Error in reading the game file. " + e.getMessage()); // NOPMD - return e.getMessage() for stackTrace()
        }

        //If input file doesn't include a starting nor an ending point, create a default point for each
        //for a starting point
        if(!maze.hasStartP())
        {
            mech.configPlayerLocation(0, 0);
            warning += "Maze doesn't contain any starting point. One will be created by default!";
        }
        //for an ending point
        if(!maze.hasEndP())
        {
            maze.setMazeSquare(maze.getMazeRows() - 1, maze.getMazeCols() - 1, maze.getMazeSquare(maze.getMazeRows() - 1, maze.getMazeCols() - 1));
            warning += "Maze doesn't contain any ending point. One will be created by default!";
        }

        return warning;
    }

    private String populateMaze(String textLine) throws GameException
    {
        String result;
        int row, col;
        String[] parts = textLine.split(" ");
        //at least each line should have 3 parameters for each feature
        if(parts.length >= 3)
        {
            try
            {
                row = Integer.parseInt(parts[1]);
                col = Integer.parseInt(parts[2]);
            }
            catch(NumberFormatException e) // NO PMD - intentionally to crash
            {
                throw new GameException("Invalid file maze input."); // NO PMD - intentionally to crash
            }

            //ignore a square which is outside of maze size
            if(row >= maze.getMazeRows() || col >= maze.getMazeCols())
            {
                result = "Warning! Maze's input file contains entry(s) with row and column that is outside of the maze's boundary. Ignore these lines!\n";
            }
            //populate to maze if otherwise
            else
            {
                if(parts[0].equals("K") || parts[0].equals("DH") || parts[0].equals("DV") || parts[0].equals("M"))
                {
                    result = param4(textLine);
                }
                else if(parts[0].equals("S") || parts[0].equals("WH") || parts[0].equals("WV") || parts[0].equals("E"))
                {
                    result = param3(textLine);
                }
                else
                {
                    throw new GameException("Invalid file maze input.");
                }
            }
        }
        else
        {
            throw new GameException("Invalid file maze input.");
        }

        return result;
    }

    private String param4(String textLine) throws GameException
    {
        String result = null;
        String[] data;
        int row, col, xData;

        data = textLine.split(" ", 4);
        row = Integer.parseInt(data[1]);
        col = Integer.parseInt(data[2]);

        if(data.length == 4)
        {
            if(data[0].equals("M"))
            {
                maze.setMazeSquare(row, col, new Message(maze.getMazeSquare(row, col) , data[3])); 
            }
            else
            {
                try
                {
                    xData = Integer.parseInt(data[3]);
                    //Color code can't be larger than the originally available code
                    if(xData > 6)
                    {
                        throw new GameException("Invalid file maze input.");
                    }
                }
                catch(NumberFormatException e) // NO PMD - intentionally to crash
                {
                    throw new GameException("Invalid file maze input."); // NO PMD - intentionally to crash
                }
    
                if(data[0].equals("K"))
                {
                    maze.setMazeSquare(row, col, new Key(maze.getMazeSquare(row, col) , xData - 1)); // adjust to array index position
                }
                else if(data[0].equals("DH"))
                {
                    if(checkBlock(false, row, col))
                    {
                        result = "Warning! A door/wall has already been placed to left of the current square. The initial value will be maintained as default.\n";
                    }
                    else
                    {
                        maze.setMazeSquare(row, col, new Door(maze.getMazeSquare(row, col) , xData - 1, false));
                    }
                }
                else if(data[0].equals("DV"))
                {
                    if(checkBlock(true, row, col))
                    {
                        result = "Warning! A door/wall has already been placed on top of the current square. The initial value will be maintained as default.\n";
                    }
                    else
                    {
                        maze.setMazeSquare(row, col, new Door(maze.getMazeSquare(row, col) , xData - 1, true));
                    }
                }
            }
        }
        else
        {
            throw new GameException("Invalid maze input file!");
        }

        return result;
    }

    private String param3(String textLine) throws GameException
    {
        String result = null;
        String[] data;
        int row, col;
        data = textLine.split(" ", 3);
        if(data.length == 3)
        {
            row = Integer.parseInt(data[1]);
            col = Integer.parseInt(data[2]);

            if(data[0].equals("S"))
            {
                //check if there's already an existing starting point
                if(maze.hasStartP())
                {
                    result = "Invalid Extra Starting Points! Player's initial position will be maintained as default.";
                }
                //otherwise add one
                else
                {
                    mech.configPlayerLocation(row, col);
                }
            }
            else if(data[0].equals("E"))
            {
                maze.setMazeSquare(row, col, new EndP(maze.getMazeSquare(row, col))); 
            }
            else if(data[0].equals("WH"))
            {
                if(checkBlock(false, row, col))
                {
                    result = "Warning! A door/wall has already been placed to left of the current square. The initial value will be maintained as default.\n";
                }
                else
                {
                    maze.setMazeSquare(row, col, new Wall(maze.getMazeSquare(row, col), false));
                }
                
            }
            else if(data[0].equals("WV"))
            {
                if(checkBlock(true, row, col))
                {
                    result = "Warning! A door/wall has already been placed to left of the current square. The initial value will be maintained as default.\n";
                }
                else
                {
                    maze.setMazeSquare(row, col, new Wall(maze.getMazeSquare(row, col), true));
                }
            }
        }
        else
        {
            throw new GameException("Invalid maze input file!");
        }

        return result;
    }

    private boolean checkBlock(boolean vertical, int row, int col)
    {
        //check for existing objects in designated grid entry
        for(Feature feat: maze.getMazeSquare(row, col).getFeatures())
        {
            if(feat.getClass().getName().equals("Door"))
            {
                if (vertical == ((Door)feat).isVertical())
                {
                    return true;
                }
            }
            else if (feat.getClass().getName().equals("Wall"))
            {
                if (vertical == ((Wall)feat).isVertical())
                {
                    return true;
                }
            }
        }

        return false;
    }
}
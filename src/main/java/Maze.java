
/**
 * Purpose: holds the main maze game object and its interaction with controller for input/output
 * Name: Minh Vu
 * Last modified date: 14/04/2022
 */
import java.util.logging.Logger;

@SuppressWarnings("PMD.NoPackage")
public class Maze 
{
    //Constant:
    private final String clearColor = "\033[m";

    //Logger
    private static final Logger logger = Logger.getLogger(Maze.class.getName()); // NO PMD - Name is fine

    //Class-fields:
    private Square[][] maze;
    private String[][] grid;
    private int sqrRows; 
    private int sqrCols;
    private int gridRows;
    private int gridCols;

    //Singleton of Maze object
    private static Maze mazeGame = null;
    public static Maze getInstance()
    {
        if(mazeGame == null)
        {
            mazeGame = new Maze();
        }
        return mazeGame;
    }

    public void createMaze(int sqrRows, int sqrCols)
    {
        this.sqrRows = sqrRows;
        this.sqrCols = sqrCols;

        //create and populate all the basic maze squares
        maze = new Square[sqrRows][sqrCols];
        for(int i = 0; i < sqrRows; i++)
        {
            for(int j = 0; j < sqrCols; j++)
            {
                maze[i][j] = new Generic();
            }
        }
    }

    //Preferred to return a shallow copy or a deep copy of mutable object but for the purpose of studying, actual reference to the original object is accepted
    public Square getMazeSquare(int indexR, int indexC)
    {
        return maze[indexR][indexC];
    }

    public int getMazeRows()
    {
        return sqrRows;
    }

    public int getMazeCols()
    {
        return sqrCols;
    }

    public void setMazeSquare(int indexR, int indexC, Square square)
    {
        maze[indexR][indexC] = square;
        updateGrid();
    }

    public boolean hasStartP()
    {
        if(sqrRows != 0)
        {
            for(int i = 0; i < sqrRows; i++)
            {
                for(int j = 0; j < sqrCols; j++)
                {
                    if(maze[i][j].hasStartP())
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean hasEndP()
    {
        if(sqrRows != 0)
        {
            for(int i = 0; i < sqrRows; i++)
            {
                for(int j = 0; j < sqrCols; j++)
                {
                    if(maze[i][j].hasStartP())
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void playerToGrid(int gridPRow, int gridPCol)
    {
        updateGrid();

        if(!grid[gridPRow][gridPCol].equals(" "))
        {
            if(grid[gridPRow][gridPCol - 1].equals(" "))
            {
                grid[gridPRow][gridPCol - 1] = grid[gridPRow][gridPCol];
            }
            else
            {
                if(grid[gridPRow][gridPCol + 1].equals(" "))
                {
                    grid[gridPRow][gridPCol + 1] = grid[gridPRow][gridPCol];
                }
            }
        }
        grid[gridPRow][gridPCol] = "P";
    }

    @Override
    public String toString()
    {
        logger.info("Got here!");

        //print out entire grid as one String
        String result = "";
        for(int i = 0; i < gridRows; i++)
        {
            for(int j = 0; j < gridCols; j++)
            {
                result += grid[i][j];
            }
            result += "\n";
        }

        return result;
    }

    public void removeKey(int sqrRow, int sqrCol, Feature key)
    {
        logger.info("check the key is actually removed here");

        maze[sqrRow][sqrCol].removeFeature(key);
        updateGrid();
    }

    private void updateGrid()
    {
        //if grid hasn't been constructed yet - first time output to screen
        if(grid == null)
        {
            populateBasic();
        }

        //populate the grid with the content of its maze square(modifiable)
        editGrid();
    }

    public String[][] getGrid()
    {
        return grid;
    }

    private void populateBasic()
    {
        //save the row and column size of the entire displayed grid
        this.gridRows = sqrRows * 2 + 1;
        this.gridCols = sqrCols * 4 + 1;

        //initialize new grid
        grid = new String[gridRows][gridCols];

        //create basic border layout on the rear of grid
        grid[0][0] = "Ú";  // (\u250c)
        grid[0][gridCols - 1] = "¿";  // (\u2510)
        grid[gridRows - 1][0] = "À";  //?(\u2514) 
        grid[gridRows - 1][gridCols - 1] = "Ù"; // (\u2518) 

        for(int i = 1; i < gridRows - 1; i++)
        {
            grid[i][0] = "³"; //(\u2502) 
            grid[i][gridCols - 1] = "³"; // (\u2502) 
        }

        for(int i = 1; i < gridCols - 1; i++)
        {
            grid[0][i] = "Ä"; // (\u2500)
            grid[gridRows - 1][i] = "Ä"; // (\u2500)
        }

        //create empty spaces inside the border grid - content-empty grid layout
        for(int i = 1; i < gridRows - 1; i++)
        {
            for(int j = 1; j < gridCols - 1; j++)
            {
                grid[i][j] = " ";
            }
        }

        // test
        // print out entire grid as one String
        String test = "";
        for(int i = 0; i < gridRows; i++)
        {
            for(int j = 0; j < gridCols; j++)
            {
                test += grid[i][j];
            }
            test += "\n";
        }
        final String finalT = test;
        logger.info(()-> "Basic grid layout: \n" + finalT);
    }

    private void editGrid()
    {
        //clear the previous content in the grid
        for(int i = 1; i < gridRows - 1; i++)
        {
            for(int j = 1; j < gridCols - 1; j++)
            {
                grid[i][j] = " ";
            }
        }

        //populate new features onto grid
        for(int i = 0; i < sqrRows; i++)
        {
            for(int j = 0; j < sqrCols; j++)
            {
                //if there're features added in this square
                if(!maze[i][j].getFeatures().isEmpty())
                {
                    //project the coordinate of squares onto grid coords
                    int x = i * 2 + 1;
                    int y = j * 4 + 1;

                    logger.info(() -> "Value of x: " + x + " and y: " + y + " on grid.");

                    //traverse all the features
                    for(Feature feature: maze[i][j].getFeatures())
                    {
                        //get name of each feature type
                        String name = feature.getClass().getName();

                        if(name.equals("Door") || name.equals("Key"))
                        {
                            //get color code
                            String color = ((WithColor)feature).getColorCode();
                            if(name.equals("Door"))
                            {
                                doorToGrid(feature, x, y, color);
                            }
                            else
                            {
                                keyToGrid(x, y, color);
                            }
                        }
                        else if(name.equals("Wall"))
                        {
                            wallToGrid(feature, x, y);
                        }
                        else if(name.equals("EndP"))
                        {
                            endPToGrid(x, y);
                        }
                    }   
                }   
            }
        }
        
        //Change the look of the wall based on surrounding after all the features are loaded up on the grid
        reDecorVWall();
        reDecorHWall();
    }

    private void doorToGrid(Feature feature, int x, int y, String colorC)
    {
        //vertical door on left
        if(((Door)feature).isVertical())
        {
            //logger.info(() -> "Visit when vertical door is projected onto grid");

            //if the maze border isn't on the left, populate onto grid
            if( y - 1 != 0)
            {
                //if the door is connected to top border, redecorate the border
                if( x - 1 == 0)
                {
                    grid[x - 1][y - 1] = "Â"; // (\u252c)
                }
                grid[x][y-1] = colorC + "±" + clearColor; // (\u2592)

                //if the door is connected to bottom border, redecorate the border
                if( x + 1 == gridRows - 1)
                {
                    grid[x - 1][y - 1] = "Â"; // (\u252c)
                }
            }
        }
        //horizontal door on top
        else
        {
            //logger.info(() -> "Visit when horizontal door is projected onto grid");

            //if the maze border isn't on the top, populate onto grid
            if( x - 1 != 0)
            {
                for(int i = y; i < y + 3; i++)
                {
                    grid[x - 1][i] = colorC + "±" + clearColor; // (\u2592)
                }
            }
        }
    }

    private void keyToGrid(int x, int y, String colorC)
    {
        logger.info(() -> "Visit when key is projected onto grid");

        // ideally indicate the key in the middle of square, otherwise at 2 other places. If it's full, no indication is needed.
        if(grid[x][y + 1].equals(" "))
        {
            grid[x][y + 1] = colorC + "ô" + clearColor; // Pilcrow symbol presenting key
        }
        else if(grid[x][y].equals(" "))
        {
            grid[x][y] = colorC + "ô" + clearColor;
        }
        else if(grid[x][y + 2].equals(" "))
        {
            grid[x][y + 2] = colorC + "ô" + clearColor;
        }
    }

    private void endPToGrid(int x, int y)
    {
        if(!grid[x][y + 1].equals(" "))
        {
            if(grid[x][y].equals(" "))
            {
                grid[x][y] = grid[x][y + 1];
            }
            else if(grid[x][y + 2].equals(" "))
            {
                grid[x][y + 2] = grid[x][y + 1];
            }
        }

        grid[x][y + 1] = "E";
    }

    private void wallToGrid(Feature feature, int x, int y)
    {
        //vertical wall on left
        if(((Wall)feature).isVertical())
        {
            //logger.info(() -> "Visit when vertical wall is projected onto grid");

            //if the maze border isn't on the left, populate onto grid
            if( y - 1 != 0)
            {
                //if maze border is right on top, change the border look where applicable
                if(x - 1 == 0)
                {
                    grid[x - 1][y - 1] = "Â"; // (\u252c)
                }
                else
                {
                    grid[x - 1][y - 1] = "³"; // (\u2502)
                }

                
                //if maze border is right below, change the border look where applicable
                if(x + 1 == gridRows - 1)
                {
                    grid[x + 1][y - 1] = "Á"; // (\u2534)
                }
                else
                {
                    grid[x + 1][y - 1] = "³"; //(\u2502)
                }

                grid[x][y - 1] = "|";
            }  
        }
        //horizontal wall on top
        else
        {
            //logger.info(() -> "Visit when horizontal wall is projected onto grid");

            //if the maze border isn't on the top, populate onto grid
            if( x - 1 != 0)
            {
                //if maze border is right on the left, change the border look where applicable
                if( y - 1 == 0)
                {
                    grid[x - 1][y - 1] = "Ã";
                }
                else
                {
                    grid[x - 1][y - 1] = "Ä"; //(\u2500)
                }

                for(int i = y; i < y + 3; i++)
                {
                    grid[x - 1][i] = "-"; 
                }

                //if maze border is right on the right, change the border look where applicable
                if( y + 3 == gridCols - 1)
                {
                    grid[x - 1][y + 3] = "´";
                }
                else
                {
                    grid[x - 1][y + 3] = "Ä"; //(\u2500)
                }
            }
        }
    }

    private void reDecorVWall()
    {
        for(int i = 1; i < gridRows - 1; i++)
        {
            for(int j = 1; j < gridCols - 1; j++)
            {
                //if there's a vertical wall in a grid element, consider the surrounding
                if(grid[i][j].equals("³")) //(\u2502)
                {
                    //variety of joint wall combinations
                    if(!grid[i - 1][j].equals(" ")) // NO PMD - Accepting in this case due to need of ! condition
                    {
                        if(!grid[i][j + 1].equals(" ")) 
                        {// NO PMD
                            if(!grid[i][j - 1].equals(" "))
                            {
                                if(!grid[i + 1][j].equals(" ")) 
                                {// NO PMD
                                    grid[i][j] = "Å"; //(\u253c)
                                }
                                else
                                {
                                    grid[i][j] = "Á"; //(\u2534)
                                }
                            }
                            else
                            {
                                grid[i][j] = "À"; //(\u2514)
                            }
                        }
                    }

                    if(!grid[i + 1][j].equals(" ")) // NO PMD - Accepting in this case due to complexity
                    {
                        if(!grid[i][j + 1].equals(" "))
                        {
                            if(!grid[i][j - 1].equals(" ")) 
                            {
                                if(grid[i - 1][j].equals(" "))
                                {
                                    grid[i][j] = "Â"; //(\u252c)
                                }
                            }
                            else
                            {
                                grid[i][j] = "Ú"; //(\u250c)
                            }
                        }
                    }

                    if(!grid[i - 1][j].equals(" ")) // NO PMD - Accepting in this case due to complexity
                    {
                        if(!grid[i][j - 1].equals(" "))
                        {
                            if(grid[i][j + 1].equals(" "))
                            {
                                grid[i][j] = "Ù"; //(\u2518)
                            }   
                        }
                    }


                    if(!grid[i + 1][j].equals(" ")) // NO PMD - Accepting in this case due to complexity
                    {
                        if(!grid[i][j - 1].equals(" "))
                        {
                            if(grid[i][j + 1].equals(" "))
                            {
                                grid[i][j] = "¿"; //(\u2510)
                            }
                        }
                    }

                    if(!grid[i + 1][j].equals(" ")) // NO PMD - Accepting in this case due to complexity
                    {
                        if(!grid[i - 1][j].equals(" "))
                        {
                            if(!grid[i][j + 1].equals(" "))
                            {
                                if(grid[i][j - 1].equals(" "))
                                {
                                    grid[i][j] = "Ã"; //(\u251c)
                                }
                            }
                        }
                    }

                    if(!grid[i + 1][j].equals(" ")) // NO PMD - Accepting in this case due to complexity
                    {
                        if(!grid[i - 1][j].equals(" "))
                        {
                            if(!grid[i][j - 1].equals(" "))
                            {
                                if(grid[i][j + 1].equals(" "))
                                {
                                    grid[i][j] = "´"; //(\u2524)
                                }
                            }
                        }
                    }
                }
                else if(grid[i][j].equals("|"))
                {
                    grid[i][j] = "³";
                }                
            }
        }
    }

    private void reDecorHWall()
    {
        for(int i = 1; i < gridRows - 1; i++)
        {
            for(int j = 1; j < gridCols - 1; j++)
            {
                //if there's a vertical wall in a grid element, consider the surrounding
                if(grid[i][j].equals("Ä")) // (\u2500)
                {
                    //variety of joint wall combinations
                    if(!grid[i][j - 1].equals(" "))
                    {
                        if(!grid[i + 1][j].equals(" "))
                        {
                            if(!grid[i][j + 1].equals(" "))
                            {
                                if(!grid[i - 1][j].equals(" "))
                                {
                                    grid[i][j] = "Å"; //(\u253c)
                                }
                                else
                                {
                                    grid[i][j] = "Â"; //(\u252c)
                                }
                            }
                            else
                            {
                                grid[i][j] = "¿"; // (\u2510) 
                            }
                        }
                    }

                    if(!grid[i][j - 1].equals(" "))
                    {
                        if(!grid[i - 1][j].equals(" "))
                        {
                            if(!grid[i][j + 1].equals(" "))
                            {
                                if(grid[i + 1][j].equals(" "))
                                {
                                    grid[i][j] = "Á"; //(\u2534)
                                }
                            }
                            else
                            {
                                grid[i][j] = "Ù"; // (\u2518)
                            }
                        }
                    }

                    if(!grid[i][j + 1].equals(" "))
                    {
                        if(!grid[i - 1][j].equals(" "))
                        {
                            if(grid[i][j - 1].equals(" "))
                            {
                                grid[i][j] = "À"; //?(\u2514)
                            }   
                        }
                    }


                    if(!grid[i][j + 1].equals(" "))
                    {
                        if(!grid[i + 1][j].equals(" "))
                        {
                            if(grid[i][j - 1].equals(" "))
                            {
                                grid[i][j] = "Ú"; // (\u250c)
                            }
                        }
                    }

                    if(!grid[i][j - 1].equals(" "))
                    {
                        if(!grid[i - 1][j].equals(" "))
                        {
                            if(!grid[i + 1][j].equals(" "))
                            {
                                if(grid[i][j + 1].equals(" "))
                                {
                                    grid[i][j] = "´"; //(\u2524)
                                }
                            }
                        }
                    }

                    if(!grid[i][j + 1].equals(" "))
                    {
                        if(!grid[i + 1][j].equals(" "))
                        {
                            if(!grid[i - 1][j].equals(" "))
                            {
                                if(grid[i][j - 1].equals(" "))
                                {
                                    grid[i][j] = "Ã"; //(\u251c)
                                }
                            }
                        }
                    }
                }
                else if(grid[i][j].equals("-"))
                {
                    //replace with the longer symbol as it's not changed based on the surrounding grid like those at edges
                    for(int k = 0; k < 3; k++)
                    {
                        grid[i][j] = "Ä";
                    }
                }                      
            }
        }
    }
}
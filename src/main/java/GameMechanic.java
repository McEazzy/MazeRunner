
/**
 * Purpose: handles the logic/mechanic of game as well as preparing for maze's output
 * Name: Minh Vu
 * Last modified date: 14/04/2022
 */
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@SuppressWarnings("PMD.NoPackage")
public class GameMechanic 
{
    //Class-field:
    private Player player;
    private Maze maze = Maze.getInstance();
    private boolean win = false;
    private boolean firstOutput = true;
    
    //Logger
    private static final Logger LOGGER = Logger.getLogger(GameMechanic.class.getName()); // NO PMD - Name is fine
    
    @Override
    public String toString()
    {
        //add player symbol onto grid after all adjustment to the maze structure output
        maze.playerToGrid(player.getGridRow(), player.getGridCol());

        //for the first output of maze
        if(firstOutput)
        {
            //re-move the player to the starting point to interact properly with starting square
            String interactStr = configPlayerLocation(player.getCurrRow(), player.getCurrCol());
            firstOutput = false;

            return maze.toString() + interactStr;
        }

        return maze.toString();
    }

    public String getBag()
    {
        String result = "";
        for(String key: player.getBagK())
        {
            result += key + "ô\033[m";
        }
        return result;
    }

    public String move(String input) throws GameException
    {
        String result;
        if(input.equals("w"))
        {
            result = moveValid(player.getCurrRow() - 1, player.getCurrCol(), player.getGridRow() - 1, player.getGridCol());
        }
        else if(input.equals("a"))
        {
            result = moveValid(player.getCurrRow(), player.getCurrCol() - 1, player.getGridRow(), player.getGridCol() - 2);
        }
        else if(input.equals("d"))
        {
            result = moveValid(player.getCurrRow(), player.getCurrCol() + 1, player.getGridRow(), player.getGridCol() + 2);
        }
        else if(input.equals("s"))
        {
            result = moveValid(player.getCurrRow() + 1, player.getCurrCol(), player.getGridRow() + 1, player.getGridCol());
        }
        else
        {
            throw new GameException("Invalid input direction!");
        }

        return result;
    }

    private String moveValid(int toRow, int toCol, int rowGap, int colGap) throws GameException
    {
        String result = "";
        //check if it's a door that blocks the way, allow the player through if possessing the right key
        if(maze.getGrid()[rowGap][colGap].contains("±"))
        {
            boolean match = false;
            for(String key: player.getBagK())
            {
                if(maze.getGrid()[rowGap][colGap].contains(key))
                {
                    match = true;
                    configPlayerLocation(toRow, toCol);
                    result = "\nYou have successfully unlocked the door!";
                    //once found the matching key, escape loop
                    break;
                }
            }

            if(match == false)
            {
                throw new GameException("You don't have the key which matches the color of this door to open it!");
            }
        }
        //if nothing is blocking, reposition the player on the maze
        else if(maze.getGrid()[rowGap][colGap].equals(" "))
        {
            result = configPlayerLocation(toRow, toCol);
        }
        else
        {
            throw new GameException("Can't move to that direction!");
        }

        return result;
    }

    public String configPlayerLocation(int currRow, int currCol)
    {
        LOGGER.info(() -> "currRow for player here is " + currRow + " and currCol is " + currCol);

        if(player == null)
        {
            player = new Player(currRow, currCol);
            maze.setMazeSquare(currRow, currCol, new StartP(maze.getMazeSquare(currRow, currCol)));
        }
        else
        {
            player.setCurrRow(currRow);
            player.setCurrCol(currCol);
        }

        return interactSqr();
    }

    private String interactSqr()
    {
        String result = "";
        List<Key> removedList = new ArrayList<>();

        for(Feature feat: maze.getMazeSquare(player.getCurrRow(), player.getCurrCol()).getFeatures())
        {
            String featName = feat.getClass().getName();
            if(featName.equals("Message"))
            {
                result += "\n" + ((Message)feat).getMessage() + "\n";
            }
            else if(featName.equals("Key"))
            {
                boolean bagInclude = false;

                //retrieve the key code from the square
                Key key = (Key)feat;

                if(player.getBagK().isEmpty())
                {
                    removedList.add(key);
                    
                    //add retrieved key to the bag
                    player.addKey(key.getColorCode());

                    result += "\nYou have picked up a " + key.getColorCode() + "ô\033[m key\n";             
                }
                else
                {
                    for(String playerKey: player.getBagK())
                    {
                        if(playerKey.equals(key.getColorCode()))
                        {
                            bagInclude = true;
                            result += "\nAnother key of color " + key.getColorCode() + "ô\033[m has already been collected. Leave this key behind!\n";
                            //already have that key type, escape loop
                            break;
                        }
                    }

                    //remove the key from the maze outside of for-loop to prevent Concurrent modification
                    if(!bagInclude)
                    {
                        removedList.add(key);
                        
                        //add retrieved key to the bag
                        player.addKey(key.getColorCode());
                                        
                        result += "\nYou have picked up a " + key.getColorCode() + "ô\033[m key\n";
                    }
                }
            }
            else if(featName.equals("EndP"))
            {
                win();
                //Player has beaten the maze => escape the function!
                result += "\nCongratulations, player! You have reached the gate of hell. Now you have to pay for all of your sins...";       
            }
        }

        //remove the collected key(s) from the maze square
        if(!removedList.isEmpty())
        {
            for(Key removed: removedList)
            {
                maze.removeKey(player.getCurrRow(), player.getCurrCol(), removed);
            }
        }
        
        return result;
    }

    private void win()
    {
        win = true;
    }

    public boolean isWin()
    {
        return win;
    }
}

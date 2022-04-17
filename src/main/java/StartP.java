

/**
 * Purpose: a feature to modify/notify the game of having a starting point for the player
 * Name: Minh Vu
 * Last modified date: 14/04/2022
 */

@SuppressWarnings("PMD.NoPackage")
public class StartP extends Feature
{
    //Constructor:
    public StartP(Square next)
    {
        super(next);
        //set this square having a starting point
        setStartP(true);
    }
}

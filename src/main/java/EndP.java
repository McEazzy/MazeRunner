/**
 * Purpose: end point feature to mark the ending/winning point of the game
 * Name: Minh Vu
 * Last modified date: 14/04/2022
 */

@SuppressWarnings("PMD.NoPackage")
public class EndP extends Feature
{   
    //Constructor:
    public EndP(Square next)
    {
        super(next);
        //set this square to have an end point
        setEndP(true);
    }
}

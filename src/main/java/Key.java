/**
 * Purpose: holds Key feature with a specific color placed in a maze square to be collected by player
 * Name: Minh Vu
 * Last modified date: 14/04/2022
 */

@SuppressWarnings("PMD.NoPackage")
public class Key extends WithColor 
{
    //Constructor:
    public Key(Square next, int colorIdx)
    {
        super(next, colorIdx);
    }
}

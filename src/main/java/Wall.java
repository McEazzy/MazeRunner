

/**
 * Purpose: holds Wall feature to be put around a maze square
 * Name: Minh Vu
 * Last modified date: 14/04/2022
 */

@SuppressWarnings("PMD.NoPackage")
public class Wall extends Feature
{
    //Class-field:
    private boolean vertical;

    //Constructor:
    public Wall(Square next, boolean vertical)
    {
        super(next);
        this.vertical = vertical;
    }

    //Accessor:
    public boolean isVertical()
    {
        return vertical;
    }
}

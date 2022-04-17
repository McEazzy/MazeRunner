/**
 * Purpose: holds a Door structure feature to be put around a maze square
 * Name: Minh Vu
 * Last modified date: 14/04/2022
 */

@SuppressWarnings("PMD.NoPackage")
public class Door extends WithColor
{
    //Class-field:
    private boolean vertical;

    //Constructor:
    public Door(Square next, int colorIndex, boolean vertical)
    {
        super(next, colorIndex);
        this.vertical = vertical;
    }

    //Accessor:
    public boolean isVertical()
    {
        return vertical;
    }
}

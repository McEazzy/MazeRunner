

/**
 * Purpose: abstract class that allows object with color attribute to inherit
 * Name: Minh Vu
 * Last modified date: 14/04/2022
 */

@SuppressWarnings("PMD.NoPackage")
public abstract class WithColor extends Feature
{
    //Constant:
    //ASCII string code presenting as colors: red, green, yellow/orange, blue, magenta, and cyan.
    private static final String[] ColorCodes = {"\033[31m","\033[32m","\033[33m","\033[34m","\033[35m","\033[36m"};
    private int colorIndex;
    
    //Constructor:
    public WithColor(Square next, int colorIndex)
    {
        super(next);
        this.colorIndex = colorIndex;
    }

    //Accessor:
    public String getColorCode()
    {
        return ColorCodes[colorIndex];
    }
}

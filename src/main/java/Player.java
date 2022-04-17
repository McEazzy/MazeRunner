
/**
 * Purpose: primary class model to hold all the player's data
 * Name: Minh Vu
 * Last modified date: 14/04/2022
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("PMD.NoPackage")
public class Player 
{
    //Class-field:
    private List<String> bagKey;
    private int currRow;
    private int currCol;
    private int currGridRow;
    private int currGridCol;

    //Constructor:
    public Player(int currRow, int currCol)
    {
        bagKey = new ArrayList<>();
        setCurrRow(currRow);
        setCurrCol(currCol);
    }

    //Accessor:
    public List<String> getBagK()
    {
        return Collections.unmodifiableList(bagKey);
    }

    public int getCurrRow()
    {
        return currRow;
    }

    public int getCurrCol()
    {
        return currCol;
    }

    public int getGridRow()
    {
        return currGridRow;
    }

    public int getGridCol()
    {
        return currGridCol;
    }

    //Mutator:
    public void addKey(String keyCode)
    {
        bagKey.add(keyCode);
    }

    public void setCurrRow(int currRow)
    {
        this.currRow = currRow;
        currGridRow = currRow * 2 + 1; 
    }

    public void setCurrCol(int currCol)
    {
        this.currCol = currCol;
        currGridCol = currCol * 4 + 2;
    }
}
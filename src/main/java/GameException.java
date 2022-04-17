
/**
 * Purpose: a custom Exception class to handle/catch fatal errors of the game, which cause the game to crash!
 * Name: Minh Vu
 * Last modified date: 14/04/2022
 */
 
@SuppressWarnings("PMD.NoPackage")
public class GameException extends Exception
{
    //Constructor:
    public GameException(String msg)
    {
        super(msg);
    }

    public GameException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}

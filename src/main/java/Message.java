/**
 * Purpose: holds a feature of message hidden in a maze square
 * Name: Minh Vu
 * Last modified date: 14/04/2022
 */

@SuppressWarnings("PMD.NoPackage")
public class Message extends Feature
{
    //Class-field:
    private String message;

    //Constructor:
    public Message(Square next, String message)
    {
        super(next);
        this.message = message;
    }

    //Accessor:
    public String getMessage()
    {
        return message;
    }
}

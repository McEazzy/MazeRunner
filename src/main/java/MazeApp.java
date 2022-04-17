/**
 * Purpose: main program starter class
 * Name: Minh Vu
 * Last modified date: 14/04/2022
 */
import java.util.Scanner;

@SuppressWarnings("PMD.NoPackage")
public class MazeApp
{
    public static void main(String[] args)
    {
        String warning = null;
        try (Scanner scanner = new Scanner(System.in)) 
        {
            GameMechanic mech = new GameMechanic();
            GameLoader game = new GameLoader(mech);

            try
            {
                warning = game.readFile();
            }
            catch(GameException e)
            {
                System.out.println("Game crash! " + e.getMessage());
            }
            
            //Pre-game warnings:
            if(warning != null)
            {
                System.out.println("\n"+ warning + "\nPush 'c' to ignore the warning/errors and continue the game anyway (Otherwise exit the program):");
                //terminate the program if user input is not 'c'
                if(!scanner.nextLine().equals("c"))
                {
                    //jump to the end of method main & terminate program
                    return;
                }
            }

            String validMessage = "";
            //Clear screen at the start of maze being drawn
            System.out.print("\033[2J"); 
            do
            {
                try
                {
                    System.out.println(game.toString() + validMessage + "\nPlayer's bag contains key(s) of each color: " + mech.getBag()
                        + "\n\nEnter a movement please: 'w' to go up, 'a' to go to the left, 's' to go down, 'd' to go right");
                    validMessage = mech.move(scanner.nextLine()) + "\n";
                    //Clear screen after each iteration anyway
                    System.out.print("\033[2J"); 
                }
                catch(GameException e)
                {
                    //clear screen and indicate error in next iteration
                    System.out.println("\033[2J" + e.getMessage());
                } 
            }while(!mech.isWin());

            //Ending output
            System.out.println(game.toString() + validMessage);
        }       
    }
}

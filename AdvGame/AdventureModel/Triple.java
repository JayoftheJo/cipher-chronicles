package AdventureModel;
import java.util.Random;
import java.util.Scanner;
    /**
     * This is the Triple or Rock/Paper Scissors Mini Game, it is a concrete implementation of MiniGame
    */
public class Triple implements miniGame {
    /**
     * just a flag boolean that returns true if user wins and false if user loses
     */
    private boolean isWin = false;
    @Override
    public void playminiGame() {
        // Implement the game logic here
        String[] choices = {"rock", "paper", "scissors"};

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your choice (rock/paper/scissors): ");
        String userChoice = scanner.next();

        Random rand = new Random();
        String computerChoice = choices[rand.nextInt(choices.length)];

        System.out.println("\nUser chose " + userChoice + ", computer chose " + computerChoice + ".\n");

        if (userChoice.equals(computerChoice)) {
            System.out.println("It's a tie! You will still lose some health! You must play again to move to the next room.");
        } else if ((userChoice.equals("rock") && computerChoice.equals("scissors")) ||
                (userChoice.equals("scissors") && computerChoice.equals("paper")) ||
                (userChoice.equals("paper") && computerChoice.equals("rock"))) {
            System.out.println("User wins! You can now move to the next room.");
            isWin = true;

        } else {
            System.out.println("Computer wins! You will lose some health.");


        }
    }
    @Override
    public boolean Won(){
        return isWin;
    }

}

package AdventureModel;

import java.util.Random;
import java.util.Scanner;

public class coinFlip implements miniGame {

    private boolean isWin = false;
    @Override
    public void playminiGame() {
        // Implement the coin flip game logic here
        String[] choices = {"heads", "tails"};

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your choice (heads/tails): ");
        String userChoice = scanner.next().toLowerCase();

        Random rand = new Random();
        String coinResult = choices[rand.nextInt(choices.length)];

        System.out.println("\nYou chose " + userChoice + ", coin shows " + coinResult + ".\n");

        if (userChoice.equals(coinResult)) {
            System.out.println("Congratulations! You guessed it right. You can now move to the next room.");
            isWin = true;

        } else {
            System.out.println("Oops! Wrong guess. You will lose some health.");
        }
    }
    @Override
    public boolean Won(){
        return isWin;
    }
}

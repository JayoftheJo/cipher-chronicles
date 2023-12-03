package AdventureModel;

import java.util.Random;
import java.util.Scanner;

    /**
    * This is Dice Mini Game, it is a concrete implementation of MiniGame
    */
public class Dice implements miniGame {
    /**
     * This is simple boolean that returns whether the user wins or loses
     */
    private boolean isWin = false;
    @Override
    public void playminiGame() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Roll the dice! Press enter to roll.");
        scanner.nextLine();

        Random rand = new Random();
        int userRoll = rand.nextInt(6) + 1; // Assuming a standard six-sided die

        int computerRoll = rand.nextInt(6) + 1;

        System.out.println("\nYou rolled a " + userRoll + ", computer rolled a " + computerRoll + ".\n");

        if (userRoll == computerRoll) {
            System.out.println("Congratulations! You rolled the same as the computer. You can now move to the next room.");
            isWin = true;

        } else {
            System.out.println("Oops! You didn't roll the same as the computer. You will lose some health.");
        }
    }
    @Override
    public boolean Won(){
        return isWin;
    }
}


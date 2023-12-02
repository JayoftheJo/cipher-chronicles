package AdventureModel;

/**
 * This is the miniGame interface, it is the base to create any miniGame and has 2 basic feauture
 */
public interface miniGame {

    /**
     * playminiGame method allows for the user to play the miniGame
     */
    public void playminiGame();

    /**
     * Won method simply returns a boolean, stating whether the user has won or lost
     */

    public boolean Won();
}

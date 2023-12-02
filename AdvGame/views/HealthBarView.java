package views;

import AdventureModel.Player;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Class HealthBarView
 *
 * Represents a Health bar, implementing the BarView
 */
public class HealthBarView implements BarView {

    private Rectangle background;
    private Rectangle onTop;

    private final int B_WIDTH = 160;
    private final int B_HEIGHT = 20;

    private Player player;

    private StackPane healthBar;

    Object view;


    /**
     * HealthBarView Constructor
     * @param player the player playing the game
     */
    public HealthBarView(Player player, Object view){

        // set the player to access health and totalHealth from
        this.player = player;

        this.view = view;


        // Set the parts of the health bar
        // the back layer of the health bar gets the whole width
        this.background = new Rectangle(B_WIDTH, B_HEIGHT);

        initState();

    }

    /**
     * Initial state of the health bar
     */
    public void initState() {
        // the top layer occupies space synonymous to player's current health
        this.onTop = new Rectangle(B_WIDTH, B_HEIGHT);
        player.changeHealth(player.TOTAL_HEALTH - player.getHealth());
        background.setFill(Color.WHITE);
        onTop.setFill(Color.GREEN);

        // put these 2 on top of each other
        healthBar = new StackPane(background, onTop);

        // to make sure the top layer changes left to right
        healthBar.setAlignment(Pos.CENTER_LEFT);
    }

    /**
     * Changes the player's health
     * @param howMuch the value by which to increase the player health
     */
    public void change(int howMuch) {
        PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
        if (howMuch >= 0){
            // Makes the bar a lighter green for 0.2 secs to show heal
            onTop.setFill(Color.LIGHTGREEN);
            pause.setOnFinished(actionEvent -> {

                // Change the health bar according based on player's increased health when within bounds
                if (!(this.player.getHealth() + howMuch >= 100)){
                    double percentage = ((double) (this.player.getHealth() + howMuch) / this.player.getHealth());
                    onTop.setWidth(percentage * onTop.getWidth());
                    this.player.changeHealth(howMuch);
                }
                // If more than bounds, set the health bar to max possible and update player health accordingly
                else {
                    onTop.setWidth(B_WIDTH);
                    this.player.changeHealth(player.TOTAL_HEALTH - this.player.getHealth());
                }

                // to change colour back from the light green after the pause
                onTop.setFill(Color.GREEN);
            });
        }
        else {
            // Makes the bar red for 0.2 secs to show damage
            onTop.setFill(Color.RED);
            pause.setOnFinished(actionEvent ->  {

                // If less than bounds, set the health bar to min possible and update player health accordingly
                if (this.player.getHealth() + howMuch <= 0){
                    onTop.setWidth(0);
                    this.player.changeHealth(-this.player.getHealth());
                    //Game Over
                    if (view instanceof AdventureGameView){
                        ((AdventureGameView) view).gameOver();
                    }
                    else if(view instanceof BossView){
                        ((BossView) view).gameOver();
                    }

                }
                // Change the health bar according based on player's decreased health when within bounds
                else{
                    double percentage = (double) (this.player.getHealth() + howMuch) / this.player.getHealth();
                    onTop.setWidth(percentage * onTop.getWidth());
                    this.player.changeHealth(howMuch);

                }
                onTop.setFill(Color.GREEN);
            });
        }

        // to change colour back from the light green after the pause
        pause.play();

    }

    /**
     * Getter for the health bar
     * @return Returns this health bar
     */
    public StackPane get() {
        return healthBar;
    }
}

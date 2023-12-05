package views.bars;

import AdventureModel.Player;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import views.AdventureGameView;
import views.BossView;

/**
 * Class HealthBarView
 * <p>
 * Represents a Health bar, implementing the BarView
 */
public class HealthBarView implements BarView {

    private Rectangle background;
    private Rectangle onTop;

    private final int B_WIDTH = 160;
    private final int B_HEIGHT = 20;

    private Player player;

    private StackPane healthBar;


    /**
     * HealthBarView Constructor
     *
     * @param player the player playing the game
     */
    public HealthBarView(Player player) {

        // set the player to access health and totalHealth from
        this.player = player;

        // Set the parts of the health bar
        // the back layer of the health bar gets the whole width
        this.background = new Rectangle(B_WIDTH, B_HEIGHT);

        initState();

        // put these 2 on top of each other
        healthBar = new StackPane(background, onTop);

        // to make sure the top layer changes left to right
        healthBar.setAlignment(Pos.CENTER_LEFT);

    }

    /**
     * Initial state of the health bar
     */
    public void initState() {
        // the top layer occupies space synonymous to player's current health
        this.onTop = new Rectangle(B_WIDTH, B_HEIGHT);
        player.updateHealth(player.TOTAL_HEALTH - player.getHealth());
        background.setFill(Color.WHITE);
        onTop.setFill(Color.GREEN);

    }

    /**
     * Changes the player's health
     *
     * @param howMuch the value by which to increase the player health
     */
    public void change(int howMuch) {
        if (howMuch >= 0) {


            // Change the health bar according based on player's increased health when within bounds
            if (!(this.player.getHealth() + howMuch >= 100)) {
                double percentage = ((double) (this.player.getHealth() + howMuch) / this.player.getHealth());
                onTop.setWidth(percentage * onTop.getWidth());
                this.player.updateHealth(howMuch);
            }
            // If more than bounds, set the health bar to max possible and update player health accordingly
            else {
                onTop.setWidth(B_WIDTH);
                this.player.updateHealth(player.TOTAL_HEALTH - this.player.getHealth());
            }


        } else {


            // If less than bounds, set the health bar to min possible and update player health accordingly
            if (this.player.getHealth() + howMuch <= 0) {
                onTop.setWidth(0);
                this.player.updateHealth(-this.player.getHealth());

            }
            // Change the health bar according based on player's decreased health when within bounds
            else {
                double percentage = (double) (this.player.getHealth() + howMuch) / this.player.getHealth();
                onTop.setWidth(percentage * onTop.getWidth());
                this.player.updateHealth(howMuch);

            }

        }


    }


    /**
     * Getter for the health bar
     *
     * @return Returns this health bar
     */
    public StackPane get() {
        return healthBar;
    }
}

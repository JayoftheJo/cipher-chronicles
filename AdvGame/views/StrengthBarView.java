package views;
import AdventureModel.Player;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class StrengthBarView implements BarView{
    private Rectangle background;
    private Rectangle onTop;

    private final int B_WIDTH = 160;
    private final int B_HEIGHT = 20;

    private Player player;

    private StackPane StrengthBar;

    private boolean usable;


    /**
     * HealthBarView Constructor
     * @param player the player playing the game
     */
    public StrengthBarView(Player player, Object view){

        this.usable = false;
        if (view instanceof BossView) {
            this.usable = true;
        }
        // set the player to access health and totalHealth from
        this.player = player;

        // Set the parts of the health bar
        // the back layer of the health bar gets the whole width
        this.background = new Rectangle(B_WIDTH, B_HEIGHT);

        // the top layer occupies space synonymous to player's current health
        this.onTop = new Rectangle(0, B_HEIGHT);

        colour();

        initState();

    }

    /**
     * Initial state of the strength bar.
     */
    @Override
    public void initState() {
        // After a pause make player strength and onTop's width to their default value of 0
        PauseTransition pause2 = new PauseTransition(Duration.seconds(0.2));
        pause2.setOnFinished(actionEvent1 -> {
            onTop.setWidth(0);
            this.player.changeStrength(-player.getStrength());
        });
        pause2.play();

        // put the top and bottom on top of each other
        StrengthBar = new StackPane(background, onTop);

        // to make sure the top layer changes left to right
        StrengthBar.setAlignment(Pos.CENTER_LEFT);
    }

    /**
     * Changes the player's health
     * Precondition: howMuch >= 0
     * @param howMuch the value by which to increase the player health
     */
    @Override
    public void change(int howMuch) {
        if (usable) {
            // Makes the bar a darker red for 0.2 secs to show heal
            onTop.setFill(Color.DARKRED);
            PauseTransition pause = new PauseTransition(Duration.seconds(0.2));

            pause.setOnFinished(actionEvent -> {

                // Change the strength bar according based on player's increased strength when within bounds
                if (!(this.player.getStrength() + howMuch >= 5)) {
                    if (player.getStrength() == 0){
                        onTop.setWidth( ((double)howMuch /5) *B_WIDTH);
                    }
                    else {
                        double percentage = ((double) (this.player.getStrength() + howMuch) / this.player.getStrength());
                        onTop.setWidth(percentage * onTop.getWidth());
                    }
                    this.player.changeStrength(howMuch);
                }
                // If more than bounds, set the strength bar to max possible and update player strength accordingly
                else {
                    onTop.setWidth(B_WIDTH);
                    this.player.changeStrength(player.FULL_STRENGTH - this.player.getStrength());
                }

                // to change colour back from the dark red after the pause
                onTop.setFill(Color.RED);
            });
            pause.play();
        }
    }

    /**
     * Set the colour for the strength bar
     * - Normal colours in Boss Room
     * - Greyed out otherwise
     */
    public void colour(){
        if (usable){
            background.setFill(Color.WHITE);
            onTop.setFill(Color.RED);
        }
        else{
            background.setFill(Color.gray(.2));
            onTop.setFill(Color.gray(.2));
        }
    }

    /**
     * Getter for the strength bar
     * @return Returns this strength bar
     */
    public StackPane get() {
        return StrengthBar;
    }
}

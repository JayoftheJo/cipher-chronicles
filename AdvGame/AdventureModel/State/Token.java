package AdventureModel.State;

import views.BossView;

/**
 * The Token state of AdventureObject
 */
public class Token implements State {
    private int count; // to know how many user used

    BossView view; // to direct us to the appropriate action for using this object

    /**
     * Token Constructor.
     */
    public Token() {
        count = 0; // No objects of this type has been used yet
    }

    /**
     * Execute for this state of the object
     * If 3 of these objects have been used, activate the strength button for use
     */
    @Override
    public void execute() {
        count++;
        if (count == 3 || this.view.getModel().getPlayer().getStrength() == 5) {
            count = 0;
            this.view.activateStrengthButton();

        }
    }

    /**
     * Set the view to delegate the execute task to
     *
     * @param view the boss view for this game
     */
    public void setView(BossView view) {
        this.view = view;
    }
}

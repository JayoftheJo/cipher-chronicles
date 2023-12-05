package AdventureModel.State;

import views.BossView;

/**
 * The Lucky Item state of AdventureObject
 */
public class LuckyItem implements State {
    BossView view;

    /**
     * LuckyItem Constructor
     */
    public LuckyItem() {

    }

    /**
     * Opens up the special attack button for user for a 20% chance of an attack in boss room
     */
    @Override
    public void execute() {
        this.view.specialAttackChance();
    }


    /**
     * Set the view to delegate the execute task to
     *
     * @param view the boss view for this game
     */
    @Override
    public void setView(BossView view) {
        this.view = view;
    }
}

package AdventureModel.State;

import views.BossView;

/**
 * The Half Damage Item State of Adventure Object
 */
public class HalfDamageItem implements State {
    BossView view;

    /**
     * HalfDamageItem Constructor
     */
    public HalfDamageItem() {

    }


    /**
     * Get rid of hald the boss' health in boss room
     */
    @Override
    public void execute() {
        this.view.halfDamage();
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

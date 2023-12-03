package AdventureModel;

import views.BossView;

public class InvincibleItem implements State {

    BossView view; // to direct us to the appropriate action for using this object

    /**
     * Invincible Item Constructor.
     */
    public InvincibleItem(){

    }

    @Override
    public void execute() {
        this.view.invincible();
    }

    public void setView(BossView view){
        this.view = view;
    }

}

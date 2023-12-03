package AdventureModel.State;

import views.BossView;

public class HalfDamageItem implements State {
    BossView view;

    public HalfDamageItem(){

    }

    @Override
    public void execute() {
        this.view.halfDamage();
    }

    public void setView(BossView view) {
        this.view = view;
    }
}

package AdventureModel;

import BossFactory.trollBoss;
import views.BossView;

public class Token implements State{
    private int count;
    BossView view;

    public Token(){
        count = 0;
    }

    @Override
    public void execute(Player player, trollBoss boss) {
        count++;
        if (count == 3){
            this.view.activateStrengthButton();
            count = 0;
        }
    }

    public void setView(BossView view){
        this.view = view;
    }
}

package AdventureModel;

import BossFactory.trollBoss;

public interface State{
    public void execute(Player player, trollBoss boss);
}

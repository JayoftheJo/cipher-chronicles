package AdventureModel.State;


import views.BossView;

public interface State{
    public void execute();

    public void setView(BossView view);
}

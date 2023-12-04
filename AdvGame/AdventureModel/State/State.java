package AdventureModel.State;


import views.BossView;

/**
 * The State Interface, provides a base for the different states of AdventureObject
 */
public interface State{
    public void execute();

    public void setView(BossView view);
}

package Commands.MovementCommands;

import AdventureModel.AdventureGame;
import Commands.Command;

/**
 * A command to move the player into an adjacent room.
 */
public abstract class MovementCommand implements Command {

    protected final AdventureGame model; //AdventureGame model to execute commands onto.

    /**
     * MovementCommand constructor.
     *
     * @param model AdventureGame from which to execute movements off.
     */
    public MovementCommand(AdventureGame model) {
        this.model = model;
    }

    /**
     * Attempt to move the player into a specified adjacent room.
     */
    public abstract void execute();

}

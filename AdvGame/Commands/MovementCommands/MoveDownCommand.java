package Commands.MovementCommands;

import AdventureModel.AdventureGame;
import Commands.Command;

/**
 * A command to move to the downward adjacent room.
 */
public class MoveDownCommand extends MovementCommand {

    /**
     * MoveDownCommand constructor.
     *
     * @param model AdventureGame from which to execute movements off.
     */
    public MoveDownCommand(AdventureGame model) { super(model); }

    /**
     * Attempt to move the player to the downward adjacent room.
     */
    public void execute() { model.movePlayer("DOWN"); }

}

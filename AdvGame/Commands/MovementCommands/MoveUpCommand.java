package Commands.MovementCommands;

import AdventureModel.AdventureGame;

/**
 * A command to move to the upward adjacent room.
 */
public class MoveUpCommand extends MovementCommand {

    /**
     * MoveUpCommand constructor.
     *
     * @param model AdventureGame from which to execute movements off.
     */
    public MoveUpCommand(AdventureGame model) {
        super(model);
    }

    /**
     * Attempt to move the player to the upward adjacent room.
     */
    public void execute() { model.movePlayer("UP"); }

}

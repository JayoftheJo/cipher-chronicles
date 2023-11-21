package Commands.MovementCommands;

import AdventureModel.AdventureGame;

/**
 * A command to move to the rightward adjacent room.
 */
public class MoveRightCommand extends MovementCommand {

    /**
     * MoveRightCommand constructor.
     *
     * @param model AdventureGame from which to execute movements off.
     */
    public MoveRightCommand(AdventureGame model) {
        super(model);
    }

    /**
     * Attempt to move the player to the rightward adjacent room.
     */
    public void execute() { model.movePlayer("RIGHT"); }

}

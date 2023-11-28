package Commands.MovementCommands;

import AdventureModel.AdventureGame;

/**
 * A command to move to the leftward adjacent room.
 */
public class MoveLeftCommand extends MovementCommand {

    /**
     * MoveLeftCommand constructor.
     *
     * @param model AdventureGame from which to execute movements off.
     */
    public MoveLeftCommand(AdventureGame model) { super(model); }

    /**
     * Attempt to move the player to the leftward adjacent room.
     */
    public void execute() { model.movePlayer("LEFT"); }

}

package Commands;

import AdventureModel.AdventureGame;
import javafx.scene.control.Label;

/**
 * A command to inspect the possible directions a player may go.
 */
public class InspectCommand implements Command {

    private final Label field; // Text field where to display the possible directions.
    private final AdventureGame model; // AdventureGame where to get possible directions from.

    /**
     * InspectCommand constructor.
     *
     * @param field The label where to display possible directions in text.
     * @param model The current AdventureGame being played.
     */
    public InspectCommand(Label field, AdventureGame model) {
        this.field = field;
        this.model = model;
    }
    public void execute() {
        field.setText("Your possible moves here are: \n" + model.getPlayer().getCurrentRoom().getCommands());
    }
}

package Commands;

/**
 * A command center from which commands are executed.
 */
public class CommandCenter {

    private Command command; // The command to be executed.

    /**
     * Set a given command to be executed.
     * @param command The command to be executed by the command center.
     */
    public void setCommand(Command command) {
        this.command = command;
    }

    /**
     * Execute the currently set command.
     */
    public void execute() {
        command.execute();
    }

    /**
     * Return the command currently assigned to this command center.
     *
     * @return the current command.
     */
    public Command getCommand() {
        return command;
    }

}

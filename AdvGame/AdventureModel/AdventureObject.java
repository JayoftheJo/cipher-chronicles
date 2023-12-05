package AdventureModel;

import AdventureModel.State.State;

import java.io.Serializable; //you will need this to save the game!
import java.util.ArrayList;

/**
 * This class keeps track of the props or the objects in the game.
 * These objects have a name, description, and location in the game.
 * The player with the objects can pick or drop them as they like and
 * these objects can be used to pass certain passages in the game.
 */
public class AdventureObject implements Serializable {
    /**
     * The name of the object.
     */
    private String objectName;

    /**
     * The description of the object.
     */
    private String description;

    private String helpTxt;

    private State state;

    /**
     * The location(s) of the object.
     */
    private ArrayList<Room> location = null;

    /**
     * Adventure Object Constructor
     * ___________________________
     * This constructor sets the name, description, and location of the object.
     *
     * @param name        The name of the Object in the game.
     * @param description One line description of the Object.
     * @param location    The location of the Object in the game.
     */
    public AdventureObject(String name, String description, ArrayList<Room> location, String helpTxt) {
        this.objectName = name;
        this.description = description;
        this.location = location;
        this.helpTxt = helpTxt;
        this.state = null;
    }

    /**
     * Switch to or set a different state for this object
     *
     * @param state the state to switch to
     */
    public void changeState(State state) {
        this.state = state;
    }

    /**
     * Getter method for the name attribute.
     *
     * @return name of the object
     */
    public String getName() {
        return this.objectName;
    }

    /**
     * Getter method for the description attribute.
     *
     * @return description of the game
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * This method returns the location of the object if the object is still in
     * the room. If the object has been pickUp up by the player, it returns null.
     *
     * @return returns the location of the object if the objects is still in
     * the room otherwise, returns null.
     */
    public ArrayList<Room> getLocation() {
        return this.location;
    }

    /**
     * Returns what state this object is at
     *
     * @return state of this object
     */
    public State getState() {
        return state;
    }

    /**
     * Returns the use of this object
     *
     * @return the purpose of this object
     */
    public String getHelpTxt() {
        return helpTxt;
    }

}

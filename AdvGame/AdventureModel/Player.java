package AdventureModel;

import views.bars.BarView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class keeps track of the progress
 * of the player in the game.
 */
public class Player implements Serializable {
    /**
     * The current room that the player is located in.
     */
    private Room currentRoom;
    private int currHealth;

    public final int TOTAL_HEALTH = 100;
    private int strength;

    private BarView healthBar;

    private BarView strengthBar;

    public final int FULL_STRENGTH = 6;

    /**
     * The list of items that the player is carrying at the moment.
     */
    public ArrayList<AdventureObject> inventory;

    /**
     * Adventure Game Player Constructor
     * @param currentRoom the current room the player is in
     */
    public Player(Room currentRoom) {
        this.currHealth = TOTAL_HEALTH;
        this.strength = 0;
        this.inventory = new ArrayList<AdventureObject>();
        this.currentRoom = currentRoom;
    }

    /**
     * This method adds an object into players inventory if the object is present in
     * the room and returns true. If the object is not present in the room, the method
     * returns false.

     *
     * @param object name of the object to pick up
     * @return true if picked up, false otherwise
     */
    public boolean takeObject(String object){
        if(this.currentRoom.checkIfObjectInRoom(object)){
            AdventureObject object1 = this.currentRoom.getObject(object);
            this.currentRoom.removeGameObject(object1);
            this.addToInventory(object1);
            return true;
        } else {
            return false;
        }
    }


    /**
     * checkIfObjectInInventory
     * __________________________
     * This method checks to see if an object is in a player's inventory.
     *
     * @param s the name of the object
     * @return true if object is in inventory, false otherwise
     */
    public boolean checkIfObjectInInventory(String s) {
        for(int i = 0; i<this.inventory.size();i++){
            if(this.inventory.get(i).getName().equals(s)) return true;
        }
        return false;
    }


    /**
     * This method drops an object in the players inventory and adds it to the room.
     * If the object is not in the inventory, the method does nothing.
     *
     * @param s name of the object to drop
     */
    public void dropObject(String s) {
        for(int i = 0; i<this.inventory.size();i++){
            if(this.inventory.get(i).getName().equals(s)) {
                this.currentRoom.addGameObject(this.inventory.get(i));
                this.inventory.remove(i);
                break; // since this version supports duplications, we have to stop at 1 removal
            }
        }
    }

    /**
     * Setter method for the current room attribute.
     *
     * @param currentRoom The location of the player in the game.
     */
    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    /**
     * This method adds an object to the inventory of the player.
     *
     * @param object Prop or object to be added to the inventory.
     */
    public void addToInventory(AdventureObject object) {
        this.inventory.add(object);
    }


    /**
     * Getter method for the current room attribute.
     *
     * @return current room of the player.
     */
    public Room getCurrentRoom() {
        return this.currentRoom;
    }

    /**
     * Getter method for string representation of inventory.
     *
     * @return ArrayList of names of items that the player has.
     */
    public ArrayList<String> getInventory() {
        ArrayList<String> objects = new ArrayList<>();
        HashMap<String, Integer> count = new HashMap<>();
        for(int i=0;i<this.inventory.size();i++){
            if(count.get(this.inventory.get(i).getName()) == null){
                count.put(this.inventory.get(i).getName(), 1);
                }
            else{
                count.put(this.inventory.get(i).getName(), count.get(this.inventory.get(i).getName()) + 1);
            }
            }

        for (String str: count.keySet()){
            if (count.get(str) > 1){
                objects.add(str + " x " + count.get(str));
            }
            else {
                objects.add(str);
            }
        }

        return objects;
    }


    public void setHealthBar(BarView healthbar){
        this.healthBar = healthbar;
    }

    // Setters and getters of health and strength attributes
    public int getHealth(){
        return this.currHealth;
    }

    public int getStrength(){
        return this.strength;
    }

    public void changeHealthBar(int health){
        this.healthBar.change(health);
    }

    public void updateHealth(int health){
        this.currHealth += health;
    }

    public void setStrengthBar(BarView strengthBar){
        this.strengthBar = strengthBar;
    }

    public void changeStrengthBar(int strength){
        this.strengthBar.change(strength);
    }

    public void updateStrength(int strength){
        this.strength += strength;
    }


}

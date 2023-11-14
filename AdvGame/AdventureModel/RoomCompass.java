package AdventureModel;

import java.util.*;

public class RoomCompass {

    private final AdventureGame model;
    private boolean up; // Whether the upper room is accessible.
    private boolean down; // Whether the lower room is accessible.
    private boolean left; // Whether the left room is accessible.
    private boolean right; // Whether the right room is accessible.

    /**
     * Room Compass constructor
     *
     * @param model The AdventureCompass associated with this compass.
     */
    public RoomCompass(AdventureGame model) {
        this.model = model;
        up = false;
        down = false;
        left = false;
        right = false;
    }

    /**
     * Updates the RoomCompass based on which rooms are accessible.
     */
    public void update(){
        // Reset all compass directions.
        up = false;
        down = false;
        left = false;
        right = false;

        List<Passage> passages = this.model.getPlayer().getCurrentRoom().getMotionTable().getDirection();
        for (Passage passage: passages) {

            // Update compass directions based on whether it is accessible.
            switch (passage.getDirection().toUpperCase()) {
                case "UP" -> up = true;
                case "DOWN" -> down = true;
                case "LEFT" -> left = true;
                case "RIGHT" -> right = true;
            }
        }
    }

    /**
     * Returns whether the upper room is accessible.
     */
    public boolean getUp() { return up; }

    /**
     * Returns whether the lower room is accessible.
     */
    public boolean getDown() { return down; }

    /**
     * Returns whether the left room is accessible.
     */
    public boolean getLeft() { return left; }

    /**
     * Returns whether the right room is accessible.
     */
    public boolean getRight() { return right; }
}

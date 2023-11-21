package RoomCompass;

import AdventureModel.AdventureGame;
import AdventureModel.Passage;
import AdventureModel.PassageTable;
import RoomCompass.RoomCompass;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class RoomCompassTests {

    Passage up = new Passage("UP", "2");
    Passage down = new Passage("DOWN", "3");
    Passage left = new Passage("LEFT", "4");
    Passage right = new Passage("RIGHT", "5");

    @Test
    void roomCompassTestAllRoomsFree() {
        AdventureGame game = new AdventureGame("TinyGame");
        RoomCompass compass = new RoomCompass(game);

        ArrayList<Passage> passages = new ArrayList<>();
        passages.add(up);
        passages.add(down);
        passages.add(left);
        passages.add(right);

        game.player.getCurrentRoom().getMotionTable().passageTable = passages;
        compass.update();

        assertTrue(compass.getUp());
        assertTrue(compass.getDown());
        assertTrue(compass.getLeft());
        assertTrue(compass.getRight());
    }

    @Test
    void roomCompassTestOneRoomFree() {
        AdventureGame game = new AdventureGame("TinyGame");
        RoomCompass compass = new RoomCompass(game);

        ArrayList<Passage> passages = new ArrayList<>();
        passages.add(up);

        game.player.getCurrentRoom().getMotionTable().passageTable = passages;
        compass.update();

        assertTrue(compass.getUp());
        assertFalse(compass.getDown());
        assertFalse(compass.getLeft());
        assertFalse(compass.getRight());
    }

    @Test
    void roomCompassTestNoRoomsFree() {
        AdventureGame game = new AdventureGame("TinyGame");
        RoomCompass compass = new RoomCompass(game);

        ArrayList<Passage> passages = new ArrayList<>();

        game.player.getCurrentRoom().getMotionTable().passageTable = passages;
        compass.update();

        assertFalse(compass.getUp());
        assertFalse(compass.getDown());
        assertFalse(compass.getLeft());
        assertFalse(compass.getRight());
    }
}

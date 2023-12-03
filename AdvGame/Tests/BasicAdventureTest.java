package Tests;

import java.io.IOException;

import AdventureModel.AdventureGame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasicAdventureTest {
    @Test
    void getCommandsTestFirstRoom() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        String commands = game.player.getCurrentRoom().getCommands();
        assertEquals("WEST, UP, NORTH, IN, SOUTH, DOWN", commands);
    }

    @Test
    void getCommandsTestSecondRoom() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        game.movePlayer("WEST"); // Move to second room.
        String commands = game.player.getCurrentRoom().getCommands();
        assertEquals("EAST, WEST, DOWN", commands);
    }

    @Test
    void getObjectStringFirstRoom() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        String objects = game.player.getCurrentRoom().getObjectString();
        assertEquals("a water bird", objects);
    }

    @Test
    void getObjectStringSecondRoom() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        game.movePlayer("WEST"); // Move to second room.
        String objects = game.player.getCurrentRoom().getObjectString();
        assertEquals("a pirate chest", objects);
    }

    @Test
    void getObjectStringThirdRoom() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        game.movePlayer("WEST"); // Move to second room.
        game.movePlayer("WEST"); // Move to third room.
        String objects = game.player.getCurrentRoom().getObjectString();
        assertEquals("a copy of an illuminated manuscript", objects);
    }

    @Test
    void getObjectStringFifthRoom() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        game.movePlayer("WEST"); // Move to second room.
        game.movePlayer("WEST"); // Move to third room.
        game.movePlayer("WEST"); // Move to fifth room.
        String objects = game.player.getCurrentRoom().getObjectString();
        assertEquals("", objects);
    }
}

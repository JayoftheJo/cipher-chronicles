package Tests;

import AdventureModel.AdventureGame;
import AdventureModel.State.HalfDamageItem;
import AdventureModel.State.InvincibleItem;
import AdventureModel.State.LuckyItem;
import AdventureModel.State.Token;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class StateTest {

    @Test
    public void multipleTokensInRooms(){
        AdventureGame game = new AdventureGame("TinyGame");
        int num = 0;
        for (int room: game.getRooms().keySet()){
            num += (int) game.getRooms().get(room).objectsInRoom.stream().filter(node -> node.getState() instanceof Token).count();
        }

        assert num == 5;
    }

    @Test
    public void moveTokens(){
        AdventureGame game = new AdventureGame("TinyGame");
        game.interpretAction("RIGHT");
        game.interpretAction("RIGHT");
        game.interpretAction("TAKE MIGHTORBS");
        assert game.getPlayer().inventory.size() == 1;
        assert game.getPlayer().getCurrentRoom().objectsInRoom.isEmpty();
        game.interpretAction("LEFT");
        game.interpretAction("LEFT");
        game.interpretAction("LEFT");
        game.interpretAction("UP");
        game.interpretAction("DROP MIGHTORBS");
        assert game.getPlayer().inventory.isEmpty();
        assert game.getPlayer().getCurrentRoom().objectsInRoom.size() == 2;
        assert game.getPlayer().getCurrentRoom().getObjectString().equals("a strengthener x 2");
        game.interpretAction("TAKE MIGHTORBS");
        assert game.getPlayer().inventory.size() == 1;
        assert game.getPlayer().getCurrentRoom().objectsInRoom.size() == 1;
        game.interpretAction("TAKE MIGHTORBS");
        assert game.getPlayer().inventory.size() == 2;
        assert game.getPlayer().getCurrentRoom().objectsInRoom.isEmpty();
    }

    @Test
    public void getInvincibleItem() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        int count = 0;
        for (int room: game.getRooms().keySet()){
            count += (int) game.getRooms().get(room).objectsInRoom.stream().filter(node -> node.getState() instanceof InvincibleItem).count();
        }

        assert count == 1;

    }

    @Test
    public void getHalfDamageItem() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        int count = 0;
        for (int room: game.getRooms().keySet()){
            count += (int) game.getRooms().get(room).objectsInRoom.stream().filter(node -> node.getState() instanceof HalfDamageItem).count();
        }

        assert count == 1;

    }

    @Test
    public void getLuckyItem() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        int count = 0;
        for (int room: game.getRooms().keySet()){
            count += (int) game.getRooms().get(room).objectsInRoom.stream().filter(node -> node.getState() instanceof LuckyItem).count();
        }

        assert count == 1;

    }

}

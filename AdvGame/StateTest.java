import AdventureModel.*;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import views.AdventureGameView;
import views.BossView;
import views.HealthBarView;
import views.StrengthBarView;

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
        game.interpretAction("TAKE TOKEN");
        assert game.getPlayer().inventory.size() == 1;
        assert game.getPlayer().getCurrentRoom().objectsInRoom.isEmpty();
        game.interpretAction("LEFT");
        game.interpretAction("LEFT");
        game.interpretAction("LEFT");
        game.interpretAction("UP");
        game.interpretAction("DROP TOKEN");
        assert game.getPlayer().inventory.isEmpty();
        assert game.getPlayer().getCurrentRoom().objectsInRoom.size() == 2;
        assert game.getPlayer().getCurrentRoom().getObjectString().equals("a strength token x 2");
        game.interpretAction("TAKE TOKEN");
        assert game.getPlayer().inventory.size() == 1;
        assert game.getPlayer().getCurrentRoom().objectsInRoom.size() == 1;
        game.interpretAction("TAKE TOKEN");
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
}

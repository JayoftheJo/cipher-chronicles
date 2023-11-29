import java.io.IOException;

import AdventureModel.AdventureGame;
import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.StackPane;
import org.junit.jupiter.api.Test;
import views.HealthBarView;

import static org.junit.jupiter.api.Assertions.*;

public class HealthBarTest {
    public JFXPanel panel = new JFXPanel();

    @Test
    void healthBoundedTest(){
        AdventureGame game = new AdventureGame("TinyGame");
        HealthBarView healthBar = new HealthBarView(game.getPlayer());

        healthBar.increase(5);

        assert 0 <= game.getPlayer().getHealth() && game.getPlayer().getHealth() <= 100;

        healthBar.decrease(105);

        assert 0 <= game.getPlayer().getHealth() && game.getPlayer().getHealth() <= 100;

    }

    @Test
    void getHealthBarTest(){
        AdventureGame game = new AdventureGame("TinyGame");
        HealthBarView healthBar = new HealthBarView(game.getPlayer());
        assert healthBar.getHealthBar() != null;
    }

    
}

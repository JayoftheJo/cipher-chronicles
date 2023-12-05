package Tests;

import AdventureModel.AdventureGame;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;
import views.bars.HealthBarView;
import views.bars.StrengthBarView;

public class PlayerStatsTest {
    public JFXPanel panel = new JFXPanel();

    @Test
    void PlayerStatsBoundedTest(){
        AdventureGame game = new AdventureGame("TinyGame");
        HealthBarView healthBar = new HealthBarView(game.getPlayer());
        StrengthBarView strengthBar = new StrengthBarView(game.getPlayer(), game);

        healthBar.change(5);
        healthBar.change(6);

        assert 0 <= game.getPlayer().getHealth() && game.getPlayer().getHealth() <= 100;
        assert 0 <= game.getPlayer().getStrength() && game.getPlayer().getStrength() <= 5;


        healthBar.change(-105);

        assert 0 <= game.getPlayer().getHealth() && game.getPlayer().getHealth() <= 100;


    }

    @Test
    void getPlayerStatsTest(){
        AdventureGame game = new AdventureGame("TinyGame");
        HealthBarView healthBar = new HealthBarView(game.getPlayer());
        StrengthBarView strengthBar = new StrengthBarView(game.getPlayer(), game);
        assert healthBar.get() != null;
        assert strengthBar.get() != null;
    }

    
}

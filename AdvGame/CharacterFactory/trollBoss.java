package CharacterFactory;

import CharacterFactory.Character;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class trollBoss extends Character {
    public double bossHealth;
    public double bossStrength;

    public trollBoss(double health, double strength) {
        this.bossHealth = health;
        this.bossStrength = strength;
    }
}

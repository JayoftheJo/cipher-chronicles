package CharacterFactory;


/**
 * Class trollBoss.
 *
 * This class is used initialize a troll boss
 * in the battle system
 */
public class trollBoss extends Character {
    public double bossHealth;//used to represent the health attribute for the troll boss
    public double bossStrength;//used to represent the health attribute for the troll boss

    /*
     * constructor for troll boss
     */
    public trollBoss(double health, double strength) {
        this.bossHealth = health;
        this.bossStrength = strength;
    }
}

package BossFactory;


import AdventureModel.Player;

/**
 * Class trollBoss.
 *
 * This class is used initialize a troll boss
 * in the battle system
 */
public class trollBoss extends Boss {
    public int bossHealth;//used to represent the health attribute for the troll boss
    public int bossStrength;//used to represent the health attribute for the troll boss

    /*
     * constructor for troll boss
     */
    public trollBoss(int health, int strength) {
        this.bossHealth = health;
        this.bossStrength = strength;
    }

    /*
     *
     * make this character fight another boss
     * @param other
     */
    @Override
    public void attack(Player other){
        int damage = rand.nextInt(0, this.bossStrength * 5);
        other.changeHealth(-damage);
    }

    /*
     * make this boss heal themselves
     */
    @Override
    public void heal(){
        int heal = rand.nextInt(0, this.bossHealth);
        this.bossHealth += heal;
    }
}

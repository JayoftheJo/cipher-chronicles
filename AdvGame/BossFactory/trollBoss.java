package BossFactory;


import AdventureModel.Player;
import views.bars.BarView;

/**
 * Class trollBoss.
 *
 * This class is used initialize a troll boss
 * in the battle system
 */
public class trollBoss extends Boss {
    public int bossHealth;//used to represent the health attribute for the troll boss
    public int bossStrength;//used to represent the health attribute for the troll boss

    private BarView healthBar;
    private BarView strengthBar;

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
        other.changeHealthBar(-damage);
    }

    /*
     * make this boss heal themselves
     */
    @Override
    public void heal(){
        int heal = rand.nextInt(0, this.bossHealth);
        changeHealthBar(heal);
    }

    public void setHealthBar(BarView healthbar){
        this.healthBar = healthbar;
    }

    // Setters and getters of health and strength attributes
    public int getHealth(){
        return this.bossHealth;
    }

    public int getStrength(){
        return this.strength;
    }

    public void changeHealthBar(int health){
        this.healthBar.change(health);
    }

    public void updateHealth(int health){
        this.bossHealth += health;
    }

    public void setStrengthBar(BarView strengthBar){
        this.strengthBar = strengthBar;
    }

    public void changeStrengthBar(int strength){
        this.strengthBar.change(strength);
    }

    public void updateStrength(int strength){
        this.strength += strength;
    }


}

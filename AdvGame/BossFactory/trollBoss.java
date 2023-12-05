package BossFactory;


import AdventureModel.Player;
import views.bars.BarView;

/**
 * Class trollBoss.
 * <p>
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
     * and return the damage inflicted
     * @param other
     */
    @Override
    public int attack(Player other) {
        int damage = rand.nextInt(0, this.bossStrength * 5);

        // boss' special attack
        if (this.bossStrength == 100) {
            damage *= 2;
            strengthBar.initState();
        }

        other.changeHealthBar(-damage);
        return damage;
    }

    /*
     * make this boss heal themselves
     */
    @Override
    public int heal() {
        int heal = rand.nextInt(0, this.bossHealth);
        changeHealthBar(heal);
        return heal;
    }

    public void setHealthBar(BarView healthbar) {
        this.healthBar = healthbar;
    }

    // Setters and getters of health and strength attributes
    public int getHealth() {
        return this.bossHealth;
    }

    public int getStrength() {
        return this.bossStrength;
    }

    public void changeHealthBar(int health) {
        this.healthBar.change(health);
    }

    public void updateHealth(int health) {
        this.bossHealth += health;
    }

    public void setStrengthBar(BarView strengthBar) {
        this.strengthBar = strengthBar;
    }

    public void changeStrengthBar(int strength) {
        this.strengthBar.change(strength);
    }

    public void updateStrength(int strength) {
        this.bossStrength += strength;
    }


}

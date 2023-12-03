package BossFactory;

import AdventureModel.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Random;

/**
 * Character class
 *
 * This class is an abstract class used
 * as a basis for the bosses
 */
public abstract class Boss {

    //used to represent the health attribute for the boss
    public int health;

    //used to represent the strength attribute for the boss
    public int strength;

    //Images used to represent the boss
    public Image charImage;
    public ImageView charImageview;
    Random rand = new Random();

    /*
     *
     * make this character fight another boss
     * @param other
     */
    public void attack(Player other){
        int damage = rand.nextInt(0, this.strength * 5);
        other.changeHealth(-damage);
    }

    /*
     * make this boss heal themselves
     */
    public void heal(){
        int heal = rand.nextInt(0, 30);
        this.health += heal;
    }
}

package BossFactory;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Character class
 *
 * This class is an abstract class used
 * as a basis for the bosses
 */
public abstract class Boss {

    //used to represent the health attribute for the boss
    public double health;

    //used to represent the strength attribute for the boss
    public double strength;

    //Images used to represent the boss
    public Image charImage;
    public ImageView charImageview;

    /*
     *
     * make this character fight another boss
     * @param other
     */
    public void attack(Boss other){
    }

    /*
     * make this boss heal themselves
     */
    public void heal(){
    }
}

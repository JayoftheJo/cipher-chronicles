package CharacterFactory;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Character {

    //used to represent the health attribute for the character
    public double health;

    //used to represent the strength attribute for the character
    public double strength;

    //Images used to represent the character
    public Image charImage;
    public ImageView charImageview;

    /*
     *
     * make this character fight another character
     * @param other
     */
    public void attack(Character other){
    }

    /*
     * make this character heal themselves
     */
    public void heal(){
    }
}

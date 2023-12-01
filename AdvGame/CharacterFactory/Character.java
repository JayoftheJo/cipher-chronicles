package CharacterFactory;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Character {

    public double health;
    public double strength;

    public Image charImage;
    public ImageView charImageview;

    public void attack(Character other){
    }

    public void heal(Character other){
    }
}

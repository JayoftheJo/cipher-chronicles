package CharacterFactory;

/**
 * This class is used to create the boss character for the final battle
 */
public class concreteCharacterFactory implements characterFactory {

    /*
     * this method creates and returns a boss character
     */
    @Override
    public Character createBossCharacter() {
        return new trollBoss(150.0, 5.0);
    }
}

package CharacterFactory;

/**
 * this interface is used as a base for the concreteCharacterFactory
 */
public interface characterFactory {

    /*
    * abstract method left to initialize boss character
     */
    public Character createBossCharacter();
}

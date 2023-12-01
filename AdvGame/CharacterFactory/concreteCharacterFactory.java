package CharacterFactory;

public class concreteCharacterFactory implements characterFactory {
    @Override
    public Character createBossCharacter() {
        return new trollBoss(150.0, 5.0);
    }


}

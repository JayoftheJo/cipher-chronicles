package BossFactory;

/**
 * This class is used to create the boss character for the final battle
 */
public class concreteBossFactory implements bossFactory {

    /*
     * this method creates and returns a boss character
     */
    @Override
    public Boss createBossCharacter() {
        return new trollBoss(150.0, 5.0);
    }
}

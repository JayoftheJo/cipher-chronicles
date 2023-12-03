package BossFactory;

/**
 * this interface is used as a base for the concreteBossFactory
 */
public interface bossFactory {

    /*
    * abstract method left to initialize boss character
     */
    public Boss createBossCharacter();
}
